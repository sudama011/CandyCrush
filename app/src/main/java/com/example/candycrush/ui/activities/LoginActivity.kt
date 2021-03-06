package com.example.candycrush.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.candycrush.databinding.ActivityLoginBinding
import com.example.candycrush.R
import com.example.candycrush.doas.UserAmountDao
import com.example.candycrush.doas.UserDao
import com.example.candycrush.models.Amount
import com.example.candycrush.models.User
import com.example.candycrush.ui.activities.MainActivity
import com.example.candycrush.utils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var loading: ProgressBar
    private lateinit var mAmount: Amount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        loading = binding.loading
        val loginWithGoogle = binding.loginWithGoogle

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth




        username.afterTextChanged {

            username.text.toString()
            password.text.toString()

        }

        password.apply {
            afterTextChanged {

                username.text.toString()
                password.text.toString()

            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        username.text.toString()
                        password.text.toString()
                    }


                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                //    loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }

        loginWithGoogle.setOnClickListener { signIn() }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
     //   updateUiWithUser(currentUser)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSingInResult(task)
        }
    }

    private fun handleSingInResult(task: Task<GoogleSignInAccount>?) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task?.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        binding.loginWithGoogle.visibility = View.GONE
        loading.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val auth = auth.signInWithCredential(credential).await()

            val user = auth.user
            val userAmountDao = UserAmountDao()
            userAmountDao.getBalance(this@LoginActivity)
            withContext(Dispatchers.Main) {
                updateUiWithUser(user)
            }

        }
    }
    // [END auth_with_google]

    private fun updateUiWithUser(firebaseUser: FirebaseUser?) {

        if (firebaseUser!= null) {
            val user = User(firebaseUser.uid, firebaseUser.displayName,firebaseUser.email , firebaseUser.photoUrl.toString(),firebaseUser.phoneNumber.toString())
            val userDao = UserDao()
            userDao.registerUser(this ,user)
            // assign bonus


            val userAmountDao = UserAmountDao()


            if(mAmount.registerBonus != 1){
                val amount = Amount(firebaseUser.uid,firebaseUser.email, 0F,100F,1)
                userAmountDao.setBonus(amount)
            }
            val welcome = getString(R.string.welcome)
            val displayName = "${firebaseUser.displayName}"
            Toast.makeText(
                applicationContext,
                "$welcome $displayName",
                Toast.LENGTH_LONG
            ).show()
            loading.visibility = View.GONE
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra(Constants.USER_NAME,displayName)
            startActivity(intent)
            finish()
        } else {
            binding.loginWithGoogle.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }

    }

    fun showLoginFailed( errorString:String) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    fun amountGetSuccess(amount: Amount){
        mAmount = amount
    }

    /**
     * Extension function to simplify setting an afterTextChanged action to EditText components.
     */
    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

}
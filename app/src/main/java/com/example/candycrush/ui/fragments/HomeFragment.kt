package com.example.candycrush.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.example.candycrush.R
import com.example.candycrush.databinding.FragmentHomeBinding
import com.example.candycrush.doas.UserAmountDao
import com.example.candycrush.doas.UserDao
import com.example.candycrush.models.Amount
import com.example.candycrush.models.User
import com.example.candycrush.ui.activities.AddAmountActivity
import com.example.candycrush.ui.activities.MainActivity
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() , View.OnClickListener {

    lateinit var mView: View
    lateinit var mAmount: Amount
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // if current user is app will be crash
   GlobalScope.launch{

     try {
         val currentUserId = Firebase.auth.currentUser!!.uid
         val userDao = UserDao()
         val user = userDao.getUserById(currentUserId).await().toObject(User::class.java)
         val userAmountDao = UserAmountDao()
        // userAmountDao.getBalance(requireActivity())
         withContext(Main){
             mView.findViewById<AppCompatTextView>(R.id.tv_user_name).text = user!!.displayName
             mView.findViewById<TextView>(R.id.tv_balance).text = (mAmount.balance + mAmount.bonus).toString()

         }
     }catch (e: FirebaseAuthException ){

       Toast.makeText(requireContext(), "LogIn failed: " + e.message, Toast.LENGTH_LONG).show();
    }catch (e: FirebaseNetworkException) {
       Toast.makeText(requireContext(), "LogIn failed: " + e.message, Toast.LENGTH_LONG).show()
    }
   }

        mView=inflater.inflate(R.layout.fragment_home,container,false)

        val btnPlay = mView.findViewById<AppCompatButton>(R.id.btn_play)

            btnPlay.setOnClickListener(this)

        mView.findViewById<ImageButton>(R.id.ib_add_amount).setOnClickListener(this)

     val btnPractice =  mView.findViewById<AppCompatButton>(R.id.btn_practice)
         btnPractice.setOnClickListener(this)
        // Inflate the layout for this fragment
        return mView
    }

    override fun onClick(v: View?) {
        if (v !=null) {

            when(v.id){
                R.id.btn_practice -> {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_play -> {
                  val intent = Intent(requireContext(), MainActivity::class.java)
                  startActivity(intent)
                }
                R.id.ib_add_amount ->{
                    val intent = Intent(requireContext(), AddAmountActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    fun  amountGetSuccess(amount: Amount){
           mAmount = amount
    }

}
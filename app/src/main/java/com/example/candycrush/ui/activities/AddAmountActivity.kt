package com.example.candycrush.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.candycrush.R
import com.example.candycrush.databinding.ActivityAddAmountBinding
import com.example.candycrush.databinding.ActivityMainBinding
import com.example.candycrush.doas.UserDao
import com.example.candycrush.models.Amount
import com.example.candycrush.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.CheckoutActivity
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class AddAmountActivity : AppCompatActivity() , View.OnClickListener , PaymentResultListener {
   private  var mTotalAmount:Int = 0
    private lateinit var mPaymentId:String
    private lateinit var binding: ActivityAddAmountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAmountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd30.setOnClickListener(this)
        binding.btnAdd50.setOnClickListener(this)
        binding.btnAdd100.setOnClickListener(this)
        binding.btnAdd500.setOnClickListener(this)

    }

    private fun startPayment() {
        /* *  You need to pass current activity in order to let Razorpay create CheckoutActivity        * */

        val activity: Activity = this
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_XhAOFv7ub7GfZh")
        try {
            val options = JSONObject()
            options.put("name","Candy Crush")
            // options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from dashboard
           // options.put("image",)
            options.put("theme.color", "#FD089C");
            options.put("currency","INR");
            // options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount",mTotalAmount*100)
            // pass amount in currency subunits
            // val retryObj = new JSONObject();
            // retryObj.put("enabled", true);
            // retryObj.put("max_count", 4);
            // options.put("retry", retryObj);
            val prefill = JSONObject()
      //      prefill.put("email",mUserDetails.uemail)
      //      prefill.put("contact",mUserDetails.mobile)
            options.put("prefill",prefill)
            checkout.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentError(errorCode: Int, response: String?) {
        /**
         * Add your logic here for a failed payment response
         */
        Toast.makeText(this,"Payment failed please try again ", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, CheckoutActivity::class.java))
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        if(razorpayPaymentId != null){
            mPaymentId = razorpayPaymentId
            Toast.makeText(this,"Payment success full paymentId is $razorpayPaymentId", Toast.LENGTH_LONG).show()
            val currentUserId = Firebase.auth.currentUser!!.uid
            val currentUserEmail = Firebase.auth.currentUser!!.email
            val amount = Amount(currentUserId,currentUserEmail,)

        }else{
            Toast.makeText(this,"Payment success full but paymentId is null", Toast.LENGTH_LONG).show()
          //  placeAnOrder()
        }
        val intent = Intent(this,DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.btn_add_30 ->{
                  mTotalAmount = 30
                   startPayment()
                }
                R.id.btn_add_50 ->{
                    mTotalAmount = 50
                    startPayment()
                }
                R.id.btn_add_100 ->{
                    mTotalAmount = 100
                    startPayment()
                }
                R.id.btn_add_500 ->{
                    mTotalAmount = 500
                    startPayment()
                }

            }
        }
}
}
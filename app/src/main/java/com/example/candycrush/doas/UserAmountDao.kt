package com.example.candycrush.doas

import android.app.Activity
import android.util.Log
import com.example.candycrush.models.Amount
import com.example.candycrush.ui.activities.DashboardActivity
import com.example.candycrush.ui.activities.LoginActivity
import com.example.candycrush.ui.fragments.HomeFragment
import com.example.candycrush.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore

class UserAmountDao {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun setBonus(amount: Amount ) {

        mFireStore.collection(Constants.AMOUNT)
            .document()
            .set(amount).addOnSuccessListener {

            }

    }
    fun getBalance(activity: LoginActivity){


        Log.i("you are in try block","get balance")
        mFireStore.collection(Constants.AMOUNT)
            .document()
            .get().addOnSuccessListener {  document ->
                if (document.toObject(Amount::class.java) != null ){


                            activity.amountGetSuccess(document.toObject(Amount::class.java)!!)



                }
            }
    }

    fun getBalanceInFragment(fragment: HomeFragment){


        Log.i("you are in try block","get balance")
        mFireStore.collection(Constants.AMOUNT)
            .document()
            .get().addOnSuccessListener {  document ->
                if (document.toObject(Amount::class.java) != null ){

                            fragment.amountGetSuccess(document.toObject(Amount::class.java)!!)
                        }
                    }

                }



            }



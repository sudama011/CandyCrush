package com.example.candycrush.doas

import com.example.candycrush.models.User
import com.example.candycrush.ui.activities.LoginActivity
import com.example.candycrush.utils.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserDao {

    // Access a Cloud Firestore instance.
                 // db
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: LoginActivity, userInfo: User) {

      GlobalScope.launch(Dispatchers.IO){

        mFireStore.collection(Constants.USERS)

          // The "users" is collection name. If the collection is already created then it will not create the same one again.
          mFireStore.collection(Constants.USERS)
              // Document ID for users fields. Here the document it is the User ID.
              .document(userInfo.uid)
              // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
              .set(userInfo, SetOptions.merge())
    
//              .addOnSuccessListener {  }
            .addOnFailureListener {e->
                activity.showLoginFailed(e.toString())
            }

      }

    }

    fun getUserById(uId:String):Task<DocumentSnapshot>{
        // this is a task  you can use addOnSuccess or await to get data
        return    mFireStore.collection(Constants.USERS).document(uId).get()
    }
}
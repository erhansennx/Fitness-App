package com.app.ebfitapp.service

import android.app.Dialog
import android.content.Context
import android.widget.Toast
import com.app.ebfitapp.utils.CustomProgress
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthService(private val context: Context) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginAccount( email: String, password : String,task: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful)
                {
                    task(true)
                    //val user = auth.currentUser

                }
                else {
                    task(false)
                    //Show Tost Message
                }
            }
            .addOnFailureListener()
            {
                task(false)
                showErrorToastMessage(it.message.toString())
            }
    }

    fun getForgotPassowrd(sPassword : String , dialog: Dialog, customProgress: CustomProgress)
    {
        auth.sendPasswordResetEmail(sPassword)
            .addOnSuccessListener {
                customProgress.dismiss()
                Toast.makeText(context, "Please Check Your Email Address", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .addOnFailureListener {
                customProgress.dismiss()
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()


            }
    }

    fun createAccount(email: String, password: String, task: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) task(true)
        }.addOnFailureListener {
            task(false)
            showErrorToastMessage(it.message.toString())
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUserEmail(): String {
        return auth.currentUser?.email.toString()
    }

    private fun showErrorToastMessage(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }



}
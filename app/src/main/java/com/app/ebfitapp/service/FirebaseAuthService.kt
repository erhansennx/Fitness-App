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
            if (it.isSuccessful) task(true)
            else task(false)
        }.addOnFailureListener() {
            task(false)
            showErrorToastMessage(it.message.toString())
        }
    }

    fun getForgotPassowrd(sPassword : String , dialog: Dialog, customProgress: CustomProgress) {
        auth.sendPasswordResetEmail(sPassword)
            .addOnSuccessListener {
                customProgress.dismiss()
                showErrorToastMessage("Please Check Your Email Address")
                dialog.dismiss()
            }.addOnFailureListener {
                customProgress.dismiss()
                showErrorToastMessage(it.message.toString())
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
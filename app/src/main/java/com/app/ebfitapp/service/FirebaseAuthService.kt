package com.app.ebfitapp.service

import android.content.Context
import android.widget.Toast
import com.app.ebfitapp.utils.CustomProgress
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthService(private val context: Context) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var customProgress: CustomProgress = CustomProgress(context)

    fun loginAccount() {}

    fun createAccount(email: String, password: String, task: (Boolean) -> Unit) {
        customProgress.show()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) task(true)
            customProgress.dismiss()
        }.addOnFailureListener {
            task(false)
            showErrorToastMessage(it.message.toString())
            customProgress.dismiss()
        }
    }

    fun signOut() {
        auth.signOut()
    }

    private fun showErrorToastMessage(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

}
package com.app.ebfitapp.service

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthService(private val context: Context) {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginAccount() {}

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

    private fun showErrorToastMessage(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

}
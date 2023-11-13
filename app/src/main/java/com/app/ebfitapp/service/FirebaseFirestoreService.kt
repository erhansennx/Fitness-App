package com.app.ebfitapp.service

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseFirestoreService(private val context: Context) {

    var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun createProfileDetail(dataMap: HashMap<String,Any?>, callback: (Boolean) -> Unit) {
        firestore.collection("profileDetail").document(dataMap["email"].toString()).set(dataMap).addOnCompleteListener { task ->
            if (task.isSuccessful) callback(true)
        }.addOnFailureListener {
            callback(false)
            showErrorToastMessage(it.message.toString())
        }
    }

    fun updateProfileDetail(email: String, updateData: HashMap<String,Any>, callback: (Boolean) -> Unit) {
        firestore.collection("profileDetail").document(email).update(updateData).addOnCompleteListener { task ->
            if (task.isSuccessful) callback(true)
        }.addOnFailureListener {
            callback(false)
            showErrorToastMessage(it.message.toString())
        }
    }

    private fun showErrorToastMessage(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

}
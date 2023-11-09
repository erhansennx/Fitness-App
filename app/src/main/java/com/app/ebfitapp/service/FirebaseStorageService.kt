package com.app.ebfitapp.service

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class FirebaseStorageService(private val context: Context) {

    private var firebaseAuthService = FirebaseAuthService(context)
    private var currentUserEmail = firebaseAuthService.getCurrentUserEmail()
    private var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()

    fun uploadImage(imageURI: Uri, imageURL: (String?) -> Unit) {
        val uuid = UUID.randomUUID()
        val reference = firebaseStorage.reference.child("profilePhotos/$currentUserEmail").child("$uuid.jpg")

        reference.putFile(imageURI).addOnCompleteListener { uploadTask ->
            if (uploadTask.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { downloadURL ->
                    imageURL(downloadURL.toString())
                }.addOnFailureListener {
                    imageURL(null)
                }
            } else imageURL(null)
        }.addOnFailureListener {
            imageURL(null)
            showErrorToastMessage(it.message.toString())
        }
    }

    private fun showErrorToastMessage(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

}
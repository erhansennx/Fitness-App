package com.app.ebfitapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.ebfitapp.model.MuscleGroupModel
import com.app.ebfitapp.service.FirebaseFirestoreService

class WorkoutViewModel(private val application: Application) : AndroidViewModel(application) {

    val muscleGroupLiveData = MutableLiveData<ArrayList<MuscleGroupModel>?>()

    private var muscleTempList: ArrayList<MuscleGroupModel> = ArrayList()
    private val firestoreService = FirebaseFirestoreService(application.applicationContext)

    fun getMuscleGroups() {
        firestoreService.firestore.collection("muscleGroups").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (snapshot in task.result.documents) {
                    val muscleGroupModel = MuscleGroupModel(snapshot.data!!["muscle"].toString(), snapshot.data!!["muscleImage"].toString())
                    muscleTempList.add(muscleGroupModel)
                }
                muscleGroupLiveData.value = muscleTempList
            } else {
                muscleGroupLiveData.value = null
            }
        }.addOnFailureListener {
            muscleGroupLiveData.value = null
            showErrorToastMessage(it.message.toString())
        }

    }

    private fun showErrorToastMessage(error: String) {
        Toast.makeText(application.applicationContext, error, Toast.LENGTH_LONG).show()
    }


}
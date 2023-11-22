package com.app.ebfitapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.ebfitapp.model.BestTrainersModel
import com.app.ebfitapp.model.MuscleGroupModel
import com.app.ebfitapp.service.FirebaseFirestoreService

class WorkoutViewModel(private val application: Application) : AndroidViewModel(application) {

    val muscleGroupLiveData = MutableLiveData<ArrayList<MuscleGroupModel>?>()
    val bestTrainersLiveData = MutableLiveData<ArrayList<BestTrainersModel>?>()

    private var muscleTempList: ArrayList<MuscleGroupModel> = ArrayList()
    private var trainersTempList: ArrayList<BestTrainersModel> = ArrayList()
    private val firestoreService = FirebaseFirestoreService(application.applicationContext)

    fun getMuscleGroups() {
        firestoreService.firestore.collection("muscleGroups").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (snapshot in task.result.documents) {
                    val muscleGroupModel = MuscleGroupModel(snapshot.data!!["muscle"].toString(), snapshot.data!!["muscleImage"].toString())
                    muscleTempList.add(muscleGroupModel)
                }
                muscleGroupLiveData.value = muscleTempList
                muscleTempList.clear()
            } else {
                muscleGroupLiveData.value = null
            }
        }.addOnFailureListener {
            muscleGroupLiveData.value = null
            showErrorToastMessage(it.message.toString())
        }

    }

    fun getBestTrainers() {
        firestoreService.firestore.collection("bestTrainers").get().addOnCompleteListener {  query ->
            if (query.isSuccessful) {
                for (snapshot in query.result.documents) {
                    val bestTrainersModel = BestTrainersModel(snapshot.data!!["profileImageURL"].toString(), snapshot.data!!["username"].toString(), snapshot.data!!["specialization"].toString())
                    trainersTempList.add(bestTrainersModel)
                }
                bestTrainersLiveData.value = trainersTempList
                trainersTempList.clear()
            } else bestTrainersLiveData.value = null

        }.addOnFailureListener {
            bestTrainersLiveData.value = null
            showErrorToastMessage(it.message.toString())
        }
    }

    private fun showErrorToastMessage(error: String) {
        Toast.makeText(application.applicationContext, error, Toast.LENGTH_LONG).show()
    }


}
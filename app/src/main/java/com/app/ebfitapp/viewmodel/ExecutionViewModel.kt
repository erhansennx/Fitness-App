package com.app.ebfitapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ebfitapp.model.ExecutionModel
import com.app.ebfitapp.utils.StreakManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExecutionViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    fun saveWorkout(execution: ExecutionModel, result: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            firestore.collection("executionExercises").document().set(execution).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkStreak()
                    result(true, "Exercise Saved Successfully!")
                } else result(false, "An error occurred while recording the exercise!")
            }.addOnFailureListener {
                result(false, "An error occurred while recording the exercise!")
            }
        }
    }

    private fun checkStreak() {
        viewModelScope.launch {
            StreakManager.updateStreak()
        }
    }

}
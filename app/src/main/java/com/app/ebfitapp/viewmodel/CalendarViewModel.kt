package com.app.ebfitapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.app.ebfitapp.model.ToDoModel
import com.app.ebfitapp.service.FirebaseAuthService
import com.app.ebfitapp.service.FirebaseFirestoreService

class CalendarViewModel(private val application: Application) : AndroidViewModel(application) {

    private val firestoreService = FirebaseFirestoreService(application.applicationContext)
    private val firebaseAuthService = FirebaseAuthService(application.applicationContext)
    fun addToDoItem(selectedDay: String?, selectedDate: String?, todoText: String, callback: (Boolean) -> Unit) {
        val currentEmail = firebaseAuthService.getCurrentUserEmail()

        val userDocumentReference = firestoreService.firestore.collection("toDoRecyclerViewItems").document(currentEmail)

        val dataMap = hashMapOf(
            "selectedDay" to selectedDay,
            "selectedDate" to selectedDate,
            "todoText" to todoText
        )

        userDocumentReference.collection("toDoRecyclerViewItems")
            .add(dataMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
    }

    fun getToDoItems(callback: (List<ToDoModel>, List<Pair<String, String>>) -> Unit) {
        val currentEmail = firebaseAuthService.getCurrentUserEmail()

        val userDocumentReference = firestoreService.firestore.collection("toDoRecyclerViewItems").document(currentEmail)

        userDocumentReference.collection("toDoRecyclerViewItems")
            .get()
            .addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    val toDoList = mutableListOf<ToDoModel>()
                    val dayDateList = mutableListOf<Pair<String, String>>()

                    for (document in querySnapshot.result!!) {
                        val selectedDay = document.getString("selectedDay")
                        val selectedDate = document.getString("selectedDate")
                        val todoText = document.getString("todoText")

                        if (selectedDay != null && selectedDate != null && todoText != null) {
                            val toDoItem = ToDoModel(selectedDay, selectedDate, todoText)
                            toDoList.add(toDoItem)
                            dayDateList.add(selectedDay to selectedDate)
                        }
                    }

                    callback(toDoList, dayDateList)
                } else {
                    callback(emptyList(), emptyList())
                }
            }
    }



}
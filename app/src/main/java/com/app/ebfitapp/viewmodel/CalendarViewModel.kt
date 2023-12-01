package com.app.ebfitapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.app.ebfitapp.model.ToDoModel
import com.app.ebfitapp.service.FirebaseAuthService
import com.app.ebfitapp.service.FirebaseFirestoreService

class CalendarViewModel(private val application: Application) : AndroidViewModel(application) {

    private val firestoreService = FirebaseFirestoreService(application.applicationContext)
    private val firebaseAuthService = FirebaseAuthService(application.applicationContext)
    val currentEmail = firebaseAuthService.getCurrentUserEmail()
    val userDocumentReference = firestoreService.firestore.collection("toDoRecyclerViewItems").document(currentEmail)
    fun addToDoItem(todoArray : ToDoModel, callback: (Boolean) -> Unit) {

        val dataMap = hashMapOf(
            "selectedDay" to todoArray.selectedDay,
            "selectedDate" to todoArray.selectedDate,
            "todoText" to todoArray.todoText,
            "todoId" to todoArray.todoId
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
    fun getToDoItems(callback: (List<ToDoModel>) -> Unit) {
        userDocumentReference.collection("toDoRecyclerViewItems")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val toDoList = mutableListOf<ToDoModel>()

                    for (document in task.result!!) {
                        val selectedDay = document.getString("selectedDay") ?: ""
                        val selectedDate = document.getString("selectedDate") ?: ""
                        val todoText = document.getString("todoText") ?: ""
                        val todoId = document.getString("todoId") ?: ""
                        val toDoItem = ToDoModel(selectedDay, selectedDate, todoText, todoId)
                        toDoList.add(toDoItem)
                    }

                    callback(toDoList)
                } else {
                    showErrorToastMessage(task.toString())
                    callback(emptyList())
                }
            }
    }
    private fun showErrorToastMessage(error: String) {
        Toast.makeText(application.applicationContext, error, Toast.LENGTH_LONG).show()
    }


}
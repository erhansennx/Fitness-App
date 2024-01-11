package com.app.ebfitapp.utils

import android.content.Context
import android.widget.Toast
import com.app.ebfitapp.model.StreakModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

object StreakManager {

    private var streakCounter = 0
    private var streakModel: StreakModel?= null
    private val email = FirebaseAuth.getInstance().currentUser!!.email
    private const val STREAKS_COLLECTION = "streaks"
    private val streaksRef = FirebaseFirestore.getInstance().collection(STREAKS_COLLECTION).document(email!!)

    fun getCurrentStreak(context: Context, callback: (Int?) -> Unit) {
        streaksRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val date = documentSnapshot.data!!["date"].toString()
                val counter = documentSnapshot.data!!["count"].toString().toInt()
                streakModel = StreakModel(counter, date, email!!)
                streakCounter = counter

                if (calculateDateDifferent(date, getTodayDate()) > 1) {
                    streakModel!!.count = -1
                    streakCounter = 0
                    updateStreak()
                }

                callback(streakCounter)
            } else {
                streaksRef.set(hashMapOf<String, Any>("count" to 0, "date" to "", "email" to email!!)).addOnCompleteListener { setTask ->
                    if (setTask.isSuccessful) {
                        streaksRef.get().addOnSuccessListener { newDocumentSnapshot ->
                            if (newDocumentSnapshot.exists()) {
                                val date = newDocumentSnapshot.data!!["date"].toString()
                                val counter = newDocumentSnapshot.data!!["count"].toString().toInt()
                                streakModel = StreakModel(counter, date, email)
                                streakCounter = counter
                                callback(streakCounter)
                            } else {
                                callback(null)
                            }
                        }.addOnFailureListener {
                            Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                            callback(null)
                        }
                    } else {
                        Toast.makeText(context, "An error occurred while creating the document: ${setTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        callback(null)
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
            callback(null)
        }
    }


    fun updateStreak() {
        val today = getTodayDate()
        if (streakModel!!.date != today) {
            val increaseStreak = streakModel!!.count + 1
            val streakMap = hashMapOf<String, Any>("count" to increaseStreak, "date" to today)
            streaksRef.update(streakMap)
            streakModel!!.date = today
        } else {
            val streakMap = hashMapOf<String, Any>("count" to 1, "date" to today)
            streaksRef.update(streakMap)
        }
    }

    private fun getTodayDate(): String {
        return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(
            Calendar.getInstance().time
        )
    }

    private fun calculateDateDifferent(streakDate: String, currentDate: String): Long {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        return try {
            val streak = dateFormat.parse(streakDate)!!
            val current = dateFormat.parse(currentDate)!!
            val diffInMilliseconds = abs(current.time - streak.time)

            diffInMilliseconds / (24 * 60 * 60 * 1000)
        } catch (e: ParseException) {
            e.printStackTrace()
            -1
        }
    }

}
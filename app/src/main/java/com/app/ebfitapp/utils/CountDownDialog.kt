package com.app.ebfitapp.utils

import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.widget.TextView
import com.app.ebfitapp.R

class CountDownDialog(context: Context) {

    private var countDownDialog =  Dialog(context)

    fun showCountDownDialog(millisecond: Long, callback:(Boolean) -> Unit) {
        countDownDialog.setContentView(R.layout.custom_countdown)
        countDownDialog.setCancelable(false)
        countDownDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val countdownText = countDownDialog.findViewById<TextView>(R.id.countdownText)

        val countDownTimer = object : CountDownTimer(millisecond, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                countdownText.text = secondsLeft.toString()
            }

            override fun onFinish() {
                countDownDialog.dismiss()
                callback(true)
            }
        }

        countDownTimer.start()
        countDownDialog.show()
    }

}
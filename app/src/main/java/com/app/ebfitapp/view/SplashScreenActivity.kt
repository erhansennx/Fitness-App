package com.app.ebfitapp.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var activitySplashScreenBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(activitySplashScreenBinding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val intentClass = if (auth.currentUser != null) MainActivity::class.java else AuthenticationActivity::class.java
            startActivity(Intent(this@SplashScreenActivity, intentClass))
            finish()
        }, 3500)


    }



}
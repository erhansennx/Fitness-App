package com.app.ebfitapp.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.ActivitySplashScreenBinding
import com.app.ebfitapp.utils.AppPreferences
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var appPreferences: AppPreferences
    private lateinit var activitySplashScreenBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(activitySplashScreenBinding.root)

        appPreferences = AppPreferences(this@SplashScreenActivity)

        Handler(Looper.getMainLooper()).postDelayed({

            val currentUser = auth.currentUser
            val rememberMe = appPreferences.getBoolean("rememberMe")

            val targetActivity = if (currentUser != null && rememberMe) MainActivity::class.java else AuthenticationActivity::class.java

            val intent = Intent(this@SplashScreenActivity, targetActivity)
            startActivity(intent)
            finish()

        }, 3500)

    }


}
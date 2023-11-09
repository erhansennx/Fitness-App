package com.app.ebfitapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.ActivityAuthenticationBinding
import com.google.firebase.auth.FirebaseAuth

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var authenticationBinding: ActivityAuthenticationBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticationBinding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(authenticationBinding.root)

    }

    override fun onStart()
    {
        super.onStart()
        if(auth.currentUser != null)
        {
            val intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}
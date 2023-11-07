package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var fragmentWelcomeBinding: FragmentWelcomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentWelcomeBinding = FragmentWelcomeBinding.inflate(layoutInflater)


        return fragmentWelcomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(fragmentWelcomeBinding) {

            getStartedButton.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeFragment_to_signUpFragment)
            }

            loginButton.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            }

        }


    }

}
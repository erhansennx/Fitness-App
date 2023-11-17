package com.app.ebfitapp.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentLoginBinding
import com.app.ebfitapp.service.FirebaseAuthService
import com.app.ebfitapp.utils.AppPreferences
import com.app.ebfitapp.utils.CustomProgress
import com.app.ebfitapp.view.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var email: String? = null
    private var password: String? = null
    private lateinit var appPreferences: AppPreferences
    private lateinit var customProgress: CustomProgress
    private lateinit var firebaseAuthService: FirebaseAuthService
    private lateinit var fragmentLoginBinding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLoginBinding = FragmentLoginBinding.inflate(layoutInflater)

        customProgress = CustomProgress(requireContext())
        appPreferences = AppPreferences(requireContext())
        firebaseAuthService = FirebaseAuthService(requireContext())

        return fragmentLoginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(fragmentLoginBinding) {
            onLoginButton.setOnClickListener() {
                email = loginEmailText.text.toString()
                password = loginPasswordText.text.toString()

                if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.please_fill_in_the_empty_fields), Toast.LENGTH_LONG).show()
                } else {
                    customProgress.show()
                    firebaseAuthService.loginAccount(email = email!!, password = password!!) { task ->
                        if (task) {
                            customProgress.dismiss()
                            checkRememberMe()
                            requireActivity().finish()
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            customProgress.dismiss()
                        }
                    }
                }
            }

            forgotPasswordText.setOnClickListener() {
                showDialog()
            }
            createAccountText.setOnClickListener()
            {
                    val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
                    Navigation.findNavController(requireView()).navigate(action)
            }

        }
    }

    private fun checkRememberMe() {
        if (fragmentLoginBinding.rememberMeCheckBox.isChecked) appPreferences.saveBoolean("rememberMe", true)
        else appPreferences.saveBoolean("rememberMe", false)
    }

    private fun showDialog() {

        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetlayout)

        val resetEmailAddress = dialog.findViewById<EditText>(R.id.resetEmailAddress)
        val resetButton = dialog.findViewById<Button>(R.id.resetButton)

        resetButton?.setOnClickListener {

            if (!resetEmailAddress?.text.isNullOrEmpty()) {
                customProgress.show()
                val sPassword = resetEmailAddress?.text.toString()
                firebaseAuthService.getForgotPassowrd(sPassword, dialog, customProgress)
            } else {
                Toast.makeText(requireContext(), "Fill the blank please", Toast.LENGTH_SHORT).show()
            }

        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

    }

}
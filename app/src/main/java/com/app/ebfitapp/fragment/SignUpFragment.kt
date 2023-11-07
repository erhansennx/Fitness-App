package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentSignUpBinding
import com.app.ebfitapp.service.FirebaseAuthService

class SignUpFragment : Fragment() {

    private var username: String? = null
    private var email: String? = null
    private var password: String? = null
    private var gender: String? = null

    private lateinit var firebaseAuthService: FirebaseAuthService
    private lateinit var fragmentSignUpBinding: FragmentSignUpBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSignUpBinding = FragmentSignUpBinding.inflate(layoutInflater)

        settings()
        firebaseAuthService = FirebaseAuthService(requireContext())

        return fragmentSignUpBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(fragmentSignUpBinding) {

            genderDropDown.setOnItemClickListener { adapterView, view, i, l ->
                gender = adapterView.getItemAtPosition(i).toString()
            }

            contiuneButton.setOnClickListener {
                username = usernameText.text.toString()
                email = emailText.text.toString()
                password = passwordText.text.toString()

                if (username.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || gender.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.please_fill_in_the_empty_fields), Toast.LENGTH_LONG).show()
                } else {
                    firebaseAuthService.createAccount(email = email!!, password = password!!) { task ->
                        if (task) {
                            val action = SignUpFragmentDirections.actionSignUpFragmentToProfileDetailFragment()
                            Navigation.findNavController(requireView()).navigate(action)
                        }
                    }
                }
            }

        }

    }

    private fun settings() {
        val genderItems = listOf(getString(R.string.male), getString(R.string.female), getString(R.string.prefer_not_to_say))
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_items, genderItems)
        fragmentSignUpBinding.genderDropDown.setAdapter(adapter)
    }

}
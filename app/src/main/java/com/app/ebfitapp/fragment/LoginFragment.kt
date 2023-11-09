package com.app.ebfitapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentLoginBinding
import com.app.ebfitapp.service.FirebaseAuthService
import com.app.ebfitapp.utils.CustomProgress
import com.app.ebfitapp.view.MainActivity

class LoginFragment : Fragment() {
    private var email: String? = null
    private var password: String? = null
    private lateinit var customProgress: CustomProgress
    private lateinit var firebaseAuthService: FirebaseAuthService
    private lateinit var fragmentLoginBinding : FragmentLoginBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLoginBinding = FragmentLoginBinding.inflate(layoutInflater)
        customProgress = CustomProgress(requireContext())
        firebaseAuthService = FirebaseAuthService(requireContext())
        return fragmentLoginBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(fragmentLoginBinding)
        {
            onLoginButton.setOnClickListener()
            {
                    email = loginEmailText.text.toString()
                    password = loginPasswordText.text.toString()

                if(email.isNullOrEmpty() || password.isNullOrEmpty())
                {
                    Toast.makeText(requireContext(), getString(R.string.please_fill_in_the_empty_fields), Toast.LENGTH_LONG).show()
                }else
                {
                    customProgress.show()
                    firebaseAuthService.loginAccount(email = email!! ,password = password!!) { task ->
                        if(task)
                        {   //Authentication succesful
                            customProgress.dismiss()

                            val intent =Intent(requireContext(),MainActivity::class.java)
                            startActivity(intent)

                            val fragmentTransaction = requireFragmentManager().beginTransaction()
                            fragmentTransaction.remove(this@LoginFragment)
                            fragmentTransaction.commit()
                        }
                        else{
                            //Authentication unsuccesful
                            customProgress.dismiss()
                        }
                    }
                }

            }
        }
    }


}
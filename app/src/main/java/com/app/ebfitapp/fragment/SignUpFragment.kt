package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var fragmentSignUpBinding: FragmentSignUpBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSignUpBinding = FragmentSignUpBinding.inflate(layoutInflater)

        settings()

        return fragmentSignUpBinding.root
    }

    private fun settings() {
        val genderItems = listOf(getString(R.string.male), getString(R.string.female), getString(R.string.prefer_not_to_say))
        val adapter = ArrayAdapter(requireContext(), R.layout.gender_items, genderItems)
        fragmentSignUpBinding.genderDropDown.setAdapter(adapter)
        //fragmentSignUpBinding.seekBar.isEnabled = false
    }

}
package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentProfileEditBinding


class ProfileEditFragment : Fragment() {

    private lateinit var editProfileBinding : FragmentProfileEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        editProfileBinding = FragmentProfileEditBinding.inflate(layoutInflater)
        return editProfileBinding.root
    }


}
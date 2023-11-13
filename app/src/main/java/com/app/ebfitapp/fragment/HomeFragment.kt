package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentHomeBinding
import com.app.ebfitapp.utils.CustomProgress
import com.app.ebfitapp.utils.downloadImageFromURL
import com.app.ebfitapp.viewmodel.MainViewModel

class HomeFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var customProgress: CustomProgress
    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

        customProgress = CustomProgress(requireActivity())
        customProgress.show()

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.getProfileDetail()
        observeProfileDetail()

        return fragmentHomeBinding.root
    }

    private fun observeProfileDetail() {
        mainViewModel.profileDetails.observe(requireActivity(), Observer { userProfileDetails ->
            customProgress.dismiss()
            if (userProfileDetails != null) {
                fragmentHomeBinding.homeRootLinear.visibility = View.VISIBLE
                fragmentHomeBinding.nicknameText.text = "Hey, ${userProfileDetails.username}!"
            }
        })
    }

}
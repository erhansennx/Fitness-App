package com.app.ebfitapp.fragment

import android.annotation.SuppressLint
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

        disableSeekBar()

        return fragmentHomeBinding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun disableSeekBar() {
        fragmentHomeBinding.circularSeekBar.setOnTouchListener { view, motionEvent -> true }
    }

    private fun observeProfileDetail() {
        mainViewModel.profileDetails.observe(requireActivity(), Observer { userProfileDetails ->
            customProgress.dismiss()
            if (userProfileDetails != null) {
                fragmentHomeBinding.homeRootLinear.visibility = View.VISIBLE
                fragmentHomeBinding.profileImage.downloadImageFromURL(userProfileDetails.profileImageURL.toString())
                fragmentHomeBinding.nicknameText.text = "${userProfileDetails.username} ${getString(R.string.wave_hand)}"
            }
        })
    }

}
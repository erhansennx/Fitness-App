package com.app.ebfitapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentHomeBinding
import com.app.ebfitapp.utils.CustomProgress
import com.app.ebfitapp.utils.downloadImageFromURL
import com.app.ebfitapp.viewmodel.MainViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable

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

        chips()

        return fragmentHomeBinding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun disableSeekBar() {
        fragmentHomeBinding.circularSeekBar.setOnTouchListener { view, motionEvent -> true }
    }

    private fun chips() {
        val chipTextArray = arrayOf("All","Yoga", "Cardio", "Strectch", "Food", "Calori")

        for (chipText in chipTextArray) {
            val chip = Chip(requireContext())
            chip.text = chipText
            chip.isCheckable = true
            chip.chipBackgroundColor = ContextCompat.getColorStateList(requireContext(), R.color.chip_background)
            chip.setChipStrokeColorResource(R.color.light_gray)
            chip.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            fragmentHomeBinding.chipGroup.addView(chip)
        }
        
        fragmentHomeBinding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChipId = group.findViewById<Chip>(group.checkedChipId)
            if (selectedChipId != null) Toast.makeText(requireContext(), "${selectedChipId.text}", Toast.LENGTH_LONG).show()
        }
        
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
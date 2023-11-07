package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentProfileDetailBinding

class ProfileDetailFragment : Fragment() {

    private lateinit var fragmentProfileDetailBinding: FragmentProfileDetailBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentProfileDetailBinding = FragmentProfileDetailBinding.inflate(layoutInflater)

        val goalItems = listOf(getString(R.string.weightLoss), getString(R.string.weightGain), getString(R.string.increaseEndurance), getString(R.string.buildMuscleMass), getString(R.string.improveFlexibility))
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_items, goalItems)
        fragmentProfileDetailBinding.goalDropDown.setAdapter(adapter)

        return fragmentProfileDetailBinding.root
    }

}
package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentBodyFatPercentageBinding


class BodyFatPercentageFragment : Fragment() {

    private lateinit var bodyFatPercentageBinding: FragmentBodyFatPercentageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bodyFatPercentageBinding = FragmentBodyFatPercentageBinding.inflate(layoutInflater)
        return bodyFatPercentageBinding.root
    }
}
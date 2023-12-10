package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentIdealWeightBinding


class IdealWeightFragment : Fragment() {
   private lateinit var idealWeightBinding: FragmentIdealWeightBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        idealWeightBinding = FragmentIdealWeightBinding.inflate(layoutInflater)
        return idealWeightBinding.root
    }



}
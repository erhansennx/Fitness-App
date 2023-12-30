package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentRopeCalorieBurnedBinding


class RopeCalorieBurnedFragment : Fragment() {

    private lateinit var bindingRope : FragmentRopeCalorieBurnedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingRope = FragmentRopeCalorieBurnedBinding.inflate(layoutInflater)
        return bindingRope.root
    }

}
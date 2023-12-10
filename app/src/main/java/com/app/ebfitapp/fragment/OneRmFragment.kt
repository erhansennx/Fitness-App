package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentCalculatorBinding
import com.app.ebfitapp.databinding.FragmentOneRmBinding


class OneRmFragment : Fragment() {
    private lateinit var fragmentOneRmFragment: FragmentOneRmBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentOneRmFragment = FragmentOneRmBinding.inflate(layoutInflater)
        return fragmentOneRmFragment.root
    }


}
package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ebfitapp.databinding.FragmentCalculatorBinding

class CalculatorFragment : Fragment() {
    private lateinit var fragmentCalculatorBinding : FragmentCalculatorBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentCalculatorBinding = FragmentCalculatorBinding.inflate(layoutInflater)
        return fragmentCalculatorBinding.root
    }

}
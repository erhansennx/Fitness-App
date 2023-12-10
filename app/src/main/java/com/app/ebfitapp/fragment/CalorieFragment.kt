package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentCalorieBinding


class CalorieFragment : Fragment() {
    private lateinit var calorieBinding: FragmentCalorieBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calorieBinding = FragmentCalorieBinding.inflate(layoutInflater)
        return calorieBinding.root
    }
}
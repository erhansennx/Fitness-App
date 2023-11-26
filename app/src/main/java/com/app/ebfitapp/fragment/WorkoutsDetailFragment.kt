package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentWorkoutsDetailBinding
import com.app.ebfitapp.model.PopularWorkoutsModel
import com.app.ebfitapp.utils.downloadImageFromURL


class WorkoutsDetailFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutsDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWorkoutsDetailBinding.inflate(layoutInflater)

        arguments.let {

            val model: PopularWorkoutsModel = it!!.getSerializable("workout") as PopularWorkoutsModel
            binding.programImage.downloadImageFromURL(model.imageURL)
            binding.programText.text = model.workoutName
            binding.descriptionText.text = model.description
            
        }


        return binding.root
    }



}
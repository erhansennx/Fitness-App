package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentExerciseExecutionBinding
import com.app.ebfitapp.model.BodyPartExercisesItem
import com.app.ebfitapp.utils.downloadGifFromURL

class ExerciseExecutionFragment : Fragment() {

    private lateinit var exerciseItem: BodyPartExercisesItem
    private lateinit var binding: FragmentExerciseExecutionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExerciseExecutionBinding.inflate(layoutInflater)

        arguments.let {

            exerciseItem = it!!.getSerializable("exercise") as BodyPartExercisesItem

            binding.exerciseGifView.downloadGifFromURL(exerciseItem.gifUrl)
            binding.exerciseName.text = exerciseItem.name

        }


        return binding.root
    }


}
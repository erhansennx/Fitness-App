package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.ebfitapp.databinding.FragmentMusclesDetailBinding
import com.app.ebfitapp.model.MuscleGroupModel
import com.app.ebfitapp.utils.downloadImageFromURL
import com.app.ebfitapp.viewmodel.MuscleExercisesViewModel

class MusclesDetailFragment : Fragment() {


    private lateinit var binding: FragmentMusclesDetailBinding
    private val muscleExercisesViewModel: MuscleExercisesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMusclesDetailBinding.inflate(layoutInflater)

        arguments.let {

            val muscleName = it!!.getSerializable("muscle") as MuscleGroupModel
            binding.muscleImage.downloadImageFromURL(muscleName.muscleImageURL)
            binding.muscleNameText.text = muscleName.muscleName

            muscleExercisesViewModel.getExercises(muscleName.muscleName.lowercase())
            observeBodyPartExercises()

        }


        return binding.root
    }


    private fun observeBodyPartExercises() {
        muscleExercisesViewModel.bodyPartExercises.observe(viewLifecycleOwner, Observer { result ->
            if (result != null) {
                Toast.makeText(requireContext(), "Data: ${result[0]}", Toast.LENGTH_LONG).show()
            }
        })
    }



}
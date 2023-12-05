package com.app.ebfitapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.app.ebfitapp.R
import com.app.ebfitapp.adapter.ExercisesAdapter
import com.app.ebfitapp.databinding.FragmentMusclesDetailBinding
import com.app.ebfitapp.model.BodyPartExercises
import com.app.ebfitapp.model.MuscleGroupModel
import com.app.ebfitapp.utils.downloadImageFromURL
import com.app.ebfitapp.viewmodel.MuscleExercisesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MusclesDetailFragment : Fragment() {

    private lateinit var exercisesAdapter: ExercisesAdapter
    private lateinit var bodyPartExercises: BodyPartExercises
    private lateinit var binding: FragmentMusclesDetailBinding
    private val muscleExercisesViewModel: MuscleExercisesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMusclesDetailBinding.inflate(layoutInflater)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)?.visibility = View.VISIBLE

        bodyPartExercises = BodyPartExercises()
        exercisesAdapter = ExercisesAdapter(bodyPartExercises)

        arguments.let {

            val muscleName = it!!.getSerializable("muscle") as MuscleGroupModel
            binding.muscleImage.downloadImageFromURL(muscleName.muscleImageURL)
            binding.muscleNameText.text = muscleName.muscleName

            muscleExercisesViewModel.getExercises(muscleName.muscleName.lowercase())
            observeBodyPartExercises()

        }


        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun observeBodyPartExercises() = with(binding) {
        muscleExercisesViewModel.bodyPartExercises.observe(viewLifecycleOwner, Observer { result ->
            if (result != null) {
                // Toast.makeText(requireContext(), "Data: ${result[0]}", Toast.LENGTH_LONG).show()
                bodyPartExercises.clear()
                bodyPartExercises.addAll(result)
                exerciseRecycler.adapter = exercisesAdapter
                exerciseRecycler.adapter?.notifyDataSetChanged()
            }
        })
    }



}
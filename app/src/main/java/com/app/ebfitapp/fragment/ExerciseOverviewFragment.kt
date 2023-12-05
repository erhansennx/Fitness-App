package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentExerciseOverviewBinding
import com.app.ebfitapp.model.BodyPartExercisesItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class ExerciseOverviewFragment : Fragment() {


    private lateinit var exerciseOverviewBinding: FragmentExerciseOverviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        exerciseOverviewBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.fragment_exercise_overview, container, false)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)?.visibility = View.GONE

        arguments.let {

            val exerciseItem = it!!.getSerializable("exercise") as BodyPartExercisesItem
            exerciseOverviewBinding.exercise = exerciseItem

            setInstructionsToTextView(exerciseItem.instructions)

        }

        return exerciseOverviewBinding.root
    }


    fun setInstructionsToTextView(instructions: List<String>) {
        val stringBuilder = StringBuilder()
        var instructionNumber = 1

        for (instruction in instructions) {
            stringBuilder.append("$instructionNumber. $instruction\n")
            instructionNumber++
        }

        exerciseOverviewBinding.instructionsText.text= stringBuilder.toString()
    }


}
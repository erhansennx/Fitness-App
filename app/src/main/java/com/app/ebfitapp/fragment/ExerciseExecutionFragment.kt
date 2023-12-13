package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentExerciseExecutionBinding
import com.app.ebfitapp.model.BodyPartExercisesItem
import com.app.ebfitapp.utils.downloadGifFromURL
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

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

        showBottomSheet()


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.settings.setOnClickListener {
            showBottomSheet()
        }

    }


    private fun showBottomSheet() = with(binding) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_execution, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        val weightEditText = view.findViewById<TextInputEditText>(R.id.weightText)
        val setsEditText = view.findViewById<TextInputEditText>(R.id.setsText)
        val repsEditText = view.findViewById<TextInputEditText>(R.id.repsText)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            val enteredWeight = weightEditText.text.toString()
            val enteredSets = setsEditText.text.toString()
            val enteredReps = repsEditText.text.toString()

            if (enteredWeight.isEmpty() || enteredSets.isEmpty() || enteredReps.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.please_fill_in_the_empty_fields), Toast.LENGTH_SHORT).show()
            } else {
                selectedWeight.text = enteredWeight
                selectedSets.text = enteredSets
                selectedReps.text = enteredReps
                bottomSheetDialog.dismiss()
            }

        }

        bottomSheetDialog.show()
    }


}
package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentExerciseExecutionBinding
import com.app.ebfitapp.model.BodyPartExercisesItem
import com.app.ebfitapp.model.ExecutionModel
import com.app.ebfitapp.utils.downloadImageFromURL
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

class ExerciseExecutionFragment : Fragment() {

    private lateinit var exerciseItem: BodyPartExercisesItem
    private lateinit var binding: FragmentExerciseExecutionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExerciseExecutionBinding.inflate(layoutInflater)

        arguments.let {

            exerciseItem = it!!.getSerializable("exercise") as BodyPartExercisesItem

            binding.exerciseGifView.downloadImageFromURL(exerciseItem.gifUrl)
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

        var selectedType: String? = null

        val weightTypes = listOf("kg","lbs")
        val typeAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_items, weightTypes)
        val typeDropDown = view.findViewById<AutoCompleteTextView>(R.id.typeDropDown)
        typeDropDown.setAdapter(typeAdapter)

        val weightEditText = view.findViewById<TextInputEditText>(R.id.weightText)
        val setsEditText = view.findViewById<TextInputEditText>(R.id.setsText)
        val repsEditText = view.findViewById<TextInputEditText>(R.id.repsText)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            val enteredWeight = weightEditText.text.toString()
            val enteredSets = setsEditText.text.toString()
            val enteredReps = repsEditText.text.toString()

            if (enteredWeight.isEmpty() || enteredSets.isEmpty() || enteredReps.isEmpty() || selectedType.isNullOrEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.please_fill_in_the_empty_fields), Toast.LENGTH_SHORT).show()
            } else {
                selectedWeight.text = "$enteredWeight $selectedType"
                selectedSets.text = enteredSets
                selectedReps.text = enteredReps
                val executionModel = ExecutionModel(enteredWeight.toDouble(), selectedType!!, enteredSets.toInt(), enteredReps.toInt())
                bottomSheetDialog.dismiss()
            }

        }

        typeDropDown.setOnItemClickListener { adapterView, view, i, l ->
            selectedType = adapterView.getItemAtPosition(i).toString()
        }

        bottomSheetDialog.show()
    }


}
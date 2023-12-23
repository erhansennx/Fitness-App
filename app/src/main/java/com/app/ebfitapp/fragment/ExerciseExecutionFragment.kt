package com.app.ebfitapp.fragment

import android.os.Bundle
import android.os.SystemClock
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
import com.app.ebfitapp.utils.CountDownDialog
import com.app.ebfitapp.utils.downloadGifFromURL
import com.app.ebfitapp.utils.downloadImageFromURL
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

class ExerciseExecutionFragment : Fragment() {

    private var isRunning = false
    private var elapsedSet = 0
    private var elapsedTime: Long = 0

    private var enteredSets = "0"
    private var enteredReps = "0"
    private var enteredWeight = "0"

    private lateinit var countDownDialog: CountDownDialog
    private lateinit var exerciseItem: BodyPartExercisesItem
    private lateinit var binding: FragmentExerciseExecutionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExerciseExecutionBinding.inflate(inflater)

        countDownDialog = CountDownDialog(requireContext())

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

        with(binding) {

            startButton.setOnClickListener {
                if (enteredSets.toInt() != 0 && enteredReps.toInt() != 0) {
                    countDownDialog.showCountDownDialog(5000) {
                        if (it) {
                            exerciseGifView.downloadGifFromURL(exerciseItem.gifUrl)
                            startButton.visibility = View.GONE
                            startedLayout.visibility = View.VISIBLE
                            startChronometer()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Please enter the number and sets of repetitions.", Toast.LENGTH_SHORT).show()
                }
            }

            nextSetButton.setOnClickListener {
                if (elapsedSet != 1) {
                    exerciseGifView.downloadImageFromURL(exerciseItem.gifUrl)
                    chronometer.stop()
                    countDownDialog.showCountDownDialog(15000) {
                        pauseAndPlay.setBackgroundResource(R.drawable.round_6dp_background)
                        pauseAndPlay.setImageResource(R.drawable.baseline_play_arrow_24)
                        chronometer.base = SystemClock.elapsedRealtime()
                        elapsedTime = 0
                        isRunning = false
                        if (elapsedSet > 1) {
                            elapsedSet--
                            elapsedSetText.text = "$elapsedSet x Elapsed Set"
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Finish!", Toast.LENGTH_SHORT).show()
                }

            }

            pauseAndPlay.setOnClickListener {
                val chronometer = binding.chronometer
                if (isRunning) {
                    elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
                    chronometer.stop()
                    isRunning = false
                    exerciseGifView.downloadImageFromURL(exerciseItem.gifUrl)
                    pauseAndPlay.setBackgroundResource(R.drawable.round_6dp_background)
                    pauseAndPlay.setImageResource(R.drawable.baseline_pause_24)
                } else {
                    chronometer.base = SystemClock.elapsedRealtime() - elapsedTime
                    chronometer.start()
                    isRunning = true
                    exerciseGifView.downloadGifFromURL(exerciseItem.gifUrl)
                    pauseAndPlay.setBackgroundResource(R.drawable.round_6dp_background)
                    pauseAndPlay.setImageResource(R.drawable.baseline_play_arrow_24)
                    if (elapsedSet == 1) nextSetButton.text = getString(R.string.finish_workout)
                }
            }

            settings.setOnClickListener {
                showBottomSheet()
            }
        }
    }

    private fun showBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_execution, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        var selectedType: String? = null

        val weightTypes = listOf("kg", "lbs")
        val typeAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_items, weightTypes)
        val typeDropDown = view.findViewById<AutoCompleteTextView>(R.id.typeDropDown)
        typeDropDown.setAdapter(typeAdapter)

        val weightEditText = view.findViewById<TextInputEditText>(R.id.weightText)
        val setsEditText = view.findViewById<TextInputEditText>(R.id.setsText)
        val repsEditText = view.findViewById<TextInputEditText>(R.id.repsText)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            enteredWeight = weightEditText.text.toString()
            enteredSets = setsEditText.text.toString()
            enteredReps = repsEditText.text.toString()

            if (listOf(enteredWeight, enteredSets, enteredReps, selectedType).any { it.isNullOrEmpty() }) {
                Toast.makeText(requireContext(), getString(R.string.please_fill_in_the_empty_fields), Toast.LENGTH_SHORT).show()
            } else {
                binding.selectedWeight.text = "$enteredWeight $selectedType"
                binding.selectedSets.text = "$enteredSets SET"
                binding.selectedReps.text = "$enteredReps REP"
                elapsedSet = enteredSets.toInt()
                binding.elapsedSetText.text = "$elapsedSet x Elapsed Set"
                bottomSheetDialog.dismiss()
            }
        }

        typeDropDown.setOnItemClickListener { adapterView, view, i, l ->
            selectedType = adapterView.getItemAtPosition(i).toString()
        }

        bottomSheetDialog.show()
    }

    private fun startChronometer() {
        val chronometer = binding.chronometer
        if (!isRunning) {
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
            isRunning = true
        } else {
            chronometer.stop()
            isRunning = false
        }
    }
}

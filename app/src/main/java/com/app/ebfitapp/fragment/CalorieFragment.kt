package com.app.ebfitapp.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentCalorieBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class CalorieFragment : Fragment() {
    private var fatPercentage: Double? = null
    private var bodyWeight: Double? = null
    private var activityLevels: String? = null
    private var selectedActivityFactor: Double? = null
    private lateinit var calorieBinding: FragmentCalorieBinding
    private val activityFactors = doubleArrayOf(1.2, 1.375, 1.55, 1.725, 1.9)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)?.visibility = View.GONE
        calorieBinding = FragmentCalorieBinding.inflate(layoutInflater)
        ActivityLevelSettings()
        return calorieBinding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val underlineColor = ContextCompat.getColor(requireContext(), R.color.red)
        val initialColor = ContextCompat.getColor(requireContext(), R.color.light_gray)

        with(calorieBinding) {

            setupEditTextValidation(
                fatPercentageEditText,
                falseFatPercentageText,
                2.0,
                60.0,
                initialColor,
                underlineColor
            )
            setupEditTextValidation(
                bodyWeightEditText,
                falsebodyWeightText,
                30.0,
                250.0,
                initialColor,
                underlineColor
            )
            activityLevelTextView.setOnItemClickListener { adapterView, view, i, l ->
                activityLevels = adapterView.getItemAtPosition(i).toString()

                selectedActivityFactor = activityFactors[i]
            }

            calculateButton.setOnClickListener {
                if (bodyWeightEditText.text.isNotEmpty() && fatPercentageEditText.text.isNotEmpty() && activityLevelTextView.text.isNotEmpty()) {
                    val leanBodyMass = calculateLeanBodyMass(bodyWeight!!, fatPercentage!!)
                    val bmr = calculateBMR(leanBodyMass)
                    val dailyCalories = calculateDailyCalories(bmr, selectedActivityFactor!!)
                    bmrCalculateText.text = "${bmr.toInt()}kcal"
                    calorieCalculateText.text = "${dailyCalories.toInt()}kcal"
                    resultTextsShow()
                }
            }
            goBackImage.setOnClickListener {
                val goBackAction = CalorieFragmentDirections.actionCalorieFragmentToCalculatorFragment()
                Navigation.findNavController(requireView()).navigate(goBackAction)
            }

            infoImage.setOnClickListener {
                showCalorieInfo()
            }
        }
    }
    private fun ActivityLevelSettings() {
        val acitivityLevelItems = listOf(
            getString(R.string.sedantary),
            getString(R.string.light),
            getString(R.string.moderat),
            getString(R.string.veryActive),
            getString(R.string.extraActive)
        )
        val actiivityLevelAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_calorie_items, acitivityLevelItems)
        calorieBinding.activityLevelTextView.setAdapter(actiivityLevelAdapter)
    }
    private fun calculateDailyCalories(bmr: Double, activityFactor: Double): Double {
        return bmr * activityFactor
    }
    private fun calculateBMR(leanBodyMass: Double): Double {
        return 370 + (21.6 * leanBodyMass)
    }
    private fun calculateLeanBodyMass(bodyWeight: Double, fatPercentage: Double): Double {
        val fatFraction = fatPercentage / 100.0
        return bodyWeight * (1 - fatFraction)
    }
    private fun showCalorieInfo()
    {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.calorie_info)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)

        val redColorSpan = ForegroundColorSpan(Color.RED)
        val explanationId = dialog.findViewById<TextView>(R.id.explanationId)
        val spannableString = SpannableString("Your BMR and calorie needs are calculated with the fat percentage and body weight you enter. Katch- McArdle Formula is used for this")
        spannableString.setSpan(redColorSpan, 93, 115, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        explanationId.text = spannableString
        dialog.show()
        val dialogCancelBtn = dialog.findViewById<Button>(R.id.gotItCalorieButton)
        dialogCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
    }
    private fun resultTextsShow()
    {
        calorieBinding.bmrCalculateText.visibility = View.VISIBLE
        calorieBinding.calorieCalculateText.visibility = View.VISIBLE
        calorieBinding.yourBmrText.visibility = View.VISIBLE
        calorieBinding.yourCalorieText.visibility = View.VISIBLE
    }

    private fun setupEditTextValidation(
        editText: EditText,
        falseTextView: TextView,
        minValue: Double,
        maxValue: Double,
        initialColor: Int,
        underlineColor: Int
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val inputText = editable.toString()

                if (inputText.isNullOrEmpty()) {
                    falseTextView.visibility = View.INVISIBLE
                    editText.backgroundTintList = ColorStateList.valueOf(initialColor)
                    return
                }

                try {
                    val formattedInput = inputText.replace(',', '.')
                    val enteredValue = formattedInput.toDouble()

                    if (enteredValue in minValue..maxValue) {
                        if (editText == calorieBinding.fatPercentageEditText) {
                            fatPercentage = enteredValue
                        } else if (editText == calorieBinding.bodyWeightEditText) {
                            bodyWeight = enteredValue
                        }

                        falseTextView.visibility = View.INVISIBLE
                        editText.backgroundTintList = ColorStateList.valueOf(initialColor)
                    } else {
                        falseTextView.visibility = View.VISIBLE
                        editText.backgroundTintList = ColorStateList.valueOf(underlineColor)
                    }
                } catch (e: NumberFormatException) {
                    falseTextView.visibility = View.VISIBLE
                    editText.backgroundTintList = ColorStateList.valueOf(underlineColor)
                }
            }
        })
    }
}
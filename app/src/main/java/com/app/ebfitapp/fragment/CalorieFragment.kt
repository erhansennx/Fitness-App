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
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentCalorieBinding


class CalorieFragment : Fragment() {
    private var fatPercentage: Double? = null
    private var bodyWeight: Double? = null
    private var activityLevels: String? = null
    private var selectedActivityFactor: Double? = null
    private lateinit var calorieBinding: FragmentCalorieBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calorieBinding = FragmentCalorieBinding.inflate(layoutInflater)
        ActivityLevelSettings()
        return calorieBinding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val underlineColor = ContextCompat.getColor(requireContext(), R.color.red)
        val initialColor = ContextCompat.getColor(requireContext(), R.color.light_gray)
        val activityFactors = doubleArrayOf(1.2, 1.375, 1.55, 1.725, 1.9)
        hideAllText()
        with(calorieBinding) {
            fatPercentageEditText.onFocusChangeListener =
                View.OnFocusChangeListener { view, hasFocus ->
                    if (!hasFocus) {
                        hideKeyboard(view)
                    }
                }
            fatPercentageEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(editable: Editable?) {
                    val inputText = editable.toString()

                    if (inputText.isNullOrEmpty()) {
                        falseFatPercentageText.visibility = View.INVISIBLE
                        fatPercentageEditText.backgroundTintList =
                            ColorStateList.valueOf(initialColor)
                        return
                    } else {
                        try {
                            val formattedInput = inputText.replace(',', '.')
                            val enteredValueFatPercentage = formattedInput.toDouble()
                            if (enteredValueFatPercentage in 2.0..60.0) {
                                fatPercentage = enteredValueFatPercentage
                                falseFatPercentageText.visibility = View.INVISIBLE
                                fatPercentageEditText.backgroundTintList =
                                    ColorStateList.valueOf(initialColor)
                            } else {
                                falseFatPercentageText.visibility = View.VISIBLE
                                fatPercentageEditText.backgroundTintList =
                                    ColorStateList.valueOf(underlineColor)
                            }
                        } catch (e: NumberFormatException) {
                            falseFatPercentageText.visibility = View.VISIBLE
                            fatPercentageEditText.backgroundTintList =
                                ColorStateList.valueOf(underlineColor)
                        }
                    }
                }
            })

            bodyWeightEditText.onFocusChangeListener =
                View.OnFocusChangeListener { view, hasFocus ->
                    if (!hasFocus) {
                        hideKeyboard(view)
                    }
                }
            bodyWeightEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    val inputText = editable.toString()

                    if (inputText.isNullOrEmpty()) {
                        falsebodyWeightText.visibility = View.INVISIBLE
                        bodyWeightEditText.backgroundTintList = ColorStateList.valueOf(initialColor)
                        return
                    }
                    try {
                        val formattedInput = inputText.replace(',', '.')
                        val enteredValuebodyWeight = formattedInput.toDouble()
                        if (enteredValuebodyWeight in 30.0..250.0) {
                            bodyWeight = enteredValuebodyWeight
                            falsebodyWeightText.visibility = View.INVISIBLE
                            bodyWeightEditText.backgroundTintList =
                                ColorStateList.valueOf(initialColor)
                        } else {
                            falsebodyWeightText.visibility = View.VISIBLE
                            bodyWeightEditText.backgroundTintList =
                                ColorStateList.valueOf(underlineColor)
                        }
                    } catch (e: NumberFormatException) {
                        falsebodyWeightText.visibility = View.VISIBLE
                        bodyWeightEditText.backgroundTintList =
                            ColorStateList.valueOf(underlineColor)
                    }
                }
            })

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
    private fun hideKeyboard(view: View) {
        val imm = ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
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
    private fun hideAllText()
    {
        calorieBinding.falseFatPercentageText.visibility = View.GONE
        calorieBinding.falsebodyWeightText.visibility = View.GONE
        calorieBinding.bmrCalculateText.visibility = View.GONE
        calorieBinding.calorieCalculateText.visibility = View.GONE
        calorieBinding.yourCalorieText.visibility = View.GONE
        calorieBinding.yourBmrText.visibility = View.GONE
    }
}
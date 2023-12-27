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
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentBodyFatPercentageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.DecimalFormat
import kotlin.math.log
import kotlin.math.pow

class BodyFatPercentageFragment : Fragment() {
    private var personHeight : Double? = null
    private var personHip : Double? = null
    private var gender : Boolean? = null
    private var personNeckSize : Double? = null
    private var personWaistSize : Double? = null
    private var resultBodyFatPercentage : Double? = null
    private val decimalFormat = DecimalFormat("#.##")

    //Düzenleme gerekli
    private lateinit var bodyFatPercentageBinding: FragmentBodyFatPercentageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)?.visibility = View.GONE
        bodyFatPercentageBinding = FragmentBodyFatPercentageBinding.inflate(layoutInflater)
        return bodyFatPercentageBinding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val underlineColor = ContextCompat.getColor(requireContext(), R.color.red)
        val initialColor = ContextCompat.getColor(requireContext(), R.color.light_gray)
        super.onViewCreated(view, savedInstanceState)
        with(bodyFatPercentageBinding)
        {
            goBackImage.setOnClickListener {
                val goBackAction = BodyFatPercentageFragmentDirections.actionBodyFatPercentageFragmentToCalculatorFragment()
                Navigation.findNavController(requireView()).navigate(goBackAction)
            }
            infoImage.setOnClickListener {
                showFatPercentageInfo()
            }
            genderSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    //For woman
                    this@BodyFatPercentageFragment.gender = false
                    personHipText.visibility = View.VISIBLE
                    genderSwitch.thumbTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.pink)
                    resultLinearLayuot.visibility = View.INVISIBLE
                    fatPercentageTextLayuot.visibility = View.INVISIBLE
                } else {
                    //For man
                    this@BodyFatPercentageFragment.gender = true
                    personHip = 0.0
                    personHipText.visibility = View.INVISIBLE
                    genderSwitch.thumbTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.blue)
                    resultLinearLayuot.visibility = View.INVISIBLE
                    fatPercentageTextLayuot.visibility = View.INVISIBLE
                }
            }

            setupEditTextValidation(
                personHeightText,
                falseHeightText,
                100.0,
                250.0,
                initialColor,
                underlineColor
            )
            setupEditTextValidation(
                personHipText,
                falseHipText,
                60.0,
                120.0,
                initialColor,
                underlineColor
            )
            setupEditTextValidation(
                personNeckSizeText,
                falseNeckSizeText,
                30.0,
                50.0,
                initialColor,
                underlineColor
            )
            setupEditTextValidation(
                personWaistSizeText,
                falseWaistSizeText,
                50.0,
                100.0,
                initialColor,
                underlineColor
            )

            calculateButton.setOnClickListener {
                if(gender != null && personHeightText.text.isNotEmpty() && personNeckSizeText.text.isNotEmpty() && personWaistSizeText.text.isNotEmpty()){

                    if(!gender!!)
                    {
                        if(personHipText.text.isNotEmpty())
                        {//For woman
                            resultBodyFatPercentage = calculateBodyFatPercentage(personHip!!,personHeight!!,personWaistSize!!,personNeckSize!!)
                            handleBodyFatResult(resultBodyFatPercentage!!,gender!!)
                            resultText.text = "${decimalFormat.format(resultBodyFatPercentage)}%"
                        }else{
                            Toast.makeText(requireContext(),"Please fill the empty field",Toast.LENGTH_SHORT).show()
                        }

                    }else{
                        //For man
                        personHip = 0.0
                        resultBodyFatPercentage = calculateBodyFatPercentage(personHip!!,personHeight!!,personWaistSize!!,personNeckSize!!)
                        handleBodyFatResult(resultBodyFatPercentage!!,gender!!)
                        resultText.text = "${decimalFormat.format(resultBodyFatPercentage)}%"
                    }
                }
                else{
                    Toast.makeText(requireContext(),"Please fill the empty field",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun calculateBodyFatPercentage(hip : Double,height: Double, waist: Double,neck : Double): Double {

        return if(gender!!)
        //For man
            495.0 / (1.0324 - 0.19077 * log(waist - neck) + 0.15456 * log(height)) - 450.0
        else        //For woman
            495.0 / (1.29579 - 0.35004 * (log(waist + hip - neck)) + 0.22100 * (log(height))) - 450.0
    }
    private fun log(value: Double): Double {
        return kotlin.math.log10(value)
    }
    private fun showPercentageTextVisibility(lowVisibility: Int, highVisibility: Int, healthyVisibility: Int) {
        with(bodyFatPercentageBinding)
        {
            lowPercentageText.visibility = lowVisibility
            highPercentageText.visibility = highVisibility
            healthyPercentageText.visibility = healthyVisibility
            resultLinearLayuot.visibility = View.VISIBLE
            fatPercentageTextLayuot.visibility = View.VISIBLE
        }


    }
    private fun handleBodyFatResult(bodyFatPercentage: Double, gender: Boolean) {
        println("Body Fat Percentage: $bodyFatPercentage, Gender: $gender")
        when (bodyFatPercentage) {
            in 0.0..14.0 -> showPercentageTextVisibility(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
            in 14.0..(if (gender) 24.0 else 31.0) -> showPercentageTextVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
            else -> showPercentageTextVisibility(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
        }//Çalışsana bacını sikiyim
            println("Visibility Updated")
    }
    private fun showFatPercentageInfo()
    {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.fat_percentage_info)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)

        val redColorSpan = ForegroundColorSpan(Color.RED)
        val explanationId = dialog.findViewById<TextView>(R.id.explanationId)
        val spannableString = SpannableString("Your body fat percentage can be estimated using measurements such as waist circumference, body weight, and height. The US Navy Body Fat Formula is employed for this calculation ")
        spannableString.setSpan(redColorSpan, 115, 143, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        explanationId.text = spannableString
        dialog.show()
        val dialogCancelBtn = dialog.findViewById<Button>(R.id.gotItCalorieButton)
        dialogCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
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
                        when (editText) {

                            bodyFatPercentageBinding.personHeightText -> {
                                personHeight = enteredValue
                            }
                            bodyFatPercentageBinding.personHipText -> {
                                personHip = enteredValue
                            }
                            bodyFatPercentageBinding.personNeckSizeText -> {
                                personNeckSize = enteredValue
                            }
                            bodyFatPercentageBinding.personWaistSizeText -> {
                                personWaistSize = enteredValue
                            }
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
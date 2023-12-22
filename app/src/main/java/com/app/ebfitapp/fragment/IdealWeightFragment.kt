package com.app.ebfitapp.fragment

import android.app.Dialog
import android.content.Context
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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentIdealWeightBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class IdealWeightFragment : Fragment() {
    private lateinit var idealWeightBinding: FragmentIdealWeightBinding
    private var personHeight : Double? = null
    private var personWeight : Double? = null
    private var gender : Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)?.visibility = View.GONE
        idealWeightBinding = FragmentIdealWeightBinding.inflate(layoutInflater)
        return idealWeightBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val underlineColor = ContextCompat.getColor(requireContext(), R.color.red)
        val initialColor = ContextCompat.getColor(requireContext(), R.color.light_gray)
        with(idealWeightBinding)
        {
            goBackImage.setOnClickListener {
                val goBackAction = IdealWeightFragmentDirections.actionIdealWeightFragmentToCalculatorFragment()
                Navigation.findNavController(requireView()).navigate(goBackAction)
            }
            infoImage.setOnClickListener {
                showIdealWeightInfo()
            }
            genderSwitch.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    //For woman
                    this@IdealWeightFragment.gender = false
                    genderSwitch.thumbTintList = ContextCompat.getColorStateList(requireContext(), R.color.pink)
                    femaleImageVisible()
                }
                else
                {
                    //For man
                    this@IdealWeightFragment.gender = true
                    genderSwitch.thumbTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
                    maleImageVisible()
                }
            }

            setupEditTextValidation(
                personHeightText,
                falseHeightText,
                30.0,
                250.0,
                initialColor,
                underlineColor
            )
            setupEditTextValidation(
                personWeightText,
                falseWeightText,
                30.0,
                250.0,
                initialColor,
                underlineColor
            )

            calculateButton.setOnClickListener {
                    try {
                        if (gender != null) {
                            calculateIdealWeight(personHeight!!, gender!!)
                            val bmi = calculateBMI(personWeight!!, personHeight!!)
                            displayBMICategory(bmi)
                        }
                        else{
                            Toast.makeText(requireContext(),"Please choose your gender",Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: NumberFormatException) {
                        println("Invalid number format")
                    }
            }

        }
    }
    private fun calculateIdealWeight(height : Double,gender : Boolean) : Double
    {
        val idealWeightForMale = (height - 100) * 0.9
        val idealWeightForFemale = (height - 100) * 0.85

        return if(gender) {
            idealWeightForMale
        } else {
            idealWeightForFemale
        }
    }
    private fun calculateBMI(weight: Double, heightCm: Double): Double {
        val heightM = heightCm / 100.0
        return weight / (heightM * heightM)
    }
    private fun femaleImageVisible()
    {
        idealWeightBinding.femaleImage.visibility = View.VISIBLE
        idealWeightBinding.mixImage.visibility = View.GONE
        idealWeightBinding.maleImage.visibility = View.GONE
    }
    private fun maleImageVisible()
    {
        idealWeightBinding.femaleImage.visibility = View.GONE
        idealWeightBinding.mixImage.visibility = View.GONE
        idealWeightBinding.maleImage.visibility = View.VISIBLE
    }
    private fun showIdealText()
    {
        idealWeightBinding.aboveBelowLayout.visibility = View.INVISIBLE
        idealWeightBinding.idealWeightTextLayuot.visibility = View.VISIBLE
        idealWeightBinding.obeseTextLayout.visibility = View.INVISIBLE

    }
    private fun showHighLowText()
    {
        idealWeightBinding.aboveBelowLayout.visibility = View.VISIBLE
        idealWeightBinding.obeseTextLayout.visibility = View.INVISIBLE
        idealWeightBinding.idealWeightTextLayuot.visibility = View.INVISIBLE
    }
    private fun showObeseText()
    {
        idealWeightBinding.obeseTextLayout.visibility = View.VISIBLE
        idealWeightBinding.aboveBelowLayout.visibility = View.INVISIBLE
        idealWeightBinding.idealWeightTextLayuot.visibility = View.INVISIBLE

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
                        if (editText == idealWeightBinding.personHeightText) {
                            personHeight = enteredValue
                        } else if (editText == idealWeightBinding.personWeightText) {
                            personWeight = enteredValue
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
    private fun displayBMICategory(bmi: Double) {
        when {
            bmi < 18.5 -> {
                // Below
                idealWeightBinding.aboveBelowText.text = " BELOW"
                showHighLowText()
            }

            bmi in 18.5 .. 24.9 -> {
                // Ideal weight
                showIdealText()
            }

            bmi in 25.0 .. 29.9 -> {
                // Above
                idealWeightBinding.aboveBelowText.text = " ABOVE"
                showHighLowText()
            }

            else -> {
                // Obese
                showObeseText()
            }
        }
    }

    private fun showIdealWeightInfo()
    {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.ideal_weight_info)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)

        val redColorSpan = ForegroundColorSpan(Color.RED)
        val explanationId = dialog.findViewById<TextView>(R.id.explanationId)
        val spannableString = SpannableString("Your ideal weight can be determined by considering your body weight and fat percentage. The Hamwi Formula is employed in this calculation. ")
        spannableString.setSpan(redColorSpan, 88, 105, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        explanationId.text = spannableString
        dialog.show()
        val dialogCancelBtn = dialog.findViewById<Button>(R.id.gotItCalorieButton)
        dialogCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
    }
}

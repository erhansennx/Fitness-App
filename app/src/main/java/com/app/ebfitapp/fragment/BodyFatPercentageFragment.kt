package com.app.ebfitapp.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentBodyFatPercentageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.DecimalFormat
import kotlin.math.pow

class BodyFatPercentageFragment : Fragment() {
    private var personHeight : Double? = null
    private var personWeight : Double? = null
    private var gender : Boolean? = null
    private var personNeckSize : Double? = null
    private var personWaistSize : Double? = null
    private var personAge : Int? = null
    private var returnForMale : Double? = null
    private var returnForFemale : Double? = null

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
                    genderSwitch.thumbTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.pink)
                } else {
                    //For man
                    this@BodyFatPercentageFragment.gender = true
                    genderSwitch.thumbTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.blue)
                }
            }
            calculateButton.setOnClickListener {

                personHeight= personHeightText.text.toString().toDouble()
                personWeight= personWeightText.text.toString().toDouble()
                personNeckSize = personNeckSizeText.text.toString().toDouble()
                personWaistSize = personWaistSizeText.text.toString().toDouble()

                if (gender == null || personHeight == null || personWeight == null || personNeckSize == null || personWaistSize == null) {
                    Toast.makeText(requireContext(), "Please fill in all the values", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else
                {
                    val decimalFormat = DecimalFormat("#.##")
                    if(gender!!)
                    {
                        returnForMale = calculateBodyFatPercentage(personWeight!!,personHeight!!,personWaistSize!!)
                        resultText.text = " ${decimalFormat.format(returnForMale)}%"
                    }
                    else{
                        returnForFemale = calculateBodyFatPercentage(personWeight!!,personHeight!!,personWaistSize!!)
                        resultText.text = " ${decimalFormat.format(returnForFemale)}%"
                    }
                    showResultText()
                }
                when {
                    returnForMale != null -> handleBodyFatResult(returnForMale!!,true)
                    returnForFemale != null -> handleBodyFatResult(returnForFemale!!,false)
                }
                }
            }
        }

    private fun calculateBodyFatPercentage(weight: Double, height: Double, waist: Double): Double {

        return if(gender!!)
            0.29288 * weight - 0.0005 * weight.pow(2.0) + 0.00000419 * weight.pow(3.0) - 0.0000722 * waist + 0.000073 * height - 10.4
        else 0.29669 * weight - 0.00043 * weight.pow(2.0) + 0.00000353 * weight.pow(3.0) - 0.0000779 * waist + 0.0000779 * height - 9.7
    }

    private fun showPercentageTextVisibility(lowVisibility: Int, highVisibility: Int, healthyVisibility: Int) {
        bodyFatPercentageBinding.lowPercentageText.visibility = lowVisibility
        bodyFatPercentageBinding.highPercentageText.visibility = highVisibility
        bodyFatPercentageBinding.healthyPercentageText.visibility = healthyVisibility
    }
    private fun showResultText()
    {
        bodyFatPercentageBinding.resultText.visibility = View.VISIBLE
        bodyFatPercentageBinding.resultLinearLayuot.visibility = View.VISIBLE
        bodyFatPercentageBinding.fatPercentageTextLayuot.visibility = View.VISIBLE

    }
    private fun handleBodyFatResult(bodyFatPercentage: Double, gender: Boolean) {
        when (bodyFatPercentage) {
            in 0.0..10.0 -> showPercentageTextVisibility(View.VISIBLE, View.INVISIBLE, View.INVISIBLE)
            in 11.0..(if (gender) 25.0 else 30.0) -> showPercentageTextVisibility(View.INVISIBLE, View.INVISIBLE, View.VISIBLE)
            else -> showPercentageTextVisibility(View.INVISIBLE, View.VISIBLE, View.INVISIBLE)
        }
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
}



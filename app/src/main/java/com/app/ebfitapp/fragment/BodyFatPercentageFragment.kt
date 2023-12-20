package com.app.ebfitapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentBodyFatPercentageBinding
import java.text.DecimalFormat

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
                Toast.makeText(requireContext(),"Info image clicked",Toast.LENGTH_SHORT).show()
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
                    println("${gender} || ${personWeight} || ${personHeight} || ${personNeckSize} || ${personWaistSize}  ")
                    return@setOnClickListener
                }
                else
                {
                    val decimalFormat = DecimalFormat("#.##")
                    if(gender!!)
                    {
                        returnForMale = calculateBodyFatPercentageMale(personWeight!!,personHeight!!,personWaistSize!!)
                        resultText.text = " ${decimalFormat.format(returnForMale)}%"
                    }
                    else{
                        returnForFemale = calculateBodyFatPercentageFemale(personWeight!!,personHeight!!,personWaistSize!!)
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

    private fun calculateBodyFatPercentageMale(weight: Double, height: Double, waist: Double): Double {
        return 0.29288 * weight - 0.0005 * Math.pow(weight, 2.0) + 0.00000419 * Math.pow(weight, 3.0)- 0.0000722 * waist + 0.000073 * height - 10.4
    }

    private fun calculateBodyFatPercentageFemale(weight: Double, height: Double, waist: Double): Double {
        return 0.29669 * weight - 0.00043 * Math.pow(weight, 2.0) + 0.00000353 * Math.pow(weight, 3.0)- 0.0000779 * waist + 0.0000779 * height - 9.7
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
}



package com.app.ebfitapp.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentIdealWeightBinding


class IdealWeightFragment : Fragment() {
    private lateinit var idealWeightBinding: FragmentIdealWeightBinding
    private var personHeight : Double? = null
    private var personWeight : Double? = null
    private var gender : Boolean? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        idealWeightBinding = FragmentIdealWeightBinding.inflate(layoutInflater)
        return idealWeightBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(idealWeightBinding)
        {
            goBackImage.setOnClickListener {
                val goBackAction = IdealWeightFragmentDirections.actionIdealWeightFragmentToCalculatorFragment()
                Navigation.findNavController(requireView()).navigate(goBackAction)
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
            calculateButton.setOnClickListener {
                if (personWeightText.text.isNotEmpty() && personHeightText.text.isNotEmpty()) {
                    try {
                        personHeight = personHeightText.text.toString().toDouble()
                        personWeight = personWeightText.text.toString().toDouble()

                        println(personWeight)
                        println(personHeight)

                        if (gender == null) {
                            throw IllegalStateException("Please select your gender.")
                        }
                        calculateIdealWeight(personHeight!!, gender!!)

                        val bmi = calculateBMI(personWeight!!, personHeight!!)

                        when {
                            bmi < 18.5 -> {
                                //Below
                                aboveBelowText.text = " BELOW"
                                showHighLowText()
                            }

                            bmi in 18.5 .. 24.9 -> {
                                //Ideal weight
                                showIdealText()
                            }

                            bmi in 25.0 .. 29.9 -> {
                                //Above
                                aboveBelowText.text = " ABOVE"
                                showHighLowText()
                            }

                            else -> {
                                //Obese
                                showObeseText()
                            }
                        }
                    } catch (e: NumberFormatException) {
                        println("Invalid number format")
                    }
                    catch (e: IllegalStateException) {
                        showToast(requireContext(),e.message!!)
                    }
                } else {
                    println("Height or weight is empty")
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

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

}

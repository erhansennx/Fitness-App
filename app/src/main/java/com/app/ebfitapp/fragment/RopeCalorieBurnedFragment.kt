package com.app.ebfitapp.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentRopeCalorieBurnedBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.math.ceil
import kotlin.math.min


class RopeCalorieBurnedFragment : Fragment() {

    private lateinit var bindingRope : FragmentRopeCalorieBurnedBinding
    private var weight : Double? = null
    private var hours : Int? = null
    private var minutes : Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)?.visibility = View.GONE
        bindingRope = FragmentRopeCalorieBurnedBinding.inflate(layoutInflater)
        return bindingRope.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val underlineColor = ContextCompat.getColor(requireContext(), R.color.red)
        val initialColor = ContextCompat.getColor(requireContext(), R.color.light_gray)

        with(bindingRope){

            goBackImage.setOnClickListener {
                val goBackAction = RopeCalorieBurnedFragmentDirections.actionRopeCalorieBurnedFragmentToCalculatorFragment()
                Navigation.findNavController(requireView()).navigate(goBackAction)
            }
            infoImage.setOnClickListener {

            }


            val textWatcher = object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    weightEditText.backgroundTintList = ColorStateList.valueOf(initialColor)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                  if(weightEditText.text.isNotEmpty() && minutesEditText.text.isNotEmpty() && hoursEditText.text.isNotEmpty()){
                      try {
                          weight = weightEditText.text.toString().toDouble()
                          minutes = minutesEditText.text.toString().toInt()
                          hours = hoursEditText.text.toString().toInt()

                          if(weight!! < 500 ){
                              falseWeightText.visibility = View.INVISIBLE
                              if(hoursEditText.text.isNotEmpty() && minutesEditText.text.isNotEmpty())
                              {
                                  showResult()
                              }else{
                                  textNaN()
                              }
                          }else{
                              //given weight is bigger than 500
                              falseWeightText.visibility = View.VISIBLE
                              weightEditText.backgroundTintList = ColorStateList.valueOf(underlineColor)
                          }
                      }catch (e: Exception){
                          Toast.makeText(requireContext(),"$e",Toast.LENGTH_SHORT).show()
                      }
                  }
                }

            }
            weightEditText.addTextChangedListener(textWatcher)
            minutesEditText.addTextChangedListener(textWatcher)
            hoursEditText.addTextChangedListener(textWatcher)

        }
    }
    private fun showResult()
    {
        with(bindingRope) {
            slowPaceBurned.text = ceil(burnedCalculasion() * 8.8).toInt().toString()
            moderatePaceBurned.text = ceil(burnedCalculasion() * 11.8).toInt().toString()
            generalBurned.text = ceil(burnedCalculasion() * 12.3).toInt().toString()
            fastPaceBurned.text = ceil(burnedCalculasion() * 12.3).toInt().toString()
        }
    }

    private fun textNaN()
    {
        with(bindingRope){
            slowPaceBurned.text = "NaN"
            moderatePaceBurned.text = "NaN"
            generalBurned.text = "NaN"
            fastPaceBurned.text = "NaN"
        }
    }

    private fun burnedCalculasion() : Double{
        //Calories Burned from jumping rope at a slow pace (per minute) = (8.8 x 81.65 x 3.5) ÷ 200 = 12.57
        return weight!! * 3.5 / 200 * timeCalculasion()
    }
    private fun timeCalculasion() : Int {return hours!! * 60 + minutes!!}



}
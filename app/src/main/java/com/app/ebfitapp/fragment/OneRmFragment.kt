package com.app.ebfitapp.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ebfitapp.R
import com.app.ebfitapp.adapter.CalendarToDoAdapter
import com.app.ebfitapp.adapter.RepAdapter
import com.app.ebfitapp.databinding.FragmentCalculatorBinding
import com.app.ebfitapp.databinding.FragmentOneRmBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class OneRmFragment : Fragment() {
    private lateinit var fragmentOneRmFragmentBinding: FragmentOneRmBinding
    private lateinit var repAdapter : RepAdapter
    private var weight : Double? = null
    private var reps : Int? = null
    private val repList: List<Int> = (1..10).toList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)?.visibility = View.GONE
        fragmentOneRmFragmentBinding = FragmentOneRmBinding.inflate(layoutInflater)
        return fragmentOneRmFragmentBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val underlineColor = ContextCompat.getColor(requireContext(), R.color.red)
        val initialColor = ContextCompat.getColor(requireContext(), R.color.light_gray)

        with(fragmentOneRmFragmentBinding) {

            goBackImage.setOnClickListener {
                val goBackAction = OneRmFragmentDirections.actionOneRmFragmentToCalculatorFragment()
                Navigation.findNavController(requireView()).navigate(goBackAction)
            }

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (weightText.text.isNotEmpty() && repsText.text.isNotEmpty()) {
                         weight = weightText.text.toString().toDouble()
                         reps = repsText.text.toString().toInt()

                        if(weight !=null && reps != null)
                        {
                            if (weight!! < 500) {
                                falseWeightText.visibility = View.INVISIBLE
                                weightText.backgroundTintList = ColorStateList.valueOf(initialColor)
                                if (reps!! in 1..10) {
                                    falseRepText.visibility = View.INVISIBLE
                                    repsText.backgroundTintList = ColorStateList.valueOf(initialColor)
                                    showRecycler()
                                } else {
                                    falseRepText.visibility = View.VISIBLE
                                    fragmentOneRmFragmentBinding.repResultRecycler.visibility = View.INVISIBLE
                                    repsText.backgroundTintList = ColorStateList.valueOf(underlineColor)
                                }
                            } else {
                                falseWeightText.visibility = View.VISIBLE
                                fragmentOneRmFragmentBinding.repResultRecycler.visibility = View.INVISIBLE
                                weightText.backgroundTintList = ColorStateList.valueOf(underlineColor)
                            }
                        }
                        else
                        {
                            Toast.makeText(requireContext(),"Exception",Toast.LENGTH_SHORT)
                        }
                    }
                    else
                    {
                        fragmentOneRmFragmentBinding.repResultRecycler.visibility = View.INVISIBLE
                    }
                }
            }
            weightText.addTextChangedListener(textWatcher)
            repsText.addTextChangedListener(textWatcher)
        }
    }

    private fun showRecycler()
    {
        repAdapter = RepAdapter(repList, weight!!,reps!!)
        fragmentOneRmFragmentBinding.repResultRecycler.layoutManager = LinearLayoutManager(this@OneRmFragment.requireContext())
        fragmentOneRmFragmentBinding.repResultRecycler.adapter = repAdapter
        repAdapter.notifyDataSetChanged()
        fragmentOneRmFragmentBinding.repResultRecycler.visibility = View.VISIBLE
    }

}


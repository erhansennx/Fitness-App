package com.app.ebfitapp.fragment

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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
            infoImage.setOnClickListener {
                showOneRMInfo()
            }

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    weightText.backgroundTintList = ColorStateList.valueOf(initialColor)
                    repsText.backgroundTintList = ColorStateList.valueOf(initialColor)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (weightText.text.isNotEmpty() && repsText.text.isNotEmpty()) {

                        try {
                            weight = weightText.text.toString().toDouble()
                            reps = repsText.text.toString().toInt()
                            if (weight!! < 500) {
                                falseWeightText.visibility = View.INVISIBLE
                                if (reps!! in 1..10) {
                                    falseRepText.visibility = View.INVISIBLE
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
                        catch(e: NumberFormatException)
                        {
                            Toast.makeText(requireContext(),"$e",Toast.LENGTH_SHORT)
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

    private fun showOneRMInfo()
    {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.one_rm_info)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)

        val redColorSpan = ForegroundColorSpan(Color.RED)
        val explanationId = dialog.findViewById<TextView>(R.id.explanationId)
        val spannableString = SpannableString("Your 1 Repetition Maximum (1RM) is calculated based on the weight and repetition count you enter. The Wathen Formula is employed for this calculation")
        spannableString.setSpan(redColorSpan, 97, 116, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        explanationId.text = spannableString
        dialog.show()
        val dialogCancelBtn = dialog.findViewById<Button>(R.id.gotItCalorieButton)
        dialogCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
    }

}


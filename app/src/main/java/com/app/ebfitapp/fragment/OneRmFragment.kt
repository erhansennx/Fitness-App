package com.app.ebfitapp.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ebfitapp.R
import com.app.ebfitapp.adapter.CalendarToDoAdapter
import com.app.ebfitapp.adapter.RepAdapter
import com.app.ebfitapp.databinding.FragmentCalculatorBinding
import com.app.ebfitapp.databinding.FragmentOneRmBinding


class OneRmFragment : Fragment() {
    private lateinit var fragmentOneRmFragmentBinding: FragmentOneRmBinding
    private lateinit var repAdapter : RepAdapter
    private val repList: List<Int> = (1..10).toList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentOneRmFragmentBinding = FragmentOneRmBinding.inflate(layoutInflater)
        return fragmentOneRmFragmentBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                        val weight = weightText.text.toString().toDouble()
                        val reps = repsText.text.toString().toInt()

                    //Rep tekrar sayısı kontrol edilecek

                        repAdapter = RepAdapter(repList, weight,reps)
                        repResultRecycler.layoutManager = LinearLayoutManager(this@OneRmFragment.requireContext())
                        repResultRecycler.adapter = repAdapter
                        repAdapter.notifyDataSetChanged()
                        repResultRecycler.visibility = View.VISIBLE
                    }
                }
            }
            weightText.addTextChangedListener(textWatcher)
            repsText.addTextChangedListener(textWatcher)
        }
    }

}
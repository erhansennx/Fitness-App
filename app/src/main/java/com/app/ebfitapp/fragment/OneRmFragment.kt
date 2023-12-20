package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentCalculatorBinding
import com.app.ebfitapp.databinding.FragmentOneRmBinding


class OneRmFragment : Fragment() {
    private lateinit var fragmentOneRmFragmentBinding: FragmentOneRmBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentOneRmFragmentBinding = FragmentOneRmBinding.inflate(layoutInflater)
        return fragmentOneRmFragmentBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(fragmentOneRmFragmentBinding)
        {
            goBackImage.setOnClickListener {
                val goBackAction = OneRmFragmentDirections.actionOneRmFragmentToCalculatorFragment()
                Navigation.findNavController(requireView()).navigate(goBackAction)
            }
        }
    }

}
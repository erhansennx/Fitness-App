package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentRopeCalorieBurnedBinding


class RopeCalorieBurnedFragment : Fragment() {

    private lateinit var bindingRope : FragmentRopeCalorieBurnedBinding
    private var weight : Double? = null
    private var hours : Int? = null
    private var minutes : Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingRope = FragmentRopeCalorieBurnedBinding.inflate(layoutInflater)
        return bindingRope.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(bindingRope){

            goBackImage.setOnClickListener {
                val goBackAction = RopeCalorieBurnedFragmentDirections.actionRopeCalorieBurnedFragmentToCalculatorFragment()
                Navigation.findNavController(requireView()).navigate(goBackAction)
            }
            infoImage.setOnClickListener {
                showRopeBurnedInfo()
            }
        }
    }



    private fun showRopeBurnedInfo(){

    }

}
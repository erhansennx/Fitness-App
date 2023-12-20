package com.app.ebfitapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.databinding.ItemRepBinding
import kotlin.math.exp

class RepAdapter(private val repList: List<Int>, private val weight: Double,private val reps: Int) : RecyclerView.Adapter<RepAdapter.RepViewHolder>() {

    class RepViewHolder(val bindingRep: ItemRepBinding) : RecyclerView.ViewHolder(bindingRep.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepViewHolder {
        val itemRepBinding =
            ItemRepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepViewHolder(itemRepBinding)
    }

    override fun getItemCount(): Int {
        return repList.size
    }

    override fun onBindViewHolder(holder: RepViewHolder, position: Int) = with(holder.bindingRep) {
        val percentage = when (position) {
            0 -> 100.0
            1 -> 94.0
            2 -> 91.0
            3 -> 88.0
            4 -> 86.0
            5 -> 83.0
            6 -> 81.0
            7 -> 79.0
            8 -> 77.0
            9 -> 75.0
            else -> 0.0
        }

        val oneRm = calculateOneRepMax(weight, reps)
        val percentageOneRm = calculatePercentageOfOneRepMax(oneRm, percentage)

        recyclerRmText.text = "${position+1}RM"
        if(reps == position+1)
        {
            recyclerRmText.setTextColor(Color.RED)
            recyclerWeightText.text = "$weight kg"
        }else
        {
            recyclerRmText.setTextColor(Color.WHITE)
            recyclerWeightText.text = String.format("%.2f kg", percentageOneRm)
        }

    }

    private fun calculateOneRepMax(weight: Double, repCount: Int): Double {
        return 100.0 * weight / (48.8 + 53.8 * exp(-0.075 * repCount))
    }

    private fun calculatePercentageOfOneRepMax(oneRepMax: Double, percentage: Double): Double {
        return oneRepMax * (percentage / 100.0)
    }
}
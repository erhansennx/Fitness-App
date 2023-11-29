package com.app.ebfitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.ItemMuscleDetailsBinding
import com.app.ebfitapp.fragment.MusclesDetailFragmentDirections
import com.app.ebfitapp.model.BodyPartExercises

class ExercisesAdapter(private val bodyPartExercises: BodyPartExercises) : RecyclerView.Adapter<ExercisesAdapter.ItemHolder>() {

    inner class ItemHolder(val itemMuscleDetailsBinding: ItemMuscleDetailsBinding) : RecyclerView.ViewHolder(itemMuscleDetailsBinding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = DataBindingUtil.inflate<ItemMuscleDetailsBinding>(LayoutInflater.from(parent.context), R.layout.item_muscle_details, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return bodyPartExercises.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) = with(holder.itemMuscleDetailsBinding) {
        exercise = bodyPartExercises[position]
        muscleDetailLinear.setOnClickListener {
            val action = MusclesDetailFragmentDirections.actionMusclesDetailFragmentToExerciseOverviewFragment(bodyPartExercises[position])
            it.findNavController().navigate(action)
        }
    }


}
package com.app.ebfitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.ItemPopularWorkoutsBinding
import com.app.ebfitapp.fragment.WorkoutFragmentDirections
import com.app.ebfitapp.model.PopularWorkoutsModel

class PopularWorkoutsAdapter(private val popularWorkouts: ArrayList<PopularWorkoutsModel>) : RecyclerView.Adapter<PopularWorkoutsAdapter.ItemHolder>() {

    inner class ItemHolder(val itemWorkoutsBinding: ItemPopularWorkoutsBinding) : RecyclerView.ViewHolder(itemWorkoutsBinding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemPopularWorkoutsBinding>(inflater, R.layout.item_popular_workouts, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return popularWorkouts.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) = with(holder.itemWorkoutsBinding) {
        workout = popularWorkouts[position]
        workoutRoot.setOnClickListener {
            val action = WorkoutFragmentDirections.actionWorkoutFragmentToWorkoutsDetailFragment(popularWorkouts[position])
            it.findNavController().navigate(action)
        }
    }


}
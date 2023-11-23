package com.app.ebfitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.databinding.ItemMuscleGroupsBinding
import com.app.ebfitapp.model.MuscleGroupModel

class MuscleGroupsAdapter(private val muscleGroups: ArrayList<MuscleGroupModel>) : RecyclerView.Adapter<MuscleGroupsAdapter.ItemHolder>() {

    inner class ItemHolder(val itemMuscleGroupsBinding: ItemMuscleGroupsBinding) : RecyclerView.ViewHolder(itemMuscleGroupsBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = ItemMuscleGroupsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return muscleGroups.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) = with(holder.itemMuscleGroupsBinding) {
        muscle = muscleGroups[position]
    }

}
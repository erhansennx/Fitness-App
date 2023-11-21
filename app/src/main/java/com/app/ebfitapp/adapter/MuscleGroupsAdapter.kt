package com.app.ebfitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.databinding.ItemMuscleGroupsBinding

class MuscleGroupsAdapter : RecyclerView.Adapter<MuscleGroupsAdapter.ItemHolder>() {

    inner class ItemHolder(val itemMuscleGroupsBinding: ItemMuscleGroupsBinding) : RecyclerView.ViewHolder(itemMuscleGroupsBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = ItemMuscleGroupsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

    }


}
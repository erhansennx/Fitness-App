package com.app.ebfitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.databinding.ItemPopularWorkoutsBinding

class PopularWorkoutsAdapter : RecyclerView.Adapter<PopularWorkoutsAdapter.ItemHolder>() {

    inner class ItemHolder(val workoutsBinding: ItemPopularWorkoutsBinding) : RecyclerView.ViewHolder(workoutsBinding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = ItemPopularWorkoutsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

    }


}
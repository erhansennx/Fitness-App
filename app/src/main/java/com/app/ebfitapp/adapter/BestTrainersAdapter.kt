package com.app.ebfitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.databinding.ItemBestTrainersBinding

class BestTrainersAdapter : RecyclerView.Adapter<BestTrainersAdapter.ItemHolder>() {

    inner class ItemHolder(val itemBestTrainersBinding: ItemBestTrainersBinding) : RecyclerView.ViewHolder(itemBestTrainersBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = ItemBestTrainersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

    }


}
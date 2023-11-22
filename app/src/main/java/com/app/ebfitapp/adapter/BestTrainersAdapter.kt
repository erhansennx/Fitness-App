package com.app.ebfitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.databinding.ItemBestTrainersBinding
import com.app.ebfitapp.model.BestTrainersModel
import com.app.ebfitapp.utils.downloadImageFromURL

class BestTrainersAdapter(private val trainersList: ArrayList<BestTrainersModel>) : RecyclerView.Adapter<BestTrainersAdapter.ItemHolder>() {

    inner class ItemHolder(val itemBestTrainersBinding: ItemBestTrainersBinding) : RecyclerView.ViewHolder(itemBestTrainersBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = ItemBestTrainersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return trainersList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) = with(holder.itemBestTrainersBinding) {
        trainerImage.downloadImageFromURL(trainersList[position].profileImageURL)
        trainerName.text = trainersList[position].username
        trainerSpecialization.text = trainersList[position].specialization
    }


}
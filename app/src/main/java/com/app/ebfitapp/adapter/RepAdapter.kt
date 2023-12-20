package com.app.ebfitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.databinding.ItemRepBinding

class RepAdapter(private val repList: List<Int>) : RecyclerView.Adapter<RepAdapter.RepViewHolder>() {

    class RepViewHolder(val bindingRep : ItemRepBinding) : RecyclerView.ViewHolder(bindingRep.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepViewHolder {

        val itemRepBinding = ItemRepBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return RepViewHolder(itemRepBinding)

    }

    override fun getItemCount(): Int {
        return repList.size
    }

    override fun onBindViewHolder(holder: RepViewHolder, position: Int) {

    }


}
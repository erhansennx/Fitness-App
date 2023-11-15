package com.app.ebfitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.ebfitapp.databinding.ItemsArticleBinding

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ItemHolder>() {

    class ItemHolder(val binding: ItemsArticleBinding) : ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemsArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

    }

}
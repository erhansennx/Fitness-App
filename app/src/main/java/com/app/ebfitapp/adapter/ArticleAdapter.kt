package com.app.ebfitapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.ebfitapp.databinding.ItemsArticleBinding
import com.app.ebfitapp.model.ArticleModel

class ArticleAdapter(private var articles: ArrayList<ArticleModel>) : RecyclerView.Adapter<ArticleAdapter.ItemHolder>() {

    class ItemHolder(val binding: ItemsArticleBinding) : ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemsArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return 10
    }
//Hocam şu resim ekleme makarası çalışmıyo galiba neresi
    // bakarim oraya okidoki sen bu surekli respond veriyor ona bi bak istersen hocam makarayı sıfırlarız düzelir okays a
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) = with(holder.binding) {
        article = articles[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newArticles: List<ArticleModel>) {
        articles = newArticles as ArrayList<ArticleModel>
        notifyDataSetChanged()
    }

}
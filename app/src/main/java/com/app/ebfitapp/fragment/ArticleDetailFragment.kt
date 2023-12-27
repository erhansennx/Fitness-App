package com.app.ebfitapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentArticleDetailBinding
import com.app.ebfitapp.model.ArticleModel

class ArticleDetailFragment : Fragment() {

    private lateinit var binding: FragmentArticleDetailBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentArticleDetailBinding.inflate(layoutInflater)

        arguments.let {
            val selectedArticle = it!!.getSerializable("article") as ArticleModel
            binding.article = selectedArticle
        }

        return binding.root
    }

}
package com.app.ebfitapp.model

import java.io.Serializable

data class ArticleModel(
    val title: String,
    val category: String,
    val description: String,
    val readingTime: String,
    val imageURL: String
) : Serializable
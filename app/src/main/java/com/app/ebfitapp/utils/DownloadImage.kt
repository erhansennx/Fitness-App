package com.app.ebfitapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.app.ebfitapp.R
import com.bumptech.glide.Glide

fun ImageView.downloadImageFromURL(url: String) {

    Glide.with(context)
        .asBitmap()
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.glide_place_animation)
        .error(R.drawable.ic_launcher_background)
        .into(this)

}

fun ImageView.downloadGifFromURL(url: String) {

    Glide.with(context)
        .asGif()
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.glide_place_animation)
        .error(R.drawable.ic_launcher_background)
        .into(this)

}

@BindingAdapter("android:getImage")
fun getImage(view: ImageView, imageURL: String) {
    view.downloadImageFromURL(imageURL)
}
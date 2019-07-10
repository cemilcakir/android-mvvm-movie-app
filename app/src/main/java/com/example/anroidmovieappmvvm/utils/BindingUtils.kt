package com.example.anroidmovieappmvvm.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("imageUrl")
fun imageUrl(imageView: ImageView, url: String) {
    Glide.with(imageView.context).load("http://image.tmdb.org/t/p/w200$url").into(imageView)
}
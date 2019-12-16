package com.rondao.shufflesongs.ui.songslist

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rondao.shufflesongs.R

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
                .load(imgUri)
                .apply(RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.ani_loading)
                        .error(R.drawable.ic_baseline_broken_image_24))
                .into(imgView)
    }
}
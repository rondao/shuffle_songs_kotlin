package com.rondao.shufflesongs.ui.songslist

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.widget.ContentLoadingProgressBar
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

@BindingAdapter("songsApiStatusLoading")
fun bindStatusLoading(statusProgressBar: ContentLoadingProgressBar, status: SongsListApiStatus?) {
    when (status) {
        SongsListApiStatus.LOADING -> {
            statusProgressBar.visibility = View.VISIBLE
        }
        else -> {
            statusProgressBar.visibility = View.GONE
        }
    }
}

@BindingAdapter("songsApiStatusError")
fun bindStatusError(statusImageView: ImageView, status: SongsListApiStatus?) {
    when (status) {
        SongsListApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
        }
        else -> {
            statusImageView.visibility = View.GONE
        }
    }
}
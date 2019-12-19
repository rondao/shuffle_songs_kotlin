package com.rondao.shufflesongs.ui.songslist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rondao.shufflesongs.R
import com.rondao.shufflesongs.domain.Track

/**
 * Customize [ImageView] with image from [imgUrl] with [Glide].
 * @param ImageView to customize image.
 * @param imgUrl to fetch image from.
 */
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

/**
 * Set [TextView] text as artist name and genre from [Track].
 * @param txtView to customize text.
 * @param song to get information from.
 */
@BindingAdapter("formatArtistGenre")
fun bindFormatSongTitle(txtView: TextView, song: Track) {
    txtView.text = "${song.artistName} (${song.primaryGenreName})"
}

/**
 * Set visibility according to [status] of loading.
 * @param statusProgressBar to hide or show.
 * @param status containing if is loading.
 */
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

/**
 * Set visibility according to [status] of error.
 * @param statusImageView to hide or show.
 * @param status containing if is error.
 */
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
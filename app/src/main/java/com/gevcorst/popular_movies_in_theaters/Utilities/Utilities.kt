package com.gevcorst.popular_movies_in_theaters.Utilities

import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gevcorst.popular_movies_in_theaters.R

object ImageLoader{
    @JvmStatic
    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val glideImgUrl = it.toUri().buildUpon().scheme("https").build()
            Glide.with(imgView.context)
                .load(glideImgUrl).apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.broken_image)
                )
                .into(imgView)
        }
    }
}
enum class MenuOptions{
    NowPLAYING,TOP_RATING,POPULAR,FAVORITE,UPCOMING
}
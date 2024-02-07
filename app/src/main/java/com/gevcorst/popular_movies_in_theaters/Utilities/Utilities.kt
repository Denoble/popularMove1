package com.gevcorst.popular_movies_in_theaters.Utilities

import android.content.Context
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gevcorst.popular_movies_in_theaters.R

object ImageLoader {
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
object CustomAlertDialog{
    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.M)
    fun showAlertDialog(
        context: Context,
        title: String,
        message: String
    ) {
        val alertDialog: AlertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(
                "close"
            ) { dialog, which ->
                dialog.dismiss()
            }
            .create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(
                AlertDialog.BUTTON_NEGATIVE
            ).setTextColor(
                context.resources.getColor(
                    R.color.ivory_white,
                    context.theme
                )
            )
        }
        alertDialog.show()
    }
}
enum class MenuOptions {
    NowPLAYING, TOP_RATING, POPULAR, FAVORITE, UPCOMING
}
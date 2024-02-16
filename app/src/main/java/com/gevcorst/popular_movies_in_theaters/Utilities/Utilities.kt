package com.gevcorst.popular_movies_in_theaters.Utilities

import android.content.Context
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gevcorst.popular_movies_in_theaters.Database.AppDatabase
import com.gevcorst.popular_movies_in_theaters.MainActivity
import com.gevcorst.popular_movies_in_theaters.R
import com.gevcorst.popular_movies_in_theaters.viewModel.MainMovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
        message: String,
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
                    R.color.colorAccent,
                    context.theme
                )
            )
        }
        alertDialog.show()
    }
    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.M)
    fun showAlertDialog(
        context: Context,
        title: String,
        message: String,
        viewModel: MainMovieViewModel
    ) {
        val alertDialog: AlertDialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(
                "close"
            ) { dialog, which ->
                viewModel.updateIsFaveEmpty(false)
                viewModel.fetchPlayingNow()
                dialog.dismiss()

            }
            .create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(
                AlertDialog.BUTTON_NEGATIVE
            ).setTextColor(
                context.resources.getColor(
                    R.color.colorAccent,
                    context.theme
                )
            )
        }
        alertDialog.show()
    }
    @JvmStatic
    fun loadFavorites(context:MainActivity,
                      viewModel: MainMovieViewModel,db:AppDatabase){
        CoroutineScope(Dispatchers.Main).launch {
            async {
                viewModel.fetchUserFavorites(db)
            }.await()
            if(context.isFaveListEmpty){
                showAlertDialog(
                    context,
                    "Empty Favorite List",
                    "Add Movie to Favourite List by Clicking" +
                            "the Heart Button on the Detail View"
                )
            }
        }
    }
}
val prefix = "Gevcorst"
enum class MenuOptions(val screenTile:String) {
    NowPLAYING("$prefix Now Playing"), TOP_RATING("$prefix Top Rating"),
    POPULAR("$prefix Popular Movies"), FAVORITE("$prefix Favourites"),
    UPCOMING("$prefix Upcoming Movies")
}
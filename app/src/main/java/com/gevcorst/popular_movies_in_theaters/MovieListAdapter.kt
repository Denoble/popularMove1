package com.gevcorst.popular_movies_in_theaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gevcorst.popular_movies_in_theaters.Model.Movie
import com.gevcorst.popular_movies_in_theaters.Utilities.ImageLoader.bindImage

class MovieListAdapter(
    private val movieList: List<Movie>,
    val context: Context,
    private val onClickListener: MovieListAdapter.OnClickListener
) :
    RecyclerView.Adapter<MovieListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.imageholder, parent, false)
        return ListViewHolder(inflatedView)
    }

    override fun getItemCount(): Int{
       return movieList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val movie = movieList[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(movie)
        }
        holder.bind(movie, context)
    }
    class ListViewHolder(v: View) :
        RecyclerView.ViewHolder(v) {
        private val view: View = v
        private val imageView: ImageView = view.findViewById(R.id.item_imageId)
        fun bind(movie: Movie,context:Context) {
            bindImage(imageView, movie.posterPath)
        }
    }

    class OnClickListener(val clickListener: (movie: Movie) -> Unit) {
        fun onClick(currentMovie: Movie) = clickListener(currentMovie)
    }

}
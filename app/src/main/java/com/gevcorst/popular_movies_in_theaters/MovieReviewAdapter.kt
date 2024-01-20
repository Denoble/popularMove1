package com.gevcorst.popular_movies_in_theaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gevcorst.popular_movies_in_theaters.Model.MovieReview
import com.gevcorst.popular_movies_in_theaters.databinding.MovieReviewItemBinding

class MovieReviewAdapter( private val reviews: List<MovieReview>,
                          val context: Context,):
    RecyclerView.Adapter<MovieReviewAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflatedView = MovieReviewItemBinding.inflate(LayoutInflater.from(parent.context))
        return ListViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
       return reviews.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val review = reviews[position]
        holder.itemView.setOnClickListener {
        }
        holder.bind(review)
    }
    class ListViewHolder(v: MovieReviewItemBinding) :
        RecyclerView.ViewHolder(v.root) {
        val authorName:TextView =  v.authorName
        val authorsComment:TextView = v.authorsComment

        fun bind(review:MovieReview) {

            authorName.text = review.author
            authorsComment.text = review.content
        }

    }
}
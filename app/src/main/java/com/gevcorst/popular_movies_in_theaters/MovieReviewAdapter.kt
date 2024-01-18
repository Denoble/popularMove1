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
        holder.bind(review, context)
    }
    class ListViewHolder(v: MovieReviewItemBinding) :
        RecyclerView.ViewHolder(v.root) {
        val authorName:TextView =  v.authorName
        val authorsComment:TextView = v.authorsComment

        fun bind(review:MovieReview, context: Context) {

            authorName.text = review.author
            authorsComment.text = review.content
        }

    }
}
/*class ListAdapter(
    private val list: List<Listings>, val context: Context,
    val onClickListener: ListAdapter.OnClickListener
) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ListViewHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listing = list[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(listing)
        }
        holder.bind(listing, context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ListViewHolder(v: View) :
        RecyclerView.ViewHolder(v) {
        private val view: View = v
        val imageView: ImageView = view.findViewById(R.id.imageView_list_item)
        val year: TextView = view.findViewById(R.id.tv_year)
        val make: TextView = view.findViewById(R.id.tv_make)
        val model: TextView = view.findViewById(R.id.tv_model)
        val trim: TextView = view.findViewById(R.id.tv_trim)
        val price: TextView = view.findViewById(R.id.tv_price)
        val milleage: TextView = view.findViewById(R.id.tv_milleage)
        val location: TextView = view.findViewById(R.id.tv_location)
        fun bind(listing: Listings, context: Context) {
            bindImage(imageView, listing.images.firstPhoto.medium)
            year.text = listing.year.toString()
            make.text = listing.make
            model.text = listing.model
            trim.text = listing.trim
            val temPrice = context.getString(R.string.dollar_sign) + listing.currentPrice.toString()
            price.text = temPrice
            val tempMileage =
                listing.mileage.toString() + context.getString(R.string.mileage_symbol)
            milleage.text = tempMileage
            val locationText = listing.dealer.city + " " + listing.dealer.state
            location.text = locationText
        }

    }

    class OnClickListener(val clickListener: (listing: Listings) -> Unit) {
        fun onClick(curentListing: Listings) = clickListener(curentListing)
    }

}*/

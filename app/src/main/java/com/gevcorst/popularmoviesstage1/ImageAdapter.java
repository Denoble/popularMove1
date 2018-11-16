package com.gevcorst.popularmoviesstage1;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gevcorst.popularmoviesstage1.Model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private static final String TAG = ImageAdapter.class.getSimpleName();
    /*
     * An on-click handler that makes it easy for an Activity to interface with
     * the RecyclerView
     */
    final private ListItemClickListener mOnClickListener;
    private static int viewHolderCount;
    private final Context mContext;
    private final List<Movie> mMovieList;
    private String mImageString;

    public ImageAdapter(int numberOfItems, ListItemClickListener listener,List<Movie> list,Context mContext) {
        int mNumberItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
       mMovieList = list;
       this.mContext = mContext;
    }


    @SuppressWarnings("unused")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.imageholder;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        ViewHolder imageViewHolderHolder = new ViewHolder(view);

        viewHolderCount++;

        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + viewHolderCount);
        return imageViewHolderHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder imageViewHolder, int position) {
        Movie movie = mMovieList.get(position);
        imageViewHolder.bind(movie, imageViewHolder.imageView);
        Log.d("ADAPTER BINDER",movie.getThumbnail());

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public class ViewHolder extends  RecyclerView.ViewHolder
            implements OnClickListener {
        // Holds the thumbnail image from the API
        final Context context;
        final ImageView imageView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * ImageViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *
         */
        ViewHolder(View itemView){
            super(itemView);
            this.context =  mContext;
            imageView = itemView.findViewById(R.id.imageId);
           // textView = (TextView)itemView.findViewById(R.id.tvId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
            //Toast toast = Toast.makeText(context,"Start DetailActivity",Toast.LENGTH_LONG);
            //toast.show();
        }
        /**
         * A method written for convenience.
         * @param movie Movie which its Thumbnail will be displayed in the imageView
         * @param imageView The imageView which holds the movie's Thumbnail
         */
        void bind(Movie movie, ImageView imageView) {
            StringBuilder mStringBuilder = new StringBuilder();
            String mImageUrl = "http://image.tmdb.org/t/p/";
            mStringBuilder.append(mImageUrl);
            String mImageSize = "w185/";
            mStringBuilder.append(mImageSize);
            mStringBuilder.append(movie.getThumbnail());
            String completeImageUrl = mStringBuilder.toString();
            Picasso.get().load(completeImageUrl).into(imageView);

        }

    }
}

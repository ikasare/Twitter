package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    Context context;
    List<Tweet> tweets;

    // Pass in the context and List of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view); // this view is the itemview(tweet) in viewholder
    }

    // Bind vaues based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);
        // Bind the tweet with view holder
        holder.bind(tweet);


    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Define a viewholder

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivTweetImage;
        ImageButton ibFavouriteButton;
        TextView tvFavoriteCount;

        public ViewHolder(@NonNull View itemView) { // itemView is a representation of one row of item passed; eg: a tweet
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetImage = itemView.findViewById(R.id.ivTweetImage);
            ibFavouriteButton = itemView.findViewById(R.id.ibFavourite);
            tvFavoriteCount = itemView.findViewById(R.id.tvFavouriteCount);
        }
        public void bind(Tweet tweet){
            int radius = 100;
            int radiusimage = 50;
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName.concat(" ").concat("•").concat(" ").concat(tweet.timestamp));
            Glide.with(context).load(tweet.user.profileImageUrl).transform(new RoundedCorners(radius)).into(ivProfileImage);

            if (tweet.imgUrl != null){
                ivTweetImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.imgUrl).transform(new RoundedCorners(radiusimage)).into(ivTweetImage);
            } else {
                ivTweetImage.setVisibility(View.GONE);
            }
            ibFavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // if not already favourited
                        // tell twitter i want to favorite
                        // change drawable to big_star_big_on
                        // increment the text inside tvFavoriteCount
                    // else if already favorited
                        // tell twitter i want to unfavourite
                        // change drawable back to off
                        // decrement text in tvfavoritecount
                }
            });
        }
    }

    // clean all elements of the recycler
    public void clear(){
        tweets.clear();
        notifyDataSetChanged();
    }

    // add a list of items
    public void addAll(List<Tweet> list){
        tweets.addAll(list);
        notifyDataSetChanged();
    }
}

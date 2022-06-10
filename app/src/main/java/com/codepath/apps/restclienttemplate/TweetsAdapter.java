package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.util.Log;
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
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.util.List;

import okhttp3.Headers;

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
            // setting the initial favourite count
            tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));

            // set the ibfavourite to grey or yellow
            if (tweet.isFavourited){
                Drawable newImage = context.getDrawable(android.R.drawable.btn_star_big_on);
                ibFavouriteButton.setImageDrawable(newImage);
            }else{
                Drawable newImage = context.getDrawable(android.R.drawable.btn_star_big_off);
                ibFavouriteButton.setImageDrawable(newImage);
            }

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
                    if(!tweet.isFavourited) {
                        // tell twitter i want to favorite
                        TwitterApp.getRestClient(context).favorite(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("adapter", "this should have been favorited");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                            }
                        });
                        // change drawable to big_star_big_on
                        Drawable newImage = context.getDrawable(android.R.drawable.btn_star_big_on);
                        ibFavouriteButton.setImageDrawable(newImage);
                        tweet.isFavourited = true;
                        // increment the text inside tvFavoriteCount
                        tweet.favoriteCount += 1;
                        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));

                    }else {
                        // else if already favorited
                        // tell twitter i want to unfavourite
                        TwitterApp.getRestClient(context).unfavorite(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("adapter", "this should have been unfavorited");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                            }
                        });
                        // change drawable back to off
                        Drawable newImage = context.getDrawable(android.R.drawable.btn_star_big_off);
                        ibFavouriteButton.setImageDrawable(newImage);
                        tweet.isFavourited = false;
                        // decrement text in tvfavoritecount
                        tweet.favoriteCount -= 1;
                        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));

                    }
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

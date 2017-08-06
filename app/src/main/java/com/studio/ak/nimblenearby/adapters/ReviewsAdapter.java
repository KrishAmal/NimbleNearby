package com.studio.ak.nimblenearby.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studio.ak.nimblenearby.R;
import com.studio.ak.nimblenearby.model.POIReviews;

import java.util.List;

/**
 * Created by Amal Krishnan on 13-12-2016.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<POIReviews> reviews;
    private Context context;

    public ReviewsAdapter(Context context, List<POIReviews> reviews) {
        this.reviews = reviews;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;
        TextView rating;

        public ViewHolder(View itemView) {
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.review_name);
            content = (TextView)itemView.findViewById(R.id.review_content);
            rating =(TextView)  itemView.findViewById(R.id.review_rating);

            content.setTypeface(Typeface.createFromAsset(itemView.getResources().getAssets(),
                    itemView.getContext().getResources().getString(R.string.roboto_light)));
            author.setTypeface(Typeface.createFromAsset(itemView.getResources().getAssets(),
                    itemView.getContext().getResources().getString(R.string.roboto_regular)));
            rating.setTypeface(Typeface.createFromAsset(itemView.getResources().getAssets(),
                    itemView.getContext().getResources().getString(R.string.roboto_light)));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, null);
        ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String rate=reviews.get(position).getRating().concat("/5");

        holder.author.setText(reviews.get(position).getAuthor_name());
        holder.content.setText(reviews.get(position).getText());
        holder.rating.setText(rate);

    }

    @Override
    public int getItemCount() {
        return this.reviews.size();
    }
}


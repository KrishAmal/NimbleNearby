package com.studio.ak.nimblenearby.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.studio.ak.nimblenearby.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amal Krishnan on 06-12-2016.
 */

public class ListRecyclerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.poi_list_item_name) TextView Name;
    @BindView(R.id.poi_list_item_distance) TextView Distance;
    @BindView(R.id.poi_list_item_vicinity) TextView Vicinity;
    @BindView(R.id.poi_list_item_dist_linear) LinearLayout distLayout;
    @BindView(R.id.poi_list_item_direction_layout) RelativeLayout relativeLayout;
    @BindView(R.id.poi_list_item_direction_text) TextView DistanceText;
    @BindView(R.id.poi_list_item_main_layout) RelativeLayout mainRelativeLayout;

    public ListRecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        Name.setTypeface(Typeface.createFromAsset(itemView.getResources().getAssets(),
                itemView.getContext().getResources().getString(R.string.roboto_regular)));
        Distance.setTypeface(Typeface.createFromAsset(itemView.getResources().getAssets(),
                itemView.getContext().getResources().getString(R.string.roboto_light)));
        Vicinity.setTypeface(Typeface.createFromAsset(itemView.getResources().getAssets(),
                itemView.getContext().getResources().getString(R.string.roboto_light)));
        DistanceText.setTypeface(Typeface.createFromAsset(itemView.getResources().getAssets(),
                itemView.getContext().getResources().getString(R.string.roboto_regular)));
    }

}

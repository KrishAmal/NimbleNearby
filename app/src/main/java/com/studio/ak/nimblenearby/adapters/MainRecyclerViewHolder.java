package com.studio.ak.nimblenearby.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.studio.ak.nimblenearby.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amal Krishnan on 04-12-2016.
 */

public class MainRecyclerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.card_icon) ImageView Icon;
    @BindView(R.id.card_name) TextView Name;;

    public MainRecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        Name.setTypeface(Typeface.createFromAsset(itemView.getResources().getAssets(),
                itemView.getContext().getResources().getString(R.string.roboto_regular)));
    }
}

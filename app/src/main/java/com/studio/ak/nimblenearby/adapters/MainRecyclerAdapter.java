package com.studio.ak.nimblenearby.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studio.ak.nimblenearby.MessageEvent;
import com.studio.ak.nimblenearby.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Amal Krishnan on 04-12-2016.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerViewHolder> {

    private List<POI> itemList;
    private Context context;

    public MainRecyclerAdapter(Context context, List<POI> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public MainRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cardview,parent,false);
        MainRecyclerViewHolder rcv = new MainRecyclerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(MainRecyclerViewHolder holder, final int position) {
        holder.Icon.setImageResource(itemList.get(position).getIcon());
        holder.Name.setText(itemList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(itemList.get(position).getName()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}

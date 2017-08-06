package com.studio.ak.nimblenearby.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studio.ak.nimblenearby.CardFragmentEvent;
import com.studio.ak.nimblenearby.R;
import com.studio.ak.nimblenearby.model.Results;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

/**
 * Created by Amal Krishnan on 06-12-2016.
 */

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerViewHolder> {

    private List<Results> itemList;
    private Context context;
    private String[] distance;

    public ListRecyclerAdapter(Context context, List<Results> itemList,String distance) {
        this.itemList = itemList;
        this.context = context;
        this.distance = distance.split(",");
    }

    @Override
    public ListRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_poi_list_item, null);
        ListRecyclerViewHolder rcv = new ListRecyclerViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ListRecyclerViewHolder holder,final int position) {

        String diff;

        holder.distLayout.setVisibility(View.VISIBLE);

        Location deviceLocation = new Location("Point 1");
        deviceLocation.setLatitude(Double.parseDouble(distance[0]));
        deviceLocation.setLongitude(Double.parseDouble(distance[1]));

        Location poiLocation=new Location("Point 2");
        poiLocation.setLatitude(Double.parseDouble(itemList.get(position).getGeometry()
                .getLocation().getLat()));
        poiLocation.setLongitude(Double.parseDouble(itemList.get(position).getGeometry()
                .getLocation().getLng()));

        if(deviceLocation.distanceTo(poiLocation)<1000) {
            diff = String.valueOf((int) deviceLocation.distanceTo(poiLocation)).concat(" mts");
            holder.Distance.setText(diff);
        }
        else {
            diff = String.format(Locale.ENGLISH,"%.2f",deviceLocation.distanceTo(poiLocation)/1000).concat(" km");
            holder.Distance.setText(diff);
        }

        holder.Vicinity.setText(itemList.get(position).getVicinity());
        holder.Name.setText(itemList.get(position).getName());

        holder.mainRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CardFragmentEvent(
                        "ListFrag",
                        itemList.get(position).getName(),
                        itemList.get(position).getPlace_id(),itemList.get(position).getVicinity(),
                        itemList.get(position).getGeometry().getLocation().getLat(),
                        itemList.get(position).getGeometry().getLocation().getLng()));
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+itemList.get(position).getGeometry()
                        .getLocation().getLat()+","+itemList.get(position).getGeometry()
                        .getLocation().getLng());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
            return this.itemList.size();
    }

}

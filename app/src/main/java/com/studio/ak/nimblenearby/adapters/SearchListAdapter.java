package com.studio.ak.nimblenearby.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studio.ak.nimblenearby.R;
import com.studio.ak.nimblenearby.model.Results;
import com.studio.ak.nimblenearby.util.SearchResultInterface;

import java.util.List;

/**
 * Created by Amal Krishnan on 14-12-2016.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private List<Results> results;
    private Context context;

    SearchResultInterface searchResultInterface;

    public SearchListAdapter(Context context, List<Results> results, SearchResultInterface
                             searchResultInterface) {
        this.searchResultInterface=searchResultInterface;
        this.context = context;
        this.results = results;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView searchResultTextview;

        public ViewHolder(View itemView) {
            super(itemView);
            searchResultTextview= (TextView) itemView.findViewById(R.id.search_item_name);
        }
    }

    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_item, null);
        SearchListAdapter.ViewHolder viewHolder =new ViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchListAdapter.ViewHolder holder, final int position) {
        holder.searchResultTextview.setText(results.get(position).getName());

        holder.searchResultTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("NAME",results.get(position).getName());
                returnIntent.putExtra("PLACE_ID",results.get(position).getPlace_id());
                returnIntent.putExtra("ADDR",results.get(position).getFormatted_address());
                returnIntent.putExtra("LAT",results.get(position).getGeometry().getLocation().getLat());
                returnIntent.putExtra("LNG",results.get(position).getGeometry().getLocation().getLng());

                searchResultInterface.setValues(returnIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

}

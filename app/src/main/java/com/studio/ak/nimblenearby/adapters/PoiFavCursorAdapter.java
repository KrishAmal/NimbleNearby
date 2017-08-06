package com.studio.ak.nimblenearby.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.studio.ak.nimblenearby.CardFragmentEvent;
import com.studio.ak.nimblenearby.R;
import com.studio.ak.nimblenearby.provider.PoiProvider;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Amal Krishnan on 11-12-2016.
 */

public class PoiFavCursorAdapter extends CursorAdapter {

    private Context mContext;

    public static class ViewHolder {
        public TextView Name;
        public TextView Vicinity;
        public RelativeLayout distLayout;
        public RelativeLayout mainLayout;

        public ViewHolder(View itemView) {
            Name = (TextView)itemView.findViewById(R.id.poi_list_item_name);
            Vicinity = (TextView)itemView.findViewById(R.id.poi_list_item_vicinity);
            distLayout = (RelativeLayout) itemView.findViewById(R.id.poi_list_item_direction_layout);
            mainLayout = (RelativeLayout) itemView.findViewById(R.id.poi_list_item_main_layout);

            Name.setTypeface(Typeface.createFromAsset(itemView.getResources().getAssets(),
                    itemView.getContext().getResources().getString(R.string.roboto_regular)));
            Vicinity.setTypeface(Typeface.createFromAsset(itemView.getResources().getAssets(),
                    itemView.getContext().getResources().getString(R.string.roboto_light)));
        }
    }

    public PoiFavCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_poi_list_item,viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        final String lat,lng;

        lat=cursor.getString(cursor.getColumnIndex(PoiProvider.Poi.POI_LAT));
        lng=cursor.getString(cursor.getColumnIndex(PoiProvider.Poi.POI_LNG));

        viewHolder.Name.setText(cursor.getString(cursor.getColumnIndex(PoiProvider.Poi.POI_NAME)));
        viewHolder.Vicinity.setText(cursor.getString(cursor.getColumnIndex(PoiProvider.Poi.POI_VICINITY)));

        viewHolder.distLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(mapIntent);
            }
        });

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CardFragmentEvent(
                        "FavFrag",
                        cursor.getString(cursor.getColumnIndex(PoiProvider.Poi.POI_NAME)),
                        cursor.getString(cursor.getColumnIndex(PoiProvider.Poi.POI_ID)),
                        cursor.getString(cursor.getColumnIndex(PoiProvider.Poi.POI_VICINITY)),
                        lat,lng));
            }
        });
    }
}

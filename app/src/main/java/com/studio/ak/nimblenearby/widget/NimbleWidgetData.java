package com.studio.ak.nimblenearby.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.studio.ak.nimblenearby.R;
import com.studio.ak.nimblenearby.provider.PoiProvider;

/**
 * Created by Amal Krishnan on 12-12-2016.
 */

public class NimbleWidgetData implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor cursor;
    private int mAppWidgetId;

    public NimbleWidgetData(Context context,Intent intent)
    {
        mContext=context;
        mAppWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        if(cursor!=null)
            cursor.close();
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        String PoiName="";
        String PoiVicinity="";

        if(cursor.moveToPosition(position)){

            PoiName=cursor.getString(cursor.getColumnIndex(PoiProvider.Poi.POI_NAME));
            PoiVicinity=cursor.getString(cursor.getColumnIndex(PoiProvider.Poi.POI_VICINITY));

        }

        RemoteViews remoteViews=new RemoteViews(mContext.getPackageName(), R.layout.nimble_widget_item);

        remoteViews.setTextViewText(R.id.poi_widget_name,PoiName);
        remoteViews.setTextViewText(R.id.poi_widget_vicinity,PoiVicinity);

        Bundle extras = new Bundle();
        extras.putString(NimbleWidgetProvider.EXTRA_ITEM1, cursor.getString(cursor.getColumnIndex(PoiProvider.Poi.POI_LAT)));
        extras.putString(NimbleWidgetProvider.EXTRA_ITEM2, cursor.getString(cursor.getColumnIndex(PoiProvider.Poi.POI_LNG)));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.poi_widget_icon, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void initData(){
        if(cursor!=null)
            cursor.close();

        cursor=mContext.getContentResolver().query(PoiProvider.CONTENT_URI.buildUpon()
                .appendPath("pois").build(),null,null,null,null);
    }
}

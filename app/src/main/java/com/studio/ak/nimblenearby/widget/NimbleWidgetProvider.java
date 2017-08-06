package com.studio.ak.nimblenearby.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.studio.ak.nimblenearby.R;

/**
 * Created by Amal Krishnan on 12-12-2016.
 */

public class NimbleWidgetProvider extends AppWidgetProvider {

    public static final String TOAST_ACTION = "com.studio.ak.nimblenearby.widget.TOAST_ACTION";
    public static final String EXTRA_ITEM1 = "com.studio.ak.nimblenearby.widget.EXTRA_ITEM1";
    public static final String EXTRA_ITEM2 = "com.studio.ak.nimblenearby.widget.EXTRA_ITEM2";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NimbleWidgetProvider", "onReceive: intent.getAction():"+intent.getAction());

        if (intent.getAction().equals(NimbleWidgetProvider.TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            Uri uri = Uri.parse("google.navigation:q=" + intent.getStringExtra(NimbleWidgetProvider.EXTRA_ITEM1)
                    + "," + intent.getStringExtra(NimbleWidgetProvider.EXTRA_ITEM2));
            Intent mInt = new Intent(Intent.ACTION_VIEW, uri);
            mInt.setPackage("com.google.android.apps.maps");
            mInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mInt);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {
            Intent intent=new Intent(context,NimbleWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.nimble_widget);
            //remoteViews.setEmptyView(R.id.listview,R.id.emptyListview);

            Intent mapIntent = new Intent(context, NimbleWidgetProvider.class);
            mapIntent.setAction(NimbleWidgetProvider.TOAST_ACTION);
            mapIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            mapIntent.setData(Uri.parse(mapIntent.toUri(Intent.URI_INTENT_SCHEME)));

            PendingIntent navPendingIntent = PendingIntent.getBroadcast(context, 0, mapIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_listview, navPendingIntent);

            remoteViews.setRemoteAdapter(R.id.widget_listview,intent);
            appWidgetManager.updateAppWidget(appWidgetIds[i],remoteViews);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}

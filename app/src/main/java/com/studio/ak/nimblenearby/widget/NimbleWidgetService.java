package com.studio.ak.nimblenearby.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Amal Krishnan on 12-12-2016.
 */

public class NimbleWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NimbleWidgetData(this.getApplicationContext(), intent);
    }
}

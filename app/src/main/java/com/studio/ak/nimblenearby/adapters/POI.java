package com.studio.ak.nimblenearby.adapters;

/**
 * Created by Amal Krishnan on 04-12-2016.
 */

public class POI {
    private String mName;
    private int mIcon;

    public POI(String name, int icon) {
        mName = name;
        mIcon = icon;
    }

    public String getName() {
        return mName;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setName(String name) {
        mName=name;
    }

    public void setIcon(int icon) {
        mIcon=icon;
    }

}

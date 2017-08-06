package com.studio.ak.nimblenearby;

/**
 * Created by Amal Krishnan on 06-12-2016.
 */

public class CardFragmentEvent {

    public final String fragName;
    public final String name;
    public final String placeId;
    public final String vicinity;
    public final String lat;
    public final String lng;

    public CardFragmentEvent(String fragName,String name, String placeId, String vicinity, String lat, String lng) {
        this.fragName=fragName;
        this.name = name;
        this.placeId = placeId;
        this.vicinity = vicinity;
        this.lat = lat;
        this.lng = lng;
    }
}

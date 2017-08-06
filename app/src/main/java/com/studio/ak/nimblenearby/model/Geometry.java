package com.studio.ak.nimblenearby.model;

/**
 * Created by Amal Krishnan on 06-12-2016.
 */

public class Geometry {

    Location location;

    public Location getLocation() {
        return location;
    }

    public class Location{

        String lat;
        String lng;

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }
    }

}

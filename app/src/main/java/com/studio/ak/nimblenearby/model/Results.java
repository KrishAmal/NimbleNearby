package com.studio.ak.nimblenearby.model;

/**
 * Created by Amal Krishnan on 06-12-2016.
 */

public class Results {

    private String name;
    private String place_id;
    private Geometry geometry;
    private String vicinity;
    private String formatted_address;

    public String getName() {
        return name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public String getVicinity() {
        return vicinity;
    }

}

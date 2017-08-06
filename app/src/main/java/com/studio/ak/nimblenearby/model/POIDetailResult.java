package com.studio.ak.nimblenearby.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amal Krishnan on 06-12-2016.
 */

public class POIDetailResult {

    private String formatted_address;
    private String formatted_phone_number;
    private String vicinity;
    private List<POIPhotos> photos;
    private List<POIReviews> reviews;

    public String getVicinity() {
        return vicinity;
    }

    @Nullable
    public String getFormatted_address() {
        return formatted_address;
    }

    @Nullable
    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public List<POIPhotos> getPhotos() {
        return photos;
    }

    public List<POIReviews> getReviews() {
        return reviews;
    }

    public POIDetailResult() {
        reviews = new ArrayList<POIReviews>();
        photos = new ArrayList<POIPhotos>();
    }
}

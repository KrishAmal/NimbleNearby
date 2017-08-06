package com.studio.ak.nimblenearby.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Amal Krishnan on 06-12-2016.
 */

public class POIDetailResponse {

    private POIDetailResult result;

    public POIDetailResult getResult() {
        return result;
    }

    public static POIResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        POIResponse poiResponse = gson.fromJson(response, POIResponse.class);
        return poiResponse;
    }

}

package com.studio.ak.nimblenearby.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amal Krishnan on 06-12-2016.
 */

public class POIResponse {

    private List<Results> results;

    public List<Results> getResults() {
        return results;
    }

    public POIResponse() {
       results = new ArrayList<Results>();
    }

    public static POIResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        POIResponse poiResponse = gson.fromJson(response, POIResponse.class);
        return poiResponse;
    }
}

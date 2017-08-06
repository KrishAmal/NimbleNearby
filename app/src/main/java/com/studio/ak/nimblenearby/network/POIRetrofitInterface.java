package com.studio.ak.nimblenearby.network;

import com.studio.ak.nimblenearby.model.POIDetailResponse;
import com.studio.ak.nimblenearby.model.POIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Amal Krishnan on 05-12-2016.
 */

public interface POIRetrofitInterface {

    @GET("/maps/api/place/nearbysearch/json?&rankby=distance")
    Call<POIResponse> listPOI(
            @Query("type") String type,
            @Query("location") String location,
            @Query("key") String key
    );

    @GET("/maps/api/place/details/json?")
    Call<POIDetailResponse> poiDetails(
            @Query("placeid") String placeid,
            @Query("key") String key
    );

//    @GET("/maps/api/place/photo?")
//    Call<ResponseBody> poiPhotos(
//            @Query("maxwidth") String maxwidth,
//            @Query("photoreference") String photoreference,
//            @Query("key") String key
//    );

    @GET("/maps/api/place/textsearch/json?")
    Call<POIResponse> listSearchPOI(
            @Query("query") String query,
            @Query("key") String key
    );

}

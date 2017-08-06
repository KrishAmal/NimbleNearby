package com.studio.ak.nimblenearby.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.studio.ak.nimblenearby.BuildConfig;
import com.studio.ak.nimblenearby.R;
import com.studio.ak.nimblenearby.adapters.ReviewsAdapter;
import com.studio.ak.nimblenearby.model.POIDetailResponse;
import com.studio.ak.nimblenearby.network.POIRetrofitInterface;
import com.studio.ak.nimblenearby.provider.PoiProvider;
import com.studio.ak.nimblenearby.util.NetworkCheck;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Amal Krishnan on 06-12-2016.
 */

public class CardFragment extends Fragment {

    private static final String TAG="CardFragment";
    private String name,place_id,vicinity,lat,lng;
    private String reviews;

    private Cursor cursor;
    private POIDetailResponse poiDetailResponse;
    private ProgressDialog progress;

    @BindView(R.id.card_name) TextView cardName;
    @BindView(R.id.card_address) TextView cardAddress;
    @BindView(R.id.card_direction) TextView cardDirection;
    @BindView(R.id.card_phone) TextView cardPhone;
    @BindView(R.id.card_image) ImageView cardImage;
    @BindView(R.id.card_gradient) View cardGradient;
    @BindView(R.id.card_call) TextView cardCall;
    @BindView(R.id.review_text) TextView cardReviewText;
    @BindView(R.id.card_fav_btn) ToggleButton cardFavBtn;
    @BindView(R.id.card_share_btn) ImageView cardShare;
    @BindView(R.id.card_no_image) FrameLayout cardNoimage;
    @BindView(R.id.review_list) RecyclerView recyclerView;

    private int isFav=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        name=getArguments().getString("POI_NAME");
        place_id=getArguments().getString("POI_ID");
        vicinity=getArguments().getString("POI_VICINITY");
        lat=getArguments().getString("POI_LAT");
        lng=getArguments().getString("POI_LNG");
        Log.d(TAG, "onCreate: "+name);

        cursor=getActivity().getContentResolver().query(PoiProvider.CONTENT_URI.buildUpon()
                .appendPath(getResources().getString(R.string.table_name))
                .build(),null, PoiProvider.Poi.POI_ID+"="+"'"+place_id+"'",null,null);

        if(cursor.getCount() > 0) {
            while(cursor.moveToNext())
                isFav = cursor.getInt(cursor.getColumnIndex(PoiProvider.Poi.POI_FAV));
        }
        else {
            isFav = 0;
        }

        if(!NetworkCheck.isNetworkConnected(getActivity())){
            Snackbar.make(getActivity().findViewById(R.id.content_main), R.string.no_internet, Snackbar.LENGTH_LONG)
                    .show();
        }else
            fetch_details();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_card,container,false);
        ButterKnife.bind(this,view);

        cardName.setTypeface(Typeface.createFromAsset(view.getResources().getAssets(), "Roboto-Regular.ttf"));
        cardAddress.setTypeface(Typeface.createFromAsset(view.getResources().getAssets(), "Roboto-Light.ttf"));
        cardPhone.setTypeface(Typeface.createFromAsset(view.getResources().getAssets(), "Roboto-Light.ttf"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(isFav==1) {
            cardFavBtn.setChecked(true);
        }
        else
            cardFavBtn.setChecked(false);

        cardShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, name+" . "+cardAddress.getText());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
            }
        });

        cardCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + cardPhone.getText()));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        cardDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                getActivity().startActivity(mapIntent);
            }
        });

        cardFavBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getActivity(), R.string.fav, Toast.LENGTH_SHORT).show();
                    ContentValues contVal = new ContentValues();

                    contVal.put(PoiProvider.Poi.POI_ID,place_id);
                    contVal.put(PoiProvider.Poi.POI_NAME,name);
                    contVal.put(PoiProvider.Poi.POI_VICINITY,vicinity);
                    contVal.put(PoiProvider.Poi.POI_ADDRESS,cardAddress.getText().toString());
                    contVal.put(PoiProvider.Poi.POI_PHONE,cardPhone.getText().toString());
                    contVal.put(PoiProvider.Poi.POI_REVIEW,reviews);
                    contVal.put(PoiProvider.Poi.POI_LAT,lat);
                    contVal.put(PoiProvider.Poi.POI_LNG,lng);
                    contVal.put(PoiProvider.Poi.POI_FAV,1);

                    try {
                        getActivity().getContentResolver().insert(PoiProvider.CONTENT_URI.buildUpon()
                                .appendPath(getResources().getString(R.string.table_name))
                                .build(), contVal);

                        Log.d(TAG, "Insert Successful");
                        Cursor cursor = getActivity().getContentResolver().query
                                (PoiProvider.CONTENT_URI.buildUpon()
                                        .appendPath(getResources().getString(R.string.table_name))
                                        .build(), null, PoiProvider.Poi.POI_ID + "=" +"'"+place_id
                                        +"'", null, null);
                        if (cursor != null) {
                            String temp = cursor.getString(cursor.getColumnIndex(PoiProvider.Poi.POI_ID));
                            Log.d(TAG, "the value is :" + temp);
                        }
                        cursor.close();
                    }
                    catch (Exception e){
                        Log.d(TAG, "onCheckedChanged: Could Not insert"+ e.getMessage());
                    }
                }
                else {
                    Toast.makeText(getActivity(), R.string.unfav, Toast.LENGTH_SHORT).show();
                    getActivity().getContentResolver().delete(PoiProvider.CONTENT_URI.buildUpon()
                            .appendPath(getResources().getString(R.string.table_name))
                            .build(),PoiProvider.Poi.POI_ID + "=" +"'"+place_id+"'",null);

                    Log.d(TAG,"Delete Successful");
                }
            }
        });
        return view;
    }

    public class PoiPhotoAsync extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            cardImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            cardGradient.setVisibility(View.VISIBLE);
            cardImage.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];

            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    private void fetch_details(){

        progress = new ProgressDialog(getActivity());
        progress.setTitle(R.string.progress_dialog_title);
        progress.setMessage(getResources().getString(R.string.progress_dialog_message));
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);
        progress.show();

//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        POIRetrofitInterface apiService =
                retrofit.create(POIRetrofitInterface.class);

        final Call<POIDetailResponse> call=apiService.poiDetails(place_id, BuildConfig.API_KEY);

        progress.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog){
                call.cancel();
                getFragmentManager().popBackStack();
            }});

        call.enqueue(new Callback<POIDetailResponse>() {
            @Override
            public void onResponse(Call<POIDetailResponse> call, Response<POIDetailResponse> response) {
                poiDetailResponse=response.body();

                cardName.setText(name);

                try {
                    cardAddress.setText(poiDetailResponse.getResult().getFormatted_address());
                }catch (NullPointerException npe) {
                    cardAddress.setText(R.string.no_address_text);
                }

                try {
                    if(poiDetailResponse.getResult().getFormatted_phone_number().isEmpty()||
                            poiDetailResponse.getResult().getFormatted_phone_number().contains("null")) {
                        cardPhone.setText(R.string.no_phone_text);
                        cardCall.setTextColor(getResources().getColor(R.color.grey));
                        cardCall.setClickable(false);
                    }
                    else
                        cardPhone.setText(poiDetailResponse.getResult().getFormatted_phone_number());
                }catch (NullPointerException npe){
                    cardPhone.setText(R.string.no_phone_text);
                    cardCall.setTextColor(getResources().getColor(R.color.grey));
                    cardCall.setClickable(false);
                }

                try {

                    if (response.body().getResult().getReviews().isEmpty()) {
                        cardReviewText.setTextColor(getResources().getColor(R.color.grey));
                    } else {

                        ReviewsAdapter reviewsAdapter=new ReviewsAdapter(getActivity(),
                                response.body().getResult().getReviews());
                        recyclerView.setAdapter(reviewsAdapter);
                    }
                }catch (NullPointerException e){

                }

                progress.dismiss();

                try {
                    if (response.body().getResult().getPhotos().isEmpty()) {
                        cardNoimage.setVisibility(View.VISIBLE);
                    } else {
                        String ref = response.body().getResult().getPhotos().get(0).getPhoto_reference();

                        HttpUrl url = new HttpUrl.Builder()
                                .scheme("https")
                                .host("maps.googleapis.com")
                                .addPathSegments("maps/api/place/photo")
                                .addQueryParameter("maxwidth", "400")
                                .addQueryParameter("photoreference", ref)
                                .addQueryParameter("key", BuildConfig.API_KEY)
                                .build();

                        Log.d(TAG, "onResponse: URL:" + url);
                        new PoiPhotoAsync().execute(url.toString());
                    }
                }catch (NullPointerException e){
                    Log.d(TAG, "onResponse: "+"Exception :"+e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<POIDetailResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: Failed "+t.getMessage());
                progress.dismiss();

                new AlertDialog.Builder(getActivity()).setMessage(R.string.alert_dialog_message).show();

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
    }
}

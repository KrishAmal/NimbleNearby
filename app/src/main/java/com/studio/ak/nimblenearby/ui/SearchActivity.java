package com.studio.ak.nimblenearby.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.studio.ak.nimblenearby.BuildConfig;
import com.studio.ak.nimblenearby.R;
import com.studio.ak.nimblenearby.adapters.SearchListAdapter;
import com.studio.ak.nimblenearby.model.POIResponse;
import com.studio.ak.nimblenearby.network.POIRetrofitInterface;
import com.studio.ak.nimblenearby.util.NetworkCheck;
import com.studio.ak.nimblenearby.util.SearchResultInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Amal Krishnan on 14-12-2016.
 */

public class SearchActivity extends AppCompatActivity implements SearchResultInterface {

    private final static String TAG = "SearchActivity";
    private String query;
    private POIResponse poiResponse;
    private RecyclerView recyclerView;
    private ProgressDialog progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        query= getIntent().getStringExtra("SEARCH_QUERY");

        if(query!=null) {
            if(!NetworkCheck.isNetworkConnected(this)){
                Snackbar.make(findViewById(R.id.search_main), R.string.no_internet, Snackbar.LENGTH_LONG)
                        .show();
            }else
                fetch();

            SearchActivity.this.setTitle(query);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.search_list);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fetch() {
        if(query!=null){

            progress = new ProgressDialog(SearchActivity.this);
            progress.setTitle(R.string.progress_dialog_title);
            progress.setMessage(getResources().getString(R.string.progress_dialog_message));
            progress.setCancelable(true);
            progress.setCanceledOnTouchOutside(false);
            progress.show();

//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.base_url))
//                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            POIRetrofitInterface apiService =
                    retrofit.create(POIRetrofitInterface.class);

            final Call<POIResponse> call = apiService.listSearchPOI(query, BuildConfig.API_KEY);

            progress.setOnCancelListener(new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface dialog){
                    call.cancel();
                    finish();
                }});

            call.enqueue(new Callback<POIResponse>() {
                @Override
                public void onResponse(Call<POIResponse> call, Response<POIResponse> response) {

                    poiResponse=response.body();

                    SearchListAdapter searchListAdapter=new SearchListAdapter(getApplicationContext(),
                            poiResponse.getResults(),SearchActivity.this);

                    recyclerView.setAdapter(searchListAdapter);
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<POIResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: FAIL"+t.getMessage());
                    progress.dismiss();

                    new AlertDialog.Builder(SearchActivity.this).setMessage(R.string.alert_dialog_message).show();

                }
            });
        }

    }

    @Override
    public void setValues(Intent intent) {
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}

package com.studio.ak.nimblenearby.ui;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.studio.ak.nimblenearby.BuildConfig;
import com.studio.ak.nimblenearby.R;
import com.studio.ak.nimblenearby.adapters.ListRecyclerAdapter;
import com.studio.ak.nimblenearby.model.POIResponse;
import com.studio.ak.nimblenearby.network.POIRetrofitInterface;
import com.studio.ak.nimblenearby.util.NetworkCheck;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Amal Krishnan on 05-12-2016.
 */

public class PoiListFragment extends Fragment {

    private final static String TAG = "PoiListFragment";

    private String location;
    private String POI_Name;
    private POIResponse poiResponse;
    private ProgressDialog progress;

    @BindView(R.id.poi_list_recycler_view)
    RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        setHasOptionsMenu(true);
        POI_Name = getArguments().getString("POI_NAME");
        location = getArguments().getString("LOCATION");

        if (!NetworkCheck.isNetworkConnected(getActivity())) {
            Snackbar.make(getActivity().findViewById(R.id.content_main), R.string.no_internet, Snackbar.LENGTH_LONG)
                    .show();
        } else
            POI_call();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_poi_list, parent, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void POI_call() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        progress = new ProgressDialog(getActivity());
        progress.setTitle(R.string.progress_dialog_title);
        progress.setMessage(getResources().getString(R.string.progress_dialog_message));
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        POIRetrofitInterface apiService =
                retrofit.create(POIRetrofitInterface.class);

        String name = POI_Name.toLowerCase();
        if (name.contains(" "))
            name = name.replace(" ", "_");

        final Call<POIResponse> call = apiService.listPOI(name, location, BuildConfig.API_KEY);

        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                call.cancel();
                getFragmentManager().popBackStack();
            }
        });

        call.enqueue(new Callback<POIResponse>() {
            @Override
            public void onResponse(Call<POIResponse> call, Response<POIResponse> response) {

                progress.dismiss();
                poiResponse = response.body();

                ListRecyclerAdapter listRecyclerAdapter = new ListRecyclerAdapter(getActivity(),
                        poiResponse.getResults(), location);
                recyclerView.setAdapter(listRecyclerAdapter);

            }

            @Override
            public void onFailure(Call<POIResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: FAIL" + t.getMessage());
                progress.dismiss();

                new AlertDialog.Builder(getActivity()).setMessage(R.string.alert_dialog_message).show();

            }
        });
    }

}

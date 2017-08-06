package com.studio.ak.nimblenearby.ui;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.studio.ak.nimblenearby.CardFragmentEvent;
import com.studio.ak.nimblenearby.MessageEvent;
import com.studio.ak.nimblenearby.R;
import com.studio.ak.nimblenearby.adapters.PoiFavCursorAdapter;
import com.studio.ak.nimblenearby.provider.PoiProvider;
import com.studio.ak.nimblenearby.util.NetworkCheck;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener ,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG="MainActivity";
    private static final int LOCATION_PERMISSION_REQ=0;
    private static final int URL_LOADER = 0;

    private LocationManager locationManager;
    private FragmentTransaction ft;
    private FragmentManager fragmentManager;
    private  GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mLatitudeText;
    private String mLongitudeText;
    private ActionBarDrawerToggle toggle;
    private PoiFavCursorAdapter poiFavCursorAdapter;

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getLoaderManager().initLoader(URL_LOADER, null, this);

        if(!NetworkCheck.isNetworkConnected(this)){
            Snackbar.make(findViewById(R.id.content_main), R.string.no_internet, Snackbar.LENGTH_LONG)
                    .show();
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        poiFavCursorAdapter= new PoiFavCursorAdapter(getApplicationContext(),null,0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

        fragmentManager = getFragmentManager();
        MainFragment mainFragment = new MainFragment();
        ft = fragmentManager.beginTransaction();
        ft.replace(R.id.placeholder, mainFragment,"MainFrag")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        //fragmentManager.enableDebugLogging(true);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        PoiListFragment poiListFragment = new PoiListFragment();
        Bundle args = new Bundle();
        args.putString("LOCATION",mLatitudeText+","+mLongitudeText);
        args.putString("POI_NAME", event.message);
        poiListFragment.setArguments(args);
        ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.placeholder, poiListFragment,"ListFrag")
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CardFragmentEvent event) {

        CardFragment cardFragment = new CardFragment();
        Bundle args2 = new Bundle();
        args2.putString("POI_ID", event.placeId);
        args2.putString("POI_NAME", event.name);
        args2.putString("POI_VICINITY", event.vicinity);
        args2.putString("POI_LAT",event.lat);
        args2.putString("POI_LNG",event.lng);
        cardFragment.setArguments(args2);

        ft = getFragmentManager().beginTransaction();
        ft.hide(getFragmentManager()
                .findFragmentByTag(event.fragName))
                .add(R.id.placeholder, cardFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(getFragmentManager().getBackStackEntryCount() != 0){
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                fetchResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchResults(String query){

        Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
        intent.putExtra("SEARCH_QUERY",query);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                CardFragment cardFragment = new CardFragment();
                Bundle args2 = new Bundle();
                args2.putString("POI_ID", data.getStringExtra("PLACE_ID"));
                args2.putString("POI_NAME", data.getStringExtra("NAME"));
                args2.putString("POI_VICINITY", data.getStringExtra("ADDR"));
                args2.putString("POI_LAT",data.getStringExtra("LAT"));
                args2.putString("POI_LNG",data.getStringExtra("LNG"));
                cardFragment.setArguments(args2);
                ft = getFragmentManager().beginTransaction();
                ft.hide(getFragmentManager()
                        .findFragmentByTag("MainFrag"))
                        .add(R.id.placeholder, cardFragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//
//            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if(getFragmentManager().getBackStackEntryCount() != 0){
                getFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            MainFragment mainFragment = new MainFragment();
            ft = fragmentManager.beginTransaction();
            ft.replace(R.id.placeholder, mainFragment,"MainFrag")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();

        } else if (id == R.id.nav_fav) {
            openFavoriteScreen();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFavoriteScreen(){
        if(getFragmentManager().getBackStackEntryCount() != 0){
            getFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FavoriteFragment favoriteFragment =new FavoriteFragment();
        ft = getFragmentManager().beginTransaction();
        favoriteFragment.setAdapter(poiFavCursorAdapter);

        ft.replace(R.id.placeholder,favoriteFragment,"FavFrag")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQ);

        }
        else {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            mLastLocation = locationManager.getLastKnownLocation(locationProvider);

            if (mLastLocation != null) {
                mLatitudeText=String.valueOf(mLastLocation.getLatitude());
                mLongitudeText=String.valueOf(mLastLocation.getLongitude());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQ: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                        String locationProvider = LocationManager.NETWORK_PROVIDER;
                        mLastLocation = locationManager.getLastKnownLocation(locationProvider);
                    }catch (SecurityException se){

                    }
                    if (mLastLocation != null) {
                        mLatitudeText=String.valueOf(mLastLocation.getLatitude());
                        mLongitudeText=String.valueOf(mLastLocation.getLongitude());
                    }
                } else {
                    Snackbar.make(findViewById(R.id.content_main), R.string.set_location, Snackbar.LENGTH_LONG)
                            .show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        switch (loaderId){
            case URL_LOADER:
                return new CursorLoader(this,
                        PoiProvider.CONTENT_URI.buildUpon().appendPath("pois").build(),
                        null,
                        null,
                        null,
                        null);
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        poiFavCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        poiFavCursorAdapter.swapCursor(null);
    }
}

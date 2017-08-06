package com.studio.ak.nimblenearby.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.studio.ak.nimblenearby.R;
import com.studio.ak.nimblenearby.adapters.MainRecyclerAdapter;
import com.studio.ak.nimblenearby.adapters.POI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amal Krishnan on 04-12-2016.
 */

public class MainFragment extends Fragment {

    @BindView(R.id.main_recycler_view) RecyclerView rView;
    List<POI> rowListItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        rowListItem = getAllItemList();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_main,parent,false);
        ButterKnife.bind(this, view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        MainRecyclerAdapter rcAdapter = new MainRecyclerAdapter(getActivity().getApplicationContext()
                , rowListItem);
        rView.setAdapter(rcAdapter);

        return view;
    }


    private List<POI> getAllItemList(){

        List<POI> allItems = new ArrayList<POI>();
        allItems.add(new POI(getResources().getString(R.string.atm), R.drawable.cash_usd));
        allItems.add(new POI(getResources().getString(R.string.hosp),R.drawable.hospital));
        allItems.add(new POI(getResources().getString(R.string.logding),R.drawable.hotel));

        allItems.add(new POI(getResources().getString(R.string.movie),R.drawable.filmstrip));
        allItems.add(new POI(getResources().getString(R.string.restaurant),R.drawable.food_fork_drink));
        allItems.add(new POI(getResources().getString(R.string.cafe),R.drawable.coffee));

        allItems.add(new POI(getResources().getString(R.string.bakery),R.drawable.food));
        allItems.add(new POI(getResources().getString(R.string.bus),R.drawable.bus));
        allItems.add(new POI(getResources().getString(R.string.lib),R.drawable.library));

        allItems.add(new POI(getResources().getString(R.string.mall),R.drawable.shopping));
        allItems.add(new POI(getResources().getString(R.string.taxi),R.drawable.taxi));
        allItems.add(new POI(getResources().getString(R.string.pharmacy),R.drawable.pharmacy));

        allItems.add(new POI(getResources().getString(R.string.park),R.drawable.pine_tree));
        allItems.add(new POI(getResources().getString(R.string.parking),R.drawable.parking));

        return allItems;
    }
}

package com.studio.ak.nimblenearby.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.studio.ak.nimblenearby.R;
import com.studio.ak.nimblenearby.adapters.PoiFavCursorAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amal Krishnan on 11-12-2016.
 */

public class FavoriteFragment extends Fragment {

    @BindView(R.id.fav_list_view) ListView listView;
    private PoiFavCursorAdapter poiFavCursorAdapter;

    @Nullable
    public void setAdapter(PoiFavCursorAdapter poiFavCursorAdapter){
        this.poiFavCursorAdapter=poiFavCursorAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fav,container,false);
        ButterKnife.bind(this,view);
        listView.setAdapter(poiFavCursorAdapter);
        return view;
    }

}

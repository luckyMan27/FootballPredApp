package com.fortunato.footballpredictions.Fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.Activities.MainActivity;
import com.fortunato.footballpredictions.Adapters.CountryRecyclerView;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.Country;
import com.fortunato.footballpredictions.Networks.LoadImage;
import com.fortunato.footballpredictions.Networks.NetworkHome;
import com.fortunato.footballpredictions.R;
import com.fortunato.footballpredictions.tools.SaveData;

import java.io.Serializable;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private static final String STATE_ITEMS = "items";
    private static final String RECYCLER_LAYOUT = "recLayout";
    private static final String NETFLAG = "netFlag";

    private List<Country> items = null;
    private Boolean flagNetwork = true;
    private ProgressBar progBar = null;

    private RecyclerView recyclerView = null;
    private CountryRecyclerView countryRecyclerView = null;
    private Parcelable recyclerLayout = null;

    private ViewGroup container = null;

    private LeagueFragment lFragment = null;
    private String nextUrl = null;

    private SaveData handleData;

    public HomeFragment() { }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handleData = new SaveData(getContext());
        if(savedInstanceState != null){
            flagNetwork = savedInstanceState.getBoolean(NETFLAG);
            items = (List<Country>)savedInstanceState.get(STATE_ITEMS);
            recyclerLayout = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
        }
        if(items == null) {
            items = handleData.loadCountries();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_ITEMS, (Serializable) items);
        outState.putParcelable(RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putBoolean(NETFLAG, flagNetwork);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        recyclerView = container.findViewById(R.id.recView);
        progBar = container.findViewById(R.id.progBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if(recyclerLayout != null){
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerLayout);
        }

        recyclerView.setHasFixedSize(true);

        countryRecyclerView = new CountryRecyclerView(items, HomeFragment.this);
        recyclerView.setAdapter(countryRecyclerView);

        if(!MainActivity.NETWORK_CONNECTION){
            Toast.makeText(getContext(), "Network Connection is unavailable!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(flagNetwork){
            if(items.size()==0) {
                progBar.setVisibility(View.VISIBLE);
                NetworkHome networkHome = new NetworkHome("countries", 0,
                        null, HomeFragment.this, getActivity());
                Thread tNet = new Thread(networkHome);
                tNet.start();
            } else{
                loadImage();
            }
            flagNetwork = false;
        }
    }

    public void addItem(BaseType object){
        items.add((Country) object);
    }

    public ProgressBar getProgBar() {
        return progBar;
    }

    public void flush(){
        handleData.saveCountries(items);
        countryRecyclerView.notifyDataSetChanged();
    }

    private void loadImage(){
        for(Country country : items){
            if(country.getLoadImage()!=null) country.getLoadImage().run();
            else if(country.getUrlImg()!=null && !country.getUrlImg().equals("null")){
                country.setLoadImage( new LoadImage(country.getUrlImg(), null, country));
                country.getLoadImage().start();
            }
        }
        countryRecyclerView.notifyDataSetChanged();
    }

    public void modifyContent(String url, int requestType, String leagueId, String countryName){
        if(url!=null && !url.equals(nextUrl)) {
            nextUrl = url;
            lFragment = new LeagueFragment(url, requestType, leagueId, countryName);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, lFragment)
                    .addToBackStack(null)
                    .commit();
        } else if(url.equals(nextUrl)){
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, lFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
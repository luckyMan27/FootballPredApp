package com.fortunato.footballpredictions.Fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.Activities.MainActivity;
import com.fortunato.footballpredictions.Adapters.LeagueRecyclerView;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.League;
import com.fortunato.footballpredictions.Networks.LoadImage;
import com.fortunato.footballpredictions.Networks.NetworkHome;
import com.fortunato.footballpredictions.R;
import com.fortunato.footballpredictions.tools.SaveData;

import java.util.List;

public class LeagueFragment extends BaseFragment {
    private static final String RECYCLER_LAYOUT = "recLayout";
    private static final String NETFLAG = "netFlag";

    private List<League> items = null;
    private Boolean flagNetwork = true;
    private ProgressBar progBar = null;

    private RecyclerView recyclerView = null;
    private LeagueRecyclerView leagueRecyclerView = null;
    private Parcelable recyclerLayout = null;

    private ViewGroup container = null;

    private String url;
    private int requestType;
    private String leagueId;
    private String country;

    private String reqLigueId = "";
    private MatchFragment mFragment = null;

    private SaveData handleData;

    public LeagueFragment() { }

    public LeagueFragment(String url, int requestType, String leagueId, String countryName) {
        this.url = url;
        this.requestType = requestType;
        this.leagueId = leagueId;
        this.country = countryName;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handleData = new SaveData(getContext());
        if(savedInstanceState != null){
            flagNetwork = savedInstanceState.getBoolean(NETFLAG);
            recyclerLayout = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
        }
        if(items == null) {
            items = handleData.loadLeagues(country);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(recyclerView != null)
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
        TextView titleText = container.findViewById(R.id.sportSelected);
        titleText.setText("Leagues - "+country);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if(recyclerLayout != null){
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerLayout);
        }

        recyclerView.setHasFixedSize(true);

        leagueRecyclerView = new LeagueRecyclerView(items, LeagueFragment.this);
        recyclerView.setAdapter(leagueRecyclerView);

        if(MainActivity.NETWORK_CONNECTION == false){
            Toast.makeText(getContext(), "Network Connection is unavailable!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(flagNetwork){
            if(items.size()==0) {
                progBar.setVisibility(View.VISIBLE);
                NetworkHome networkHome = new NetworkHome(url, requestType,
                        leagueId, LeagueFragment.this, getActivity());
                Thread tNet = new Thread(networkHome);
                tNet.start();
            } else {
                for(League league : items){
                    if(league.getLoadImage()!=null) league.getLoadImage().run();
                    else if(league.getUrlImg()!=null && !league.getUrlImg().equals("null")){
                        league.setLoadImage( new LoadImage(league.getUrlImg(), null, league));
                        league.getLoadImage().start();
                    }
                }
                leagueRecyclerView.notifyDataSetChanged();
            }
        }
        flagNetwork = false;
    }

    public void addItem(BaseType object){
        items.add((League) object);
    }

    public ProgressBar getProgBar() {
        return progBar;
    }

    public void flush(){
        League league = items.get(0);
        handleData.saveLeagues(items, league.getCountry());
        leagueRecyclerView.notifyDataSetChanged();
    }

    public void modifyContent(String url, int requestType, String leagueId, String leagueName){
        if(!reqLigueId.equals(leagueId)) {
            reqLigueId = leagueId;
            mFragment = new MatchFragment(url, requestType, leagueId, leagueName);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}

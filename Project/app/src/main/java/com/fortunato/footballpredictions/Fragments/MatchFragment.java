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
import com.fortunato.footballpredictions.Adapters.MatchRecyclerView;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.LeagueFixture;
import com.fortunato.footballpredictions.DataStructures.SingletonCurrentFragment;
import com.fortunato.footballpredictions.Networks.NetworkHome;
import com.fortunato.footballpredictions.R;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class MatchFragment extends BaseFragment {

    private static final String RECYCLER_LAYOUT = "recLayout";
    private static final String NETFLAG = "netFlag";

    private List<LeagueFixture> items = null;
    private Boolean flagNetwork = true;
    private ProgressBar progBar = null;

    private RecyclerView recyclerView = null;
    private MatchRecyclerView matchRecyclerView = null;
    private Parcelable recyclerLayout = null;

    private ViewGroup container = null;

    private String url;
    private int requestType;
    private String leagueId;
    private String leagueName;

    public MatchFragment() { }

    public MatchFragment(String url, int requestType, String leagueId, String leagueName) {
        this.url = url;
        this.requestType = requestType;
        this.leagueId = leagueId;
        this.leagueName = leagueName;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            flagNetwork = savedInstanceState.getBoolean(NETFLAG);
            //items = (List<LeagueFixture>)savedInstanceState.get(STATE_ITEMS);
            recyclerLayout = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
        }
        if(items == null) {
            items = new LinkedList<>();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putSerializable(STATE_ITEMS, (Serializable) items);
        if(recyclerView!=null)
            outState.putParcelable(RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        //else outState.putParcelable(RECYCLER_LAYOUT, null);
        outState.putBoolean(NETFLAG, flagNetwork);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
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
        SingletonCurrentFragment.setCurrentId(this.getId());
        recyclerView = container.findViewById(R.id.recView);
        progBar = container.findViewById(R.id.progBar);
        TextView titleText = container.findViewById(R.id.sportSelected);
        titleText.setText("Games - "+leagueName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if(recyclerLayout != null){
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerLayout);
        }

        recyclerView.setHasFixedSize(true);

        matchRecyclerView = new MatchRecyclerView(items, leagueName);
        recyclerView.setAdapter(matchRecyclerView);

        if(MainActivity.NETWORK_CONNECTION == false){
            Toast.makeText(getContext(), "Network Connection is unavailable!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(flagNetwork){
            progBar.setVisibility(View.VISIBLE);
            NetworkHome networkHome = new NetworkHome(url, requestType,
                    leagueId, MatchFragment.this, getActivity());
            Thread tNet = new Thread(networkHome);
            tNet.start();
            flagNetwork = false;
        }
    }

    public void addItem(BaseType object){
        items.add((LeagueFixture)object);
    }

    public ProgressBar getProgBar() {
        return progBar;
    }

    public void flush(){
        matchRecyclerView.notifyDataSetChanged();
    }

    public void onResume() {
        super.onResume();
        SingletonCurrentFragment.setCurrentId(this.getId());
    }
}

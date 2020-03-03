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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.Activities.MainActivity;
import com.fortunato.footballpredictions.Adapters.BetRecyclerView;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.Bet;
import com.fortunato.footballpredictions.DataStructures.Bet_Item;
import com.fortunato.footballpredictions.DataStructures.SingletonCurrentBet;
import com.fortunato.footballpredictions.Networks.NetworkBackend;
import com.fortunato.footballpredictions.R;
import com.fortunato.footballpredictions.tools.SaveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BetFragment extends Fragment {

    private ViewGroup container = null;

    private static final String STATE_ITEMS = "items";
    private static final String RECYCLER_LAYOUT = "recLayout";
    private static final String NETFLAG = "netFlag";

    private List<Bet> items = null;
    private Boolean flagNetwork = true;
    private ProgressBar progBar = null;

    private RecyclerView recyclerView = null;
    private BetRecyclerView betRecyclerView = null;
    private Parcelable recyclerLayout = null;

    private LeagueFragment lFragment = null;
    private String nextUrl = null;

    private SaveData handleData;

    private DatabaseReference ref = null;
    private BetRecyclerView bet_recycler= null;
    private FirebaseAuth userAuth;

    public BetFragment() { }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.container = container;
        return inflater.inflate(R.layout.fragment_bet, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();


        recyclerView = container.findViewById(R.id.recViewBet);

        progBar = container.findViewById(R.id.progBarBet);
        progBar.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if(recyclerLayout != null){
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerLayout);
        }

        recyclerView.setHasFixedSize(true);
        items = new ArrayList<Bet>();
        betRecyclerView = new BetRecyclerView(items, BetFragment.this);
        recyclerView.setAdapter(betRecyclerView);

        userAuth = FirebaseAuth.getInstance();

        if(!MainActivity.NETWORK_CONNECTION){
            Toast.makeText(getContext(), "Network Connection is unavailable!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(userAuth.getCurrentUser()!=null){

            final String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

            NetworkBackend net = new NetworkBackend(BetFragment.this, null , 1, null, null, currentFirebaseUser, getActivity(), null);
            Thread tNet = new Thread(net);
            tNet.start();
        }
        else{
            Toast.makeText(getContext(), "Authenticate to see Bets", Toast.LENGTH_SHORT).show();
        }
    }

    public void flush(){
        betRecyclerView.notifyDataSetChanged();
    }


    public void addItem(BaseType object){
        items.add((Bet) object);
    }

    public void removeItem(BaseType object) {
        items.remove((Bet) object);
        flush();
    }

    public ProgressBar getProgBar() {
        return progBar;
    }

}


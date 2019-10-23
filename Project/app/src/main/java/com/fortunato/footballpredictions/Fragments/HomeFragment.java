package com.fortunato.footballpredictions.Fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.Adapters.RecyclerViewAdapter;
import com.fortunato.footballpredictions.Network.ManagerNetwork;
import com.fortunato.footballpredictions.R;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String STATE_ITEMS = "items";
    private static final String RECYCLER_LAYOUT = "recLayout";
    private static final String NETFLAG = "netFlag";

    private List<String> items = null;
    private Boolean flagNetwork = true;
    private ProgressBar progBar = null;

    private RecyclerView recyclerView = null;
    private RecyclerViewAdapter recyclerViewAdapter = null;
    private Parcelable recyclerLayout = null;

    private ViewGroup container = null;

    public HomeFragment() { }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            flagNetwork = savedInstanceState.getBoolean(NETFLAG);
            items = (List<String>)savedInstanceState.get(STATE_ITEMS);
            recyclerLayout = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
        }
        if(items == null) {
            items = new LinkedList<String>();
            items.add("In attesa di ricevere i dati...");
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

        recyclerView = container.findViewById(R.id.ligueView);
        progBar = container.findViewById(R.id.progBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if(recyclerLayout != null){
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerLayout);
        }

        recyclerView.setHasFixedSize(false);

        recyclerViewAdapter = new RecyclerViewAdapter(items);
        recyclerView.setAdapter(recyclerViewAdapter);

        if(flagNetwork){
            progBar.setVisibility(View.VISIBLE);
            ManagerNetwork managerNetwork = new ManagerNetwork("leagues/season/2019", HomeFragment.this, getActivity());
            Thread tNet = new Thread(managerNetwork);
            tNet.start();
            flagNetwork = false;
            items.clear();
        }


    }

    public void addItem(String str){
        items.add(str);
    }

    public ProgressBar getProgBar() {
        return progBar;
    }

    public void flush(){
        recyclerViewAdapter.notifyDataSetChanged();
    }
}

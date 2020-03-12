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
import com.fortunato.footballpredictions.Adapters.FavoriteRecyclerView;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.League;
import com.fortunato.footballpredictions.DataStructures.SingletonCurrentFragment;
import com.fortunato.footballpredictions.DataStructures.SingletonFavorite;
import com.fortunato.footballpredictions.Networks.LoadImage;
import com.fortunato.footballpredictions.Networks.NetworkHome;
import com.fortunato.footballpredictions.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FavoriteFragment extends BaseFragment {

    private static final String STATE_ITEMS = "itemsFav";
    private static final String RECYCLER_LAYOUT = "recLayoutFav";
    private static final String NETFLAG = "netFlagFav";

    private List<BaseType> items = null;
    private Boolean flagNetwork = true;
    private ProgressBar progBar = null;

    private RecyclerView recyclerView = null;
    private FavoriteRecyclerView favoriteRecyclerView = null;
    private Parcelable recyclerLayout = null;

    private ViewGroup container = null;

    public FavoriteFragment() { }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            flagNetwork = savedInstanceState.getBoolean(NETFLAG);
            items = (List<BaseType>)savedInstanceState.get(STATE_ITEMS);
            recyclerLayout = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
        }
        if(items == null) {
            items = new LinkedList<BaseType>();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_ITEMS, (Serializable) items);
        if(recyclerView != null)
            outState.putParcelable(RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putBoolean(NETFLAG, flagNetwork);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        SingletonCurrentFragment.setCurrentId(this.getId());
        progBar = container.findViewById(R.id.progBarFav);
        recyclerView = container.findViewById(R.id.recViewFav);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if(recyclerLayout != null){
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerLayout);
        }

        recyclerView.setHasFixedSize(true);

        FirebaseAuth userAuth = FirebaseAuth.getInstance();
        progBar.setVisibility(View.VISIBLE);
        if(userAuth.getCurrentUser()!=null){
            final String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(currentFirebaseUser);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    LinkedList<BaseType> list = new LinkedList<BaseType>();;
                    if (progBar != null) {
                        progBar.setVisibility(View.GONE);
                    }
                    for(DataSnapshot data_s : dataSnapshot.getChildren()){
                        String dbId = data_s.getKey();
                        League l = data_s.getValue(League.class);
                        l.setDbId(dbId);
                        if(l.getLoadImage()!=null) l.getLoadImage().run();
                        else if(l.getUrlImg()!=null && !l.getUrlImg().equals("null")){
                            l.setLoadImage( new LoadImage(l.getUrlImg(), null, l));
                            l.getLoadImage().start();
                        }
                        list.add(l);
                    }
                    if(list.isEmpty()){
                        Toast.makeText(getContext(), "No favorite items", Toast.LENGTH_SHORT).show();
                    }
                    //Collections.reverse(list);
                    SingletonFavorite.setInstance(list);
                    favoriteRecyclerView = new FavoriteRecyclerView(list, FavoriteFragment.this);
                    recyclerView.setAdapter(favoriteRecyclerView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(getContext(), "Authenticate to see Favorites", Toast.LENGTH_SHORT).show();
            if (progBar != null) {
                progBar.setVisibility(View.GONE);
            }
        }

        if(MainActivity.NETWORK_CONNECTION == false){
            Toast.makeText(getContext(), "Network Connection is unavailable!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void addItem(BaseType object){
        items.add(object);
    }

    public ProgressBar getProgBar() {
        return progBar;
    }

    public void flush(){
        favoriteRecyclerView.notifyDataSetChanged();
    }

    public void modifyContent(String url, int requestType, String leagueId, String leagueName){
        MatchFragment mFragment = new MatchFragment(url, requestType, leagueId, leagueName);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mFragment)
                    .addToBackStack(null)
                    .commit();
    }

    public void onResume() {
        super.onResume();
        SingletonCurrentFragment.setCurrentId(this.getId());
    }
}

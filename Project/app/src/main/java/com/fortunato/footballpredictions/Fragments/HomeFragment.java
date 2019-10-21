package com.fortunato.footballpredictions.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.R;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView = null;
    private Context context = null;
    private ViewGroup container = null;

    public HomeFragment(Context passContext) {
        this.context = passContext;
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
        ProgressBar progressBar = container.findViewById(R.id.progBar);
        progressBar.setVisibility(View.VISIBLE);
        // Do Stuff
        progressBar.setVisibility(View.GONE);
    }
}

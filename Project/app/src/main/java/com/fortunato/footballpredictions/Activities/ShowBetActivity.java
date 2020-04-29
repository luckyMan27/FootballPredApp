package com.fortunato.footballpredictions.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fortunato.footballpredictions.Adapters.SelectedBetRecyclerView;
import com.fortunato.footballpredictions.DataStructures.Bet_Item;
import com.fortunato.footballpredictions.DataStructures.Wrapper;
import com.fortunato.footballpredictions.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ShowBetActivity extends AppCompatActivity {

     ArrayList<Bet_Item> bets;

    private ProgressBar progBar = null;
    private RecyclerView recyclerView = null;
    private SelectedBetRecyclerView sel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bet);

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("bets");
        String title = getIntent().getStringExtra("title");
        Wrapper obj = gson.fromJson(strObj, Wrapper.class);

        bets = obj.getList();

        recyclerView = findViewById(R.id.recViewSelected);
        TextView titleText = findViewById(R.id.selectedBet);
        titleText.setText(title);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        sel = new SelectedBetRecyclerView(bets);
        recyclerView.setAdapter(sel);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}

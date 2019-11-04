package com.fortunato.footballpredictions.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.Adapters.PredictionStatisticRecyclerView;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.Networks.NetworkPredStat;
import com.fortunato.footballpredictions.R;

import java.util.LinkedList;
import java.util.List;

public class PredictionStatisticActivity extends AppCompatActivity {

    private List<BaseType> items = null;
    private RecyclerView recyclerView = null;
    private PredictionStatisticRecyclerView recyclerViewAdp = null;

    private Boolean flagNetwork = true;
    private ProgressBar progBar = null;
    private String fixtureId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pred_stat);
        Intent intent = getIntent();
        fixtureId = intent.getStringExtra("fixture_id");
        setTitle(intent.getStringExtra("teams_match"));

        if(items == null) items = new LinkedList<>();

        TabHost tabHost = findViewById(R.id.tabPredStat);
        tabHost.setup();

        TabHost.TabSpec tSpec = tabHost.newTabSpec("Predictions");
        tSpec.setContent(R.id.tab1);
        tSpec.setIndicator("Predictions");
        tabHost.addTab(tSpec);
        tabPredictions();

        tSpec = tabHost.newTabSpec("Statistics");
        tSpec.setContent(R.id.tab2);
        tSpec.setIndicator("Statistics");
        tabHost.addTab(tSpec);
    }

    private void tabPredictions(){
        recyclerView = findViewById(R.id.recviewPred);
        progBar = findViewById(R.id.predStatprogBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewAdp = new PredictionStatisticRecyclerView(items);
        recyclerView.setAdapter(recyclerViewAdp);

        recyclerView.setHasFixedSize(true);

        if(flagNetwork){
            progBar.setVisibility(View.VISIBLE);
            NetworkPredStat netTools = new NetworkPredStat("predictions/"+fixtureId, 0, this);
            Thread tNet = new Thread(netTools);
            tNet.start();
            flagNetwork = false;
        }
    }

    public void addItem(BaseType obj){
        items.add(obj);
    }

    public void flush(){
        recyclerViewAdp.notifyDataSetChanged();
    }

    public ProgressBar getProgBar() {
        return progBar;
    }
}

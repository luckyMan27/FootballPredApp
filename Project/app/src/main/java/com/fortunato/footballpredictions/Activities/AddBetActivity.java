package com.fortunato.footballpredictions.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.fortunato.footballpredictions.Adapters.AddBetRecyclerView;
import com.fortunato.footballpredictions.DataStructures.Bet_Item;
import com.fortunato.footballpredictions.DataStructures.SingletonCurrentBet;
import com.fortunato.footballpredictions.Fragments.BetFragment;
import com.fortunato.footballpredictions.Networks.NetworkBackend;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fortunato.footballpredictions.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddBetActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AddBetRecyclerView add;
    private List<Bet_Item> bets;
    private EditText desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bet);

        recyclerView = findViewById(R.id.recViewAddBet);

        desc = findViewById(R.id.edit_description);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        bets = SingletonCurrentBet.getInstance();

        add = new AddBetRecyclerView(bets);
        recyclerView.setAdapter(add);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.NETWORK_CONNECTION){
                    Toast.makeText(AddBetActivity.this, "Network Connection is unavailable!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    Toast.makeText(AddBetActivity.this, "Please log in", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(SingletonCurrentBet.getInstance().isEmpty()){
                    Toast.makeText(AddBetActivity.this, "Please select Bets", Toast.LENGTH_SHORT).show();
                    return;
                }

                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
                onBackPressed();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            SingletonCurrentBet.clearInstance();
            add.notifyDataSetChanged();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            long millisec = System.currentTimeMillis();
            Date currentDate = new Date(millisec);
            DateFormat df = new SimpleDateFormat("dd/MM/yy");
            String date_string = df.format(currentDate);

            String d = desc.getText().toString();
            if(d.isEmpty()){
                d = "Missing description";
            }

            NetworkBackend net = new NetworkBackend(null, SingletonCurrentBet.getInstance() , 0, date_string, d, currentFirebaseUser, null, null);
            Thread tNet = new Thread(net);
            tNet.start();
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

    }



}

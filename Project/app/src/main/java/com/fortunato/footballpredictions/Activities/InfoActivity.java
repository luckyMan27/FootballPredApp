package com.fortunato.footballpredictions.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fortunato.footballpredictions.R;

import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    TextView desc1;
    TextView desc2;
    TextView desc3;
    String info1;
    String info2;
    String info3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        desc1 = findViewById(R.id.textInfoApp);
        desc2 = findViewById(R.id.textInfoApp2);
        desc3 = findViewById(R.id.textInfoApp3);

        info1 = "Football Prediction is an application dedicated to the football's world. " +
                "The main focus of our application are the football pools.\n" +
                "We offer the possibility to consult predictions over hundreds of different matches, " +
                "save bets informations and locate the different stadiums.\n ";

        info2 = "The application covers: \n\n" +
                "\u2022 +130 Countries\n" +
                "\u2022 +480 Leagues and Cups\n" +
                "\u2022 +1000 Teams\n" +
                "\u2022 Possibility to save bets\n" +
                "\u2022 Statium location\n";

        info3 = "Authors of the applcation: \n \n" +
                "\u2022 Author1 \n" +
                "\u2022 Author2";

        desc1.setText(info1);
        desc2.setText(info2);
        desc3.setText(info3);
    }
}

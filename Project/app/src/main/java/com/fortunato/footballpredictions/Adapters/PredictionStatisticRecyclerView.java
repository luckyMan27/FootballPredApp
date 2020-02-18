package com.fortunato.footballpredictions.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.Activities.PredictionStatisticActivity;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.Bet_Item;
import com.fortunato.footballpredictions.DataStructures.FixturePrediction;
import com.fortunato.footballpredictions.DataStructures.SingletonCurrentBet;
import com.fortunato.footballpredictions.R;

import java.util.List;

public class PredictionStatisticRecyclerView extends RecyclerView.Adapter<PredictionStatisticRecyclerView.ViewHolder> {

    private List<BaseType> list;

    private String team1;
    private String team2;

    private List<Bet_Item> bets;
    private Bet_Item it;

    private boolean flag_1 = false;
    private boolean flag_x = false;
    private boolean flag_2 = false;



    public PredictionStatisticRecyclerView(List<BaseType> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PredictionStatisticRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.prediction_tab, parent, false);
        return new PredictionStatisticRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionStatisticRecyclerView.ViewHolder holder, int position) {
        TextView tTitle = holder.titlePred;
        TextView tPred = holder.textPred;
        TextView tTitle1 = holder.titlePred1;
        TextView tPred1 = holder.textPred1;
        TextView tTitle2 = holder.titlePred2;
        TextView tPred2 = holder.textPred2;
        TextView tTitle3 = holder.titlePred3;
        TextView tPred3 = holder.textPred3;
        TextView tTitle4 = holder.titlePred4;
        TextView tPred4 = holder.textPred4;
        TextView tPred5 = holder.textPred5;
        ProgressBar progPred5 = holder.progPred5;
        TextView tPred6 = holder.textPred6;
        ProgressBar progPred6 = holder.progPred6;
        TextView tPred7 = holder.textPred7;
        ProgressBar progPred7 = holder.progPred7;

        final RadioButton btn_1 = holder.btn_1;
        final RadioButton btn_x = holder.btn_x;
        final RadioButton btn_2 = holder.btn_2;


        BaseType obj = list.get(position);
        if(obj instanceof FixturePrediction){
            FixturePrediction prediction = (FixturePrediction) obj;
            Log.d("Debug-predStatRecView", prediction.toString());
            tTitle.setText("Match Winner");
            tPred.setText((prediction.getMatchWinner().equals("1")) ? prediction.getHome().getTeamName() : prediction.getAway().getTeamName()) ;
            tTitle1.setText("Under/Over");
            tPred1.setText((!prediction.getUnderOver().equals("null") ? prediction.getUnderOver() : "no prediction"));
            tTitle2.setText("Goals Home");
            tPred2.setText(prediction.getGoalsHome());
            tTitle3.setText("Goals Away");
            tPred3.setText(prediction.getGoalsAway());
            tTitle4.setText("Advice");
            tPred4.setText(prediction.getAdvice());
            tPred5.setText(prediction.getWinPercHome());
            int i = Integer.parseInt(prediction.getWinPercHome().replace("%", ""));
            progPred5.setProgress(i);
            tPred6.setText(prediction.getWinPercDraws());
            i = Integer.parseInt(prediction.getWinPercDraws().replace("%", ""));
            progPred6.setProgress(i);
            tPred7.setText(prediction.getWinPercAway());
            i = Integer.parseInt(prediction.getWinPercAway().replace("%", ""));
            progPred7.setProgress(i);

            team1 = prediction.getHome().getTeamName();
            team2 = prediction.getAway().getTeamName();

        }


        btn_1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag_1){
                    flag_1 = false;
                    btn_1.setChecked(false);
                    remove_elem_list(team1, team2, "1");
                }
                else{
                    flag_1 = true;
                    btn_1.setChecked(true);
                    flag_x = false;
                    flag_2 = false;
                    add_elem_list(team1, team2, "1", view);
                }
            }

        });
        btn_x.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag_x){
                    flag_x = false;
                    btn_x.setChecked(false);
                    remove_elem_list(team1, team2, "X");
                }
                else{
                    flag_x = true;
                    btn_x.setChecked(true);
                    flag_1 = false;
                    flag_2 = false;
                    add_elem_list(team1, team2, "X", view);
                }
            }
        });
        btn_2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag_2){
                    flag_2 = false;
                    btn_2.setChecked(false);
                    //Toast.makeText(view.getContext(), "Falso", Toast.LENGTH_SHORT).show();
                    remove_elem_list(team1, team2, "2");
                }
                else{
                    flag_2 = true;
                    btn_2.setChecked(true);
                    //Toast.makeText(view.getContext(), "Vero", Toast.LENGTH_SHORT).show();
                    flag_1 = false;
                    flag_x = false;
                    add_elem_list(team1, team2, "2", view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titlePred;
        private TextView textPred;
        private TextView titlePred1;
        private TextView textPred1;
        private TextView titlePred2;
        private TextView textPred2;
        private TextView titlePred3;
        private TextView textPred3;
        private TextView titlePred4;
        private TextView textPred4;
        private TextView textPred5;
        private ProgressBar progPred5;
        private TextView textPred6;
        private ProgressBar progPred6;
        private TextView textPred7;
        private ProgressBar progPred7;

        private RadioButton btn_1;
        private RadioButton btn_x;
        private RadioButton btn_2;




        public ViewHolder(@NonNull View view) {
            super(view);
            view.findViewById(R.id.layoutLinearPred).setOnClickListener(this);
            this.titlePred = view.findViewById(R.id.titlePred);
            this.textPred = view.findViewById(R.id.textPred);
            this.titlePred1 = view.findViewById(R.id.titlePred1);
            this.textPred1 = view.findViewById(R.id.textPred1);
            this.titlePred2 = view.findViewById(R.id.titlePred2);
            this.textPred2 = view.findViewById(R.id.textPred2);
            this.titlePred3 = view.findViewById(R.id.titlePred3);
            this.textPred3 = view.findViewById(R.id.textPred3);
            this.titlePred4 = view.findViewById(R.id.titlePred4);
            this.textPred4 = view.findViewById(R.id.textPred4);
            this.textPred5 = view.findViewById(R.id.textPred5);
            this.progPred5 = view.findViewById(R.id.progressBar5);
            this.textPred6 = view.findViewById(R.id.textPred6);
            this.progPred6 = view.findViewById(R.id.progressBar6);
            this.textPred7 = view.findViewById(R.id.textPred7);
            this.progPred7 = view.findViewById(R.id.progressBar7);

            this.btn_1 = view.findViewById(R.id.radio_1);
            this.btn_x = view.findViewById(R.id.radio_x);
            this.btn_2 = view.findViewById(R.id.radio_2);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Object obj = list.get(position);

            /*MatchFragment matchFragment;

            if(fragment instanceof MatchFragment){
                matchFragment = (MatchFragment) fragment;
                if(obj instanceof LeagueFixture){
                    //
                }
            }*/
        }
    }

    public void add_elem_list(String t1, String t2, String value, View view){
        bets = SingletonCurrentBet.getInstance();
        it = new Bet_Item(t1, t2, value);
        if(bets.contains(it)) {
            int index = bets.indexOf(it);
            Bet_Item it2 = bets.get(index);
            if (it.getValue() == it2.getValue()) {
                return;
            } else {
                bets.remove(it2);
                bets.add(it);
            }
        }
        else {
            bets.add(it);
        }
    }
    public void remove_elem_list(String t1, String t2, String value){
        bets = SingletonCurrentBet.getInstance();
        it = new Bet_Item(t1, t2, value);
        if(bets.contains(it)) {
            int index = bets.indexOf(it);
            Bet_Item it2 = bets.get(index);
            if (it.getValue() == it2.getValue()) {
                bets.remove(it2);
            }
        }
    }
}
package com.fortunato.footballpredictions.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.Activities.PredictionActivity;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.Bet_Item;
import com.fortunato.footballpredictions.DataStructures.FixturePrediction;
import com.fortunato.footballpredictions.DataStructures.SingletonCurrentBet;
import com.fortunato.footballpredictions.R;

import java.util.List;

public class PredictionRecyclerView extends RecyclerView.Adapter<PredictionRecyclerView.ViewHolder> {

    private List<BaseType> list;

    private String team1;
    private String team2;

    private List<Bet_Item> bets;
    private Bet_Item it;

    private String league;

    private boolean flag_1 = false;
    private boolean flag_x = false;
    private boolean flag_2 = false;

    private PredictionActivity parent;



    public PredictionRecyclerView(List<BaseType> list, PredictionActivity parent, String league) {

        this.list = list;
        this.parent = parent;
        this.league = league;
    }

    @NonNull
    @Override
    public PredictionRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.prediction_tab, parent, false);
        return new PredictionRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionRecyclerView.ViewHolder holder, int position) {
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

        final RadioGroup gr= holder.gr;

        Button stadio_btn = holder.stadio_btn;

        BaseType obj = list.get(position);
        if(obj instanceof FixturePrediction){
            FixturePrediction prediction = (FixturePrediction) obj;
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

            int res = verify_elem(team1, team2, "1", league);

            if(res == 1){
                gr.clearCheck();
                btn_1.setChecked(true);
            }
            else if(res == 2){
                gr.clearCheck();
                btn_2.setChecked(true);
            }
            else if(res == 3){
                gr.clearCheck();
                btn_x.setChecked(true);
            }

        }


        btn_1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag_1){
                    flag_1 = false;
                    gr.clearCheck();
                    btn_1.setChecked(false);
                    remove_elem_list(team1, team2, "1", league);
                }
                else{
                    flag_1 = true;
                    gr.clearCheck();
                    btn_1.setChecked(true);
                    flag_x = false;
                    flag_2 = false;
                    add_elem_list(team1, team2, "1", view, league);
                }
            }

        });
        btn_x.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag_x){
                    flag_x = false;
                    gr.clearCheck();
                    btn_x.setChecked(false);
                    remove_elem_list(team1, team2, "X", league);
                }
                else{
                    flag_x = true;
                    gr.clearCheck();
                    btn_x.setChecked(true);
                    flag_1 = false;
                    flag_2 = false;
                    add_elem_list(team1, team2, "X", view, league);
                }
            }
        });
        btn_2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag_2){
                    flag_2 = false;
                    gr.clearCheck();
                    btn_2.setChecked(false);
                    remove_elem_list(team1, team2, "2", league);
                }
                else{
                    flag_2 = true;
                    gr.clearCheck();
                    btn_2.setChecked(true);
                    flag_1 = false;
                    flag_x = false;
                    add_elem_list(team1, team2, "2", view, league);
                }
            }
        });

        stadio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.modifyContent();
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
        private Button stadio_btn;

        private RadioGroup gr;




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

            this.stadio_btn = view.findViewById(R.id.stadium);

            this.gr = view.findViewById(R.id.radio_group);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Object obj = list.get(position);
        }
    }

    public void add_elem_list(String t1, String t2, String value, View view, String league){
        bets = SingletonCurrentBet.getInstance();
        it = new Bet_Item(t1, t2, value, league);
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
    public void remove_elem_list(String t1, String t2, String value, String league){
        bets = SingletonCurrentBet.getInstance();
        it = new Bet_Item(t1, t2, value, league);
        if(bets.contains(it)) {
            int index = bets.indexOf(it);
            Bet_Item it2 = bets.get(index);
            if (it.getValue() == it2.getValue()) {
                bets.remove(it2);
            }
        }
    }

    public int verify_elem(String t1, String t2, String value, String league){
        bets = SingletonCurrentBet.getInstance();
        it = new Bet_Item(t1, t2, value, league);
        if(bets.contains(it)){
            String value_of = bets.get(bets.indexOf(it)).getValue();
            if(value_of == "1"){
                return 1;
            }
            else if(value_of == "2"){
                return 2;
            }
            else if(value_of == "X"){
                return 3;
            }
        }
        return 0;
    }
}
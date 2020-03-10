package com.fortunato.footballpredictions.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.Activities.PredictionActivity;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.LeagueFixture;
import com.fortunato.footballpredictions.R;

import java.util.List;

public class MatchRecyclerView extends RecyclerView.Adapter<MatchRecyclerView.ViewHolder> {

    private List<LeagueFixture> list;
    private String leagueName;

    public MatchRecyclerView(List<LeagueFixture> list, String leagueName) {

        this.list = list;
        this.leagueName = leagueName;
    }

    @NonNull
    @Override
    public MatchRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recview_match, parent, false);

        return new MatchRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchRecyclerView.ViewHolder holder, int position) {
        TextView tHomeView = holder.homeText;
        TextView tAwayView = holder.awayText;
        ImageView iHomeView = holder.homeImg;
        ImageView iAwayView = holder.awayImg;
        TextView result = holder.resultText;
        TextView time = holder.time;

        BaseType obj = list.get(position);
        if(obj instanceof LeagueFixture){
            LeagueFixture match = (LeagueFixture) obj;
            tHomeView.setText(match.gethTeam_name());
            tAwayView.setText(match.getaTeam_name());
            iHomeView.setImageBitmap(match.gethLogo());
            iAwayView.setImageBitmap(match.getaLogo());
            if(match.getStatsShort().equals("FT")){
                result.setText(match.getsFulltime());
                time.setText(match.getStatsShort());
            } else if(match.getStatsShort().equals("HT") ||
                        match.getStatsShort().equals("1H") ||
                        match.getStatsShort().equals("2H") ||
                        match.getStatsShort().equals("EH") ||
                        match.getStatsShort().equals("P")){
                result.setText(match.getGoalsHomeTeam()+" - "+match.getGoalsAwayTeam());
                result.setTextColor(Color.RED);
                time.setText(match.getElapsed_time());
            } else {
                if(!match.getStatsShort().equals("NS"))
                result.setText(match.getStatsShort());
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView homeText;
        private TextView awayText;
        private ImageView homeImg;
        private ImageView awayImg;
        private TextView resultText;
        private TextView time;

        private ViewHolder(@NonNull View view) {
            super(view);
            view.findViewById(R.id.layoutRelMatch).setOnClickListener(this);
            this.homeText = view.findViewById(R.id.homeText);
            this.awayText = view.findViewById(R.id.awayText);
            this.homeImg = view.findViewById(R.id.homeImg);
            this.awayImg = view.findViewById(R.id.awayImg);
            this.resultText = view.findViewById(R.id.textSpace);
            this.time = view.findViewById(R.id.textTime);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Object obj = list.get(position);

            if(obj instanceof LeagueFixture){
                LeagueFixture fixture = (LeagueFixture) obj;
                Intent intent = new Intent(v.getContext(), PredictionActivity.class);
                intent.putExtra("fixture_id", fixture.getFixture_id());
                intent.putExtra("teams_match", fixture.gethTeam_name()+" - "+fixture.getaTeam_name());
                intent.putExtra("home_id", fixture.gethTeam_id());
                intent.putExtra("venue", fixture.getVenue());
                intent.putExtra("league", leagueName);
                v.getContext().startActivity(intent);
            }
        }
    }
}
package com.fortunato.footballpredictions.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.Activities.PredictionStatisticActivity;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.LeagueFixture;
import com.fortunato.footballpredictions.Fragments.MatchFragment;
import com.fortunato.footballpredictions.R;

import java.util.List;

public class MatchRecyclerView extends RecyclerView.Adapter<MatchRecyclerView.ViewHolder> {

    private List<BaseType> list;
    private Fragment fragment;

    public MatchRecyclerView(List<BaseType> list, Fragment fragment) {
        this.list = list;
        this.fragment = fragment;
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

        BaseType obj = list.get(position);
        if(obj instanceof LeagueFixture){
            LeagueFixture match = (LeagueFixture) obj;
            Log.d("Debug-MatchRecView", match.toString());
            tHomeView.setText(match.gethTeam_name());
            tAwayView.setText(match.getaTeam_name());
            iHomeView.setImageBitmap(match.gethLogo());
            iAwayView.setImageBitmap(match.getaLogo());
            if(match.getStatsShort().equals("FT")){
                result.setText(match.getsFulltime());
            } else if(match.getStatsShort().equals("HT") ||
                        match.getStatsShort().equals("1H") ||
                        match.getStatsShort().equals("2H") ){
                result.setText(match.getsHalftime());
                result.setTextColor(Color.RED);
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

        public ViewHolder(@NonNull View view) {
            super(view);
            view.findViewById(R.id.layoutRelMatch).setOnClickListener(this);
            this.homeText = view.findViewById(R.id.homeText);
            this.awayText = view.findViewById(R.id.awayText);
            this.homeImg = view.findViewById(R.id.homeImg);
            this.awayImg = view.findViewById(R.id.awayImg);
            this.resultText = view.findViewById(R.id.textSpace);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Object obj = list.get(position);

            if(obj instanceof LeagueFixture){
                LeagueFixture fixture = (LeagueFixture) obj;
                Intent intent = new Intent(v.getContext(), PredictionStatisticActivity.class);
                intent.putExtra("fixture_id", fixture.getFixture_id());
                v.getContext().startActivity(intent);
            }
        }
    }
}
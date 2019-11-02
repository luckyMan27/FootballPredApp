package com.fortunato.footballpredictions.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.Activities.PredictionStatisticActivity;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.League;
import com.fortunato.footballpredictions.DataStructures.LeagueFixture;
import com.fortunato.footballpredictions.DataStructures.SingletonFavorite;
import com.fortunato.footballpredictions.R;

import java.util.List;

public class FavoriteRecyclerView extends RecyclerView.Adapter<FavoriteRecyclerView.ViewHolder> {

    private List<BaseType> list;
    private List<BaseType> matchs;

    public FavoriteRecyclerView(List<BaseType> listMatch) {
        list = SingletonFavorite.getInstance();
        this.matchs = listMatch;
    }

    @NonNull
    @Override
    public FavoriteRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recview_favorite, parent, false);

        return new FavoriteRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRecyclerView.ViewHolder holder, int position) {
        TextView tHomeView = holder.homeText;
        TextView tAwayView = holder.awayText;
        ImageView iHomeView = holder.homeImg;
        ImageView iAwayView = holder.awayImg;
        TextView result = holder.resultText;
        TextView time = holder.time;
        TextView league = holder.titleLeague;
        ToggleButton favB = holder.favB;

        BaseType obj = list.get(position);
        if(obj instanceof LeagueFixture){
            LeagueFixture match = (LeagueFixture) obj;
            if(position == 0) league.setText(match.getLeague_id());
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
            setFavorite(favB, match.getLeague_id());
        }
    }

    private void setFavorite(ToggleButton favB, String leagueId) {
        LeagueFixture app;
        for(BaseType obj : matchs){
            if(obj instanceof LeagueFixture){
                app = (LeagueFixture) obj;
                if(app.getLeague_id().equals(leagueId)){
                    favB.setChecked(true);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleLeague;
        private TextView homeText;
        private TextView awayText;
        private ImageView homeImg;
        private ImageView awayImg;
        private TextView resultText;
        private TextView time;
        private ToggleButton favB;

        public ViewHolder(@NonNull View view) {
            super(view);
            view.findViewById(R.id.layoutRelMatch).setOnClickListener(this);
            this.titleLeague = view.findViewById(R.id.LeagueText);
            this.homeText = view.findViewById(R.id.homeText);
            this.awayText = view.findViewById(R.id.awayText);
            this.homeImg = view.findViewById(R.id.homeImg);
            this.awayImg = view.findViewById(R.id.awayImg);
            this.resultText = view.findViewById(R.id.textSpace);
            this.time = view.findViewById(R.id.textTime);
            this.favB = view.findViewById(R.id.favToogle);
            setListnerForFav();
        }

        private void setListnerForFav() {
            favB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Object obj = list.get(position);

                    List<BaseType> favoriteList = SingletonFavorite.getInstance();

                    if(obj instanceof League) {
                        League fixture = (League) obj;
                        if(favoriteList!=null && favoriteList.contains(fixture))
                            favoriteList.remove(fixture);
                        else favoriteList.add(fixture);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Object obj = list.get(position);

            if(obj instanceof LeagueFixture){
                LeagueFixture fixture = (LeagueFixture) obj;
                Intent intent = new Intent(v.getContext(), PredictionStatisticActivity.class);
                intent.putExtra("fixture_id", fixture.getFixture_id());
                intent.putExtra("teams_match", fixture.gethTeam_name()+" - "+fixture.getaTeam_name());
                v.getContext().startActivity(intent);
            }
        }
    }
}
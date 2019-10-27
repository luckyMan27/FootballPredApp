package com.fortunato.footballpredictions.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.League;
import com.fortunato.footballpredictions.Fragments.LeagueFragment;
import com.fortunato.footballpredictions.R;

import java.util.List;

public class LeagueRecyclerView extends RecyclerView.Adapter<LeagueRecyclerView.ViewHolder> {

    private List<BaseType> list;
    private Fragment fragment;

    public LeagueRecyclerView(List<BaseType> list, Fragment fragment) {
        this.list = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public LeagueRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recview_country_league, parent, false);
        LeagueRecyclerView.ViewHolder viewHolder = new LeagueRecyclerView.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueRecyclerView.ViewHolder holder, int position) {
        TextView tView = holder.item;
        ImageView iView = holder.img_item;

        BaseType obj = list.get(position);
        if(obj instanceof League){
            League league = (League) obj;
            tView.setText(league.getName());
            iView.setImageBitmap(league.getLogo());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView item;
        private ImageView img_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.item = itemView.findViewById(R.id.item);
            this.item.setOnClickListener(this);
            this.img_item = itemView.findViewById(R.id.itemImg);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Object obj = list.get(position);
            
            LeagueFragment leagueFragment;

            if(fragment instanceof LeagueFragment){
                leagueFragment = (LeagueFragment) fragment;
                if(obj instanceof League){
                    League league = (League) obj;
                    leagueFragment.modifyContent("", 2, league.getLeague_id());
                }
            }
        }
    }
}

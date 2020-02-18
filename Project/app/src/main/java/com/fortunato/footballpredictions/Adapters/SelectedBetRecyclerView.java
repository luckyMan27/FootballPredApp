package com.fortunato.footballpredictions.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.Bet_Item;
import com.fortunato.footballpredictions.R;

import java.util.List;

public class SelectedBetRecyclerView extends RecyclerView.Adapter<SelectedBetRecyclerView.ViewHolder>{
    private List<Bet_Item> list;

    public SelectedBetRecyclerView(List<Bet_Item> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SelectedBetRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recview_selected_bet, parent, false);
        return new SelectedBetRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView home = holder.home;
        TextView away = holder.away;
        TextView value = holder.value;


        BaseType obj = list.get(position);
        if(obj instanceof Bet_Item){
            Bet_Item bet = (Bet_Item) obj;
            home.setText(bet.getHome());
            away.setText(bet.getAway());
            value.setText(bet.getValue());
            if(position %2 == 1)
            {
                home.setBackgroundColor(Color.parseColor("#FFFFFF"));
                away.setBackgroundColor(Color.parseColor("#FFFFFF"));
                value.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            else
            {
                home.setBackgroundColor(Color.parseColor("#D7D7D7"));
                away.setBackgroundColor(Color.parseColor("#D7D7D7"));
                value.setBackgroundColor(Color.parseColor("#D7D7D7"));
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView home;
        private TextView away;
        private TextView value;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.home = itemView.findViewById(R.id.home_bet);
            this.away = itemView.findViewById(R.id.away_bet);
            this.value = itemView.findViewById(R.id.value_bet);

        }


    }
}


package com.fortunato.footballpredictions.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.Bet_Item;
import com.fortunato.footballpredictions.DataStructures.SingletonCurrentBet;
import com.fortunato.footballpredictions.R;

import java.util.List;

public class AddBetRecyclerView extends RecyclerView.Adapter<AddBetRecyclerView.ViewHolder>{

    private List<Bet_Item> list;

    public AddBetRecyclerView(List<Bet_Item> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AddBetRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recview_add_bet, parent, false);
        return new AddBetRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        TextView home = holder.home;
        TextView away = holder.away;
        TextView value = holder.value;
        TextView league = holder.league;
        Button remove = holder.remove;
        final int index = position;


        BaseType obj = list.get(position);
        if(obj instanceof Bet_Item){
            Bet_Item bet = (Bet_Item) obj;
            home.setText(bet.getHome());
            away.setText(bet.getAway());
            value.setText(bet.getValue());
            league.setText(bet.getLeague());
        }

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingletonCurrentBet.getInstance().remove(index);
                notifyItemRemoved(index);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView home;
        private TextView away;
        private TextView value;
        private TextView league;
        private Button remove;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.home = itemView.findViewById(R.id.home_bet_add);
            this.away = itemView.findViewById(R.id.away_bet_add);
            this.value = itemView.findViewById(R.id.value_bet_add);
            this.league = itemView.findViewById(R.id.league_bet_add);
            this.remove = itemView.findViewById(R.id.button_remove_bet);
        }
    }
}

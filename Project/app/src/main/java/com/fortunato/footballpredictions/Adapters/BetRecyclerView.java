package com.fortunato.footballpredictions.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.Activities.MainActivity;
import com.fortunato.footballpredictions.Activities.ShowBetActivity;
import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.Bet;
import com.fortunato.footballpredictions.DataStructures.Bet_Item;
import com.fortunato.footballpredictions.DataStructures.Wrapper;
import com.fortunato.footballpredictions.Fragments.BetFragment;
import com.fortunato.footballpredictions.Networks.NetworkBackend;
import com.fortunato.footballpredictions.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BetRecyclerView extends RecyclerView.Adapter<BetRecyclerView.ViewHolder>{

    private List<Bet> list;
    private BetFragment fragment;

    public BetRecyclerView(List<Bet> list, BetFragment fragment) {
        this.list = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public BetRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recview_bet, parent, false);
        return new BetRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView tView = holder.item;
        TextView desc = holder.desc;

        BaseType obj = list.get(position);
        if(obj instanceof Bet){
            Bet bet = (Bet) obj;
            tView.setText(bet.getDate());
            desc.setText(bet.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView item;
        private Button btn;
        private TextView desc;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.item = itemView.findViewById(R.id.bet_id);
            this.item.setOnClickListener(this);
            this.btn = itemView.findViewById(R.id.button_remove);
            this.desc = itemView.findViewById(R.id.desc_bet);
            this.desc.setOnClickListener(this);
            setListnerForButton();
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Object obj = list.get(position);

            if(obj instanceof Bet) {
                Bet bet = (Bet) obj;
                Wrapper w = new Wrapper();
                w.setWrapperList((ArrayList<Bet_Item>) bet.getList());
                Gson j = new Gson();
                Intent intent = new Intent(v.getContext(), ShowBetActivity.class);
                intent.putExtra("bets", j.toJson(w));
                intent.putExtra("title", bet.getDescription());
                v.getContext().startActivity(intent);
            }


        }
        public void setListnerForButton(){
            btn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!MainActivity.NETWORK_CONNECTION){
                        Toast.makeText(fragment.getContext(), "Network Connection is unavailable!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int position = getAdapterPosition();
                    Object obj = list.get(position);

                    if(obj instanceof Bet) {
                        Bet bet = (Bet) obj;
                        String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        fragment.removeItem(bet);

                        NetworkBackend net = new NetworkBackend(fragment, null , 2, null, null, currentFirebaseUser, fragment.getActivity(), bet.getBet_id());
                        Thread tNet = new Thread(net);
                        tNet.start();
                    }
                }
            });
        }
    }
}

package com.fortunato.footballpredictions.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.League;
import com.fortunato.footballpredictions.DataStructures.SingletonFavorite;
import com.fortunato.footballpredictions.Fragments.LeagueFragment;
import com.fortunato.footballpredictions.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LeagueRecyclerView extends RecyclerView.Adapter<LeagueRecyclerView.ViewHolder> {

    private List<League> list;
    private Fragment fragment;

    public LeagueRecyclerView(List<League> list, Fragment fragment) {
        this.list = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public LeagueRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recview_league, parent, false);
        return new LeagueRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueRecyclerView.ViewHolder holder, int position) {
        TextView tView = holder.item;
        ImageView iView = holder.img_item;
        ToggleButton favB = holder.favB;

        BaseType obj = list.get(position);
        if(obj instanceof League){
            League league = (League) obj;
            tView.setText(league.getName());
            try {
                if(league.getLoadImage()!=null){
                    league.getLoadImage().join();
                    iView.setImageBitmap(league.getLogo());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(SingletonFavorite.getInstance().contains(league)) favB.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView item;
        private ImageView img_item;
        private ToggleButton favB;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.item = itemView.findViewById(R.id.item);
            this.item.setOnClickListener(this);
            this.img_item = itemView.findViewById(R.id.itemImg);
            this.favB = itemView.findViewById(R.id.favToogleLeague);
            setListnerForFav();
        }

        private void setListnerForFav() {
            favB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Object obj = list.get(position);

                    List<BaseType> favoriteList = SingletonFavorite.getInstance();
                    /*
                    if(obj instanceof League) {
                        League fixture = (League) obj;
                        if(favoriteList!=null && favoriteList.contains(fixture))
                            favoriteList.remove(fixture);
                        else favoriteList.add(fixture);
                    }

                     */
                    if (obj instanceof League) {
                        League fixture = (League) obj;
                        if (favoriteList != null && favoriteList.contains(fixture)) {
                            //remove

                            String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(currentFirebaseUser).child(fixture.getLeague_id());
                            ref.removeValue();


                        } else {
                            //add
                            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                                Toast.makeText(fragment.getContext(), "Please log in", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            long millisec = System.currentTimeMillis();
                            DatabaseReference entry = database.getReference("users").child(currentFirebaseUser).child(String.valueOf(millisec));

                            DatabaseReference id = entry.child("league_id");
                            id.setValue(fixture.getLeague_id());

                            DatabaseReference name = entry.child("name");
                            name.setValue(fixture.getName());

                            DatabaseReference urlImg = entry.child("urlImg");
                            urlImg.setValue(fixture.getUrlImg());

                            DatabaseReference country = entry.child("country");
                            country.setValue(fixture.getCountry());

                            DatabaseReference cov = entry.child("coverage");
                            DatabaseReference pred = cov.child("prediction");
                            pred.setValue(fixture.getPredictions());
                        }

                    }
                }
            });
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
                    leagueFragment.modifyContent("", 2, league.getLeague_id(), league.getName());
                }
            }
        }
    }
}

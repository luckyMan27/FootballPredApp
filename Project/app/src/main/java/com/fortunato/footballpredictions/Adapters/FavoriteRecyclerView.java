package com.fortunato.footballpredictions.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.League;
import com.fortunato.footballpredictions.DataStructures.SingletonFavorite;
import com.fortunato.footballpredictions.Fragments.FavoriteFragment;
import com.fortunato.footballpredictions.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavoriteRecyclerView extends RecyclerView.Adapter<FavoriteRecyclerView.ViewHolder> {

    private List<BaseType> list;
    private Fragment fragment;

    public FavoriteRecyclerView(List<BaseType> list, Fragment fragment) {
        this.list = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public FavoriteRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recview_league, parent, false);
        return new FavoriteRecyclerView.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRecyclerView.ViewHolder holder, int position) {
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

                    if (obj instanceof League) {
                        League fixture = (League) obj;
                        if (favoriteList != null && favoriteList.contains(fixture)) {
                            //remove
                            String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(currentFirebaseUser).child(fixture.getDbId());
                            ref.removeValue();

                            favoriteList.remove(fixture);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Object obj = list.get(position);

            FavoriteFragment favoriteFragment;

            if(fragment instanceof FavoriteFragment){
                favoriteFragment = (FavoriteFragment) fragment;
                if(obj instanceof League){
                    League league = (League) obj;
                    favoriteFragment.modifyContent("", 2, league.getLeague_id(), league.getName());
                }
            }
        }
    }
}
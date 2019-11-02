package com.fortunato.footballpredictions.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fortunato.footballpredictions.DataStructures.BaseType;
import com.fortunato.footballpredictions.DataStructures.Country;
import com.fortunato.footballpredictions.Fragments.HomeFragment;
import com.fortunato.footballpredictions.R;

import java.util.Calendar;
import java.util.List;

public class CountryRecyclerView extends RecyclerView.Adapter<CountryRecyclerView.ViewHolder> {

    private List<BaseType> list;
    private HomeFragment fragment;

    public CountryRecyclerView(List<BaseType> list, HomeFragment fragment) {
        this.list = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recview_country, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView tView = holder.item;
        ImageView iView = holder.img_item;

        BaseType obj = list.get(position);
        if(obj instanceof Country){
            Country country = (Country) obj;
            tView.setText(country.getCountry());
            try {
                if(country.getLoadImage()!=null){
                    country.getLoadImage().join();
                    iView.setImageBitmap(country.getflag());
                } else if(country.getCountry().equals("World")){
                    iView.setImageResource(R.drawable.world_flags_android);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

            if(obj instanceof Country){
                Country country = (Country) obj;
                String url = "leagues/country/"+country.getCountry()
                        +"/"+Calendar.getInstance().get(Calendar.YEAR);
                fragment.modifyContent(url, 1, "");

            }
        }
    }
}

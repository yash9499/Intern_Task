package com.example.pablo.mycarpet;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

/**
 * Created by pablo on 3/6/18.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private Context mContext;
    private List<Worldpopulation> country_list;
    final private ListItemClickListener mOnClickListener;

    public CountryAdapter(Context mContext, List<Worldpopulation> worldpopulations, ListItemClickListener listener) {
        this.country_list = worldpopulations;
        this.mContext = mContext;
        mOnClickListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex) throws IOException;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Worldpopulation item = country_list.get(position);
        holder.country_name.setText(item.getCountry());
        holder.rank.setText(item.getRank().toString());
        holder.population.setText(item.getPopulation().toString());
        Glide.with(mContext).load(Uri.parse(item.getFlag())).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return country_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView rank;
        TextView population;
        TextView country_name;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumbnail);
            rank = itemView.findViewById(R.id.edit_rank);
            population = itemView.findViewById(R.id.edit_country_population);
            country_name = itemView.findViewById(R.id.country_name);
            itemView.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            try {
                mOnClickListener.onListItemClick(clickedPosition);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

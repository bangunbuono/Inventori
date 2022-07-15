package com.example.inventori.Adapter.RVAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventori.R;
import com.example.inventori.model.StatModel;
import com.example.inventori.model.StocksModel;

import java.util.List;

public class AdapterStat extends RecyclerView.Adapter<AdapterStat.ViewHolder>{
    Context context;
    List<StatModel> list;

    public AdapterStat(Context context, List<StatModel> objects){
        this.context = context;
        list = objects;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvSatuanStat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSatuanStat = itemView.findViewById(R.id.tvSatuanStat);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.satuan_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        holder.itemView.setTag(list.get(position));
        holder.tvSatuanStat.setText(list.get(position).getSatuan());
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

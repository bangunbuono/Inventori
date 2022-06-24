package com.example.inventori.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inventori.R;
import com.example.inventori.model.MenuModel;

import java.util.List;

public class AdapterUsageAuto extends ArrayAdapter<MenuModel> {
    Context context;
    List<MenuModel> list;

    public AdapterUsageAuto(Context context, List<MenuModel> objects) {
        super(context, R.layout.usage_auto_row,objects);

        this.context = context;
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.usage_auto_row, parent, false);

        TextView tvProduct = convertView.findViewById(R.id.tvProduct);
        TextView tvQty = convertView.findViewById(R.id.tvQty);
        Button btnMin = convertView.findViewById(R.id.btnMin);
        Button btnPlus = convertView.findViewById(R.id.btnPlus);

        tvProduct.setText(list.get(position).getMenu());
        tvQty.setText(list.get(position).getQty()+"");
        int qty = list.get(position).getQty();

        btnMin.setOnClickListener(view -> {
            if(qty != 0){
                list.get(position).setQty(qty-1);
                tvQty.setText(list.get(position).getQty()+"");
            }
            notifyDataSetChanged();
        });

        btnPlus.setOnClickListener(view -> {
            list.get(position).setQty(qty+1);
            tvQty.setText(list.get(position).getQty()+"");
            notifyDataSetChanged();
        });
        return convertView;
    }
}

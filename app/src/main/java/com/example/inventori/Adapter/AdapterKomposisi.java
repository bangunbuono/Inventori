package com.example.inventori.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inventori.R;
import com.example.inventori.model.KomposisiModel;

import java.util.List;

public class AdapterKomposisi extends ArrayAdapter<KomposisiModel> {
    Context context;
    private List<KomposisiModel> komposisiModels;
    EditText etBahan,etJumlah,etSatuan, etIdBahan;

    public AdapterKomposisi(Context context, List<KomposisiModel> objects) {
        super(context, R.layout.komposisi_row,objects);

        this.context = context;
        komposisiModels = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.komposisi_row, parent, false);

        etIdBahan = convertView.findViewById(R.id.etIdBahan);
        etBahan = convertView.findViewById(R.id.etBahan);
        etJumlah = convertView.findViewById(R.id.etJumlah);
        etSatuan = convertView.findViewById(R.id.etSatuan);

        etIdBahan.setText(komposisiModels.get(position).getId()+"");
        etBahan.setText(komposisiModels.get(position).getKomposisi());
        etJumlah.setText(komposisiModels.get(position).getJumlah()+"");
        etSatuan.setText(komposisiModels.get(position).getSatuan());

        return convertView;
    }
}

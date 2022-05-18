package com.example.inventori.Activity.Usage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.inventori.API.APIRequestStock;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Activity.Restock.InventRestock;
import com.example.inventori.Adapter.AdapterSpinnerRestock;
import com.example.inventori.R;
import com.example.inventori.model.ResponseModel;
import com.example.inventori.model.RestockModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsageManualFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsageManualFrag extends Fragment {

    Spinner spinRestock;
    ArrayList<RestockModel> restockList = new ArrayList<>();
    RequestQueue requestQueue;
    TextView tvRestockSatuan;
    EditText etRestockJumlah;
    AdapterSpinnerRestock adapterRestock;
    Button btnCollectRestock;
    int id, jumlah;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsageManualFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsageManualFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static UsageManualFrag newInstance(String param1, String param2) {
        UsageManualFrag fragment = new UsageManualFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usage_manual, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());

        spinRestock = view.findViewById(R.id.spinnerManual);
        tvRestockSatuan = view.findViewById(R.id.tvManual);
        btnCollectRestock = view.findViewById(R.id.btnManual);
        etRestockJumlah = view.findViewById(R.id.etManual);

        String URL = "http://10.0.2.2/notif/restocktest.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("stocks");
                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String bahan = jsonObject.optString("bahan_baku");
                        String satuan = jsonObject.optString("satuan");
                        id = jsonObject.optInt("id");
                        restockList.add(new RestockModel(id, bahan, satuan));
                    }
                    adapterRestock = new AdapterSpinnerRestock(getActivity(), restockList);
                    spinRestock.setAdapter(adapterRestock);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "gagal memuat data: "+error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
        spinRestock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvRestockSatuan.setText(restockList.get(i).getSatuan());
                id = restockList.get(i).getId();
//                Toast.makeText(getActivity(), "item: "+restockList.get(i).getBahan_baku(),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnCollectRestock.setOnClickListener(view1 -> {
            jumlah = Integer.parseInt(etRestockJumlah.getText().toString().trim());
            restockAdd();
        });
        return view;
    }

    private void restockAdd(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> minusStock = stockData.minStocks(id,jumlah);

        minusStock.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
                etRestockJumlah.setText(null);
                Toast.makeText(getActivity(), "berhasil", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "gagal "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
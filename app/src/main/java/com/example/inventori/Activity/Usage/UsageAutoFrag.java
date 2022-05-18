package com.example.inventori.Activity.Usage;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.inventori.API.APIRequestMenu;
import com.example.inventori.API.APIUsageAuto;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Adapter.AdapterUsageAuto;
import com.example.inventori.R;
import com.example.inventori.model.MenuModel;
import com.example.inventori.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsageAutoFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsageAutoFrag extends Fragment {

    ListView lvUsageAuto;
    Button btnOrder;
    AdapterUsageAuto adapterUsageAuto;
    List<MenuModel> menuModels = new ArrayList<>();
    String konfirmasi, pesanan;
    int jumlah;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsageAutoFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsageAutoFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static UsageAutoFrag newInstance(String param1, String param2) {
        UsageAutoFrag fragment = new UsageAutoFrag();
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
        View view = inflater.inflate(R.layout.fragment_usage_auto, container, false);
        lvUsageAuto = view.findViewById(R.id.lvUsageAuto1);
        btnOrder = view.findViewById(R.id.btnOrder1);
        getList();

        btnOrder.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("konfirmasi pesanan");
            konfirmasi = "item: ";
            for (int i = 0; i < menuModels.size(); i++) {
                if(menuModels.get(i).getQty() != 0) {
                    pesanan = menuModels.get(i).getMenu();
                    jumlah = menuModels.get(i).getQty();
                    konfirmasi = konfirmasi + "\n" + pesanan + ": " + jumlah+"\n";
                }
            }

            builder.setMessage(konfirmasi);

            builder.setPositiveButton("Ok", (dialogInterface, i) -> {

                for (i = 0; i < menuModels.size(); i++) {
                    pesanan = menuModels.get(i).getMenu();
                    jumlah = menuModels.get(i).getQty();
                    System.out.println(pesanan +
                            " : " + jumlah);
                    konfirmOrder();
                }
                for (i = 0; i < menuModels.size(); i++) {
                    menuModels.get(i).setQty(0);
                }
                adapterUsageAuto.notifyDataSetChanged();
                Toast.makeText(getActivity(), "pesanan dikonfirmasi",
                        Toast.LENGTH_SHORT).show();

            });
            builder.setNegativeButton(
                    "Cancel", (dialogInterface, i) -> {
                    });
            builder.show();
        });
        return view;
    }

    private void getList(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> showData = dataMenu.showMenu();

        showData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String pesan = response.body().getPesan();

                menuModels = response.body().getData();
                adapterUsageAuto = new AdapterUsageAuto(getActivity(), menuModels);
                lvUsageAuto.setAdapter(adapterUsageAuto);
                Toast.makeText(getActivity(),
                        "success: " +"(" +code+ ")" +" "+ pesan, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void konfirmOrder(){
        APIUsageAuto usageData = ServerConnection.connection().create(APIUsageAuto.class);
        Call<ResponseModel> order = usageData.usage(pesanan.trim().replaceAll("\\s","_"),jumlah);

        order.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(getActivity(), "berhasil memesan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "gagal: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}
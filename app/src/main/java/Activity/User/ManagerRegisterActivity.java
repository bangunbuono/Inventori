package Activity.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventori.API.APIAccounts;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.R;
import com.example.inventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerRegisterActivity extends AppCompatActivity {
    EditText etRegisManager, etRegisManagerPassword, etConfirmManagerPassword;
    TextView tvMasukManager;
    Button btnRegisManager;
    String manager, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_register);

        etRegisManager = findViewById(R.id.etRegisManager);
        etRegisManagerPassword = findViewById(R.id.etRegisManagerPassword);
        etConfirmManagerPassword = findViewById(R.id.etConfirmManagerPassword);
        tvMasukManager = findViewById(R.id.tvMasukManager);
        btnRegisManager = findViewById(R.id.btnRegisManager);

        btnRegisManager.setOnClickListener(view -> {
            manager = etRegisManager.getText().toString().trim();
            password = etRegisManagerPassword.getText().toString().trim();
            confirmPassword = etConfirmManagerPassword.getText().toString().trim();

            if(manager.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show();
            }else if(!password.equals(confirmPassword)){
                Toast.makeText(this, "Password tidak sesuai", Toast.LENGTH_SHORT).show();
            }else {
                register();
            }
        });

        tvMasukManager.setOnClickListener(view -> {
            startActivity(new Intent(this, ManagerLoginActivity.class));
            finish();
        });
    }

    private void register() {
        APIAccounts regist = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> registManager = regist.registerManager(manager,password);

        registManager.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null){
                    String pesan = response.body().getPesan();
                    int code = response.body().getCode();
                    if(code==1){
                        startActivity(new Intent(ManagerRegisterActivity.this, ManagerLoginActivity.class));
                        finish();
                    }else Toast.makeText(ManagerRegisterActivity.this, pesan, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable throwable) {

            }
        });
    }
}
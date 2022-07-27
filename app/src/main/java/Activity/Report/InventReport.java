package Activity.Report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.inventori.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InventReport extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fragment;
    FragmentManager manager;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_report);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bottomNavigationView = findViewById(R.id.navReport);
        radioGroup = findViewById(R.id.radioGroupTime);

        bottomNavigationView.setItemHorizontalTranslationEnabled(true);

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction().replace(R.id.flFragmentReport, new HistoryFrag());
        transaction.commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            fragment = null;
            switch (item.getItemId()){
                case R.id.historyNav:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.flFragmentReport, new HistoryFrag()).commit();
                    break;

                case R.id.statNav:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.flFragmentReport, new StatFrag()).commit();
                    break;
            }
            return true;
        });

    }
}
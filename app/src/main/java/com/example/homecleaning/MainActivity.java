package com.example.homecleaning;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homecleaning.models.Service;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvServices;
    private ServiceAdapter adapter;
    private DBHelper db;
    private FloatingActionButton fabBookings;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        db = new DBHelper(this);
        rvServices = findViewById(R.id.rvServices);
        fabBookings = findViewById(R.id.fabBookings);
        tvWelcome = findViewById(R.id.tvWelcome);

        rvServices.setLayoutManager(new LinearLayoutManager(this));

        loadServices();

        fabBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("main");
                startActivity(new Intent(MainActivity.this, BookingActivity.class));
            }
        });

        // welcome
        SharedPreferences prefs = getSharedPreferences("clean_pref", MODE_PRIVATE);
        String name = prefs.getString("user_name", "");
        if (!name.isEmpty()) tvWelcome.setText("Welcome, " + name);
    }

    private void loadServices() {
        List<Service> list = db.getAllServices();
        adapter = new ServiceAdapter(list, this);
        rvServices.setAdapter(adapter);
    }

    // menu for logout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            getSharedPreferences("clean_pref", MODE_PRIVATE).edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
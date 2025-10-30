package com.example.homecleaning;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.homecleaning.models.Service;

public class ServiceDetailActivity extends BaseActivity {
    private TextView tvTitle, tvDesc, tvPrice;
    private Button btnBook;
    private DBHelper db;
    private int serviceId;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Back button → behave like normal back
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_home) {
            // Custom Home button → go to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        setupToolbar("My Bookings", true);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        db = new DBHelper(this);
        tvTitle = findViewById(R.id.tvTitle);
        tvDesc = findViewById(R.id.tvDesc);
        tvPrice = findViewById(R.id.tvPrice);
        btnBook = findViewById(R.id.btnBook);

        serviceId = getIntent().getIntExtra("service_id", -1);
        if (serviceId != -1) {
            Service s = db.getServiceById(serviceId);
            if (s != null) {
                tvTitle.setText(s.getTitle());
                tvDesc.setText(s.getDescription());
                tvPrice.setText("₹ " + s.getPrice());
            }
        }

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ServiceDetailActivity.this, BookingActivity.class);
                i.putExtra("mode", "new");           // ✅ Add this line
                i.putExtra("service_id", serviceId);  // Pass the selected service id
                startActivity(i);
            }
        });

    }
}


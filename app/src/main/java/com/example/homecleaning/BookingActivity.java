package com.example.homecleaning;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homecleaning.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends BaseActivity {

    LinearLayout newBookingLayout, myBookingsLayout;
    EditText etAddress, etDate, etTime;
    Button btnBook;
    RecyclerView recyclerBookings;
    DBHelper dbHelper;
    int userId, serviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        // ✅ UI reference


        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        newBookingLayout = findViewById(R.id.newBookingLayout);
        myBookingsLayout = findViewById(R.id.myBookingsLayout);
        etAddress = findViewById(R.id.etAddress);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        btnBook = findViewById(R.id.btnBook);
        recyclerBookings = findViewById(R.id.recyclerBookings);

        dbHelper = new DBHelper(this);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // ✅ Get userId from SharedPreferences
        userId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getInt("user_id", -1);

        // ✅ Get mode from Intent
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");

        if ("new".equals(mode)) {
            // Show new booking form
            newBookingLayout.setVisibility(View.VISIBLE);
            myBookingsLayout.setVisibility(View.GONE);
            setupToolbar("Enter Booking Detail", true);
            serviceId = intent.getIntExtra("service_id", -1);

            btnBook.setOnClickListener(v -> {
                String address = etAddress.getText().toString().trim();
                String date = etDate.getText().toString().trim();
                String time = etTime.getText().toString().trim();

                if (!TextUtils.isEmpty(address) && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(time)) {
                    Booking booking = new Booking();
                    booking.setUserId(userId);
                    booking.setServiceId(serviceId);
                    booking.setAddress(address);
                    booking.setDate(date);
                    booking.setTime(time);
                    booking.setStatus("Pending");

                    boolean success = dbHelper.addBooking(booking);
                    if (success) {
                        Toast.makeText(this, "✅ Booking Confirmed!", Toast.LENGTH_SHORT).show();
                        newBookingLayout.setVisibility(View.GONE);
                        myBookingsLayout.setVisibility(View.VISIBLE);

                        List<Booking> bookingList = dbHelper.getBookingsByUserId(userId);
                        BookingAdapter adapter = new BookingAdapter(this, new ArrayList<>(bookingList));
                        recyclerBookings.setLayoutManager(new LinearLayoutManager(this));
                        recyclerBookings.setAdapter(adapter);

                    } else {
                        Toast.makeText(this, "❌ Booking Failed!", Toast.LENGTH_SHORT).show();
                    }
                    setupToolbar("My Bookings", true);
                } else {
                    Toast.makeText(this, "⚠️ Please enter address, date & time", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            // Show bookings list
            setupToolbar("My Bookings", true);
            newBookingLayout.setVisibility(View.GONE);
            myBookingsLayout.setVisibility(View.VISIBLE);

            List<Booking> bookingList = dbHelper.getBookingsByUserId(userId);
            if (!bookingList.isEmpty()) {
                BookingAdapter adapter = new BookingAdapter(this, new ArrayList<>(bookingList));
                recyclerBookings.setLayoutManager(new LinearLayoutManager(this));
                recyclerBookings.setAdapter(adapter);
            } else {
                Toast.makeText(this, "No bookings found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

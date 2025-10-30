package com.example.homecleaning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homecleaning.models.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context;
    private List<Booking> bookingList;

    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.tvBookingId.setText("Booking ID: " + booking.getId());
        holder.tvServiceTitle.setText("Service Title: " + booking.getServiceTitle());
        holder.tvDateTime.setText("Date & Time: " + booking.getDate()+" "+booking.getTime());
        holder.tvAddress.setText("Address: " + booking.getAddress());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookingId, tvServiceTitle, tvDateTime,tvAddress;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvServiceTitle = itemView.findViewById(R.id.tvServiceTitle);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvAddress = itemView.findViewById(R.id.tvAddress);
        }
    }
}


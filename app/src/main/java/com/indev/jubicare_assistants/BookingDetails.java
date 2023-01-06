package com.indev.jubicare_assistants;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class BookingDetails extends AppCompatActivity {
    Button Button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        setTitle("Booking Details");
    }
}
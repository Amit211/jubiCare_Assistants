package com.indev.jubicare_assistants;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {
    @BindView(R.id.et_contect_no)
    EditText et_contect_no;
    @BindView(R.id.ll_contact)
    LinearLayout ll_contact;
    @BindView(R.id.rg_PatientBookin)
    RadioGroup rg_PatientBookin;
    @BindView(R.id.rb_patientAccuntNo)
    RadioButton rb_patientAccuntNo;
    @BindView(R.id.rb_patientAccuntYes)
    RadioButton rb_patientAccuntYes;
    @BindView(R.id.btn_login)
    Button btn_login;
    String patientBooking;
    ProgressDialog mprogressDialog;
    SharedPrefHelper sharedPrefHelper;
    SignUpModel signUpModel;
    Context context = this;
    String contect_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Booking");
        ButterKnife.bind(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        signUpModel = new SignUpModel();
        //rg_PatientBookin.check(rb_patientAccuntYes);
        //rb_patientAccuntYes.setChecked(true);

        rg_PatientBookin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_patientAccuntYes:
                        patientBooking = ("Yes");
                        ll_contact.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_patientAccuntNo:
                        patientBooking = ("No");
                        ll_contact.setVisibility(View.GONE);
                        break;
                }
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                      if (patientBooking.equals("Yes")) {
                        Intent intent = new Intent(BookingActivity.this, BookinlistActivity.class);
                        intent.putExtra("et_contect", et_contect_no.getText().toString());
                        startActivity(intent);
                        finish();
                    } else if (patientBooking.equals("No")) {
                        Intent intent = new Intent(BookingActivity.this, PatientFillAppointment.class);
                        intent.putExtra("fromCounselor", "fromCounselor");
                        startActivity(intent);
                        finish();
                    }

                }
            }

        });


    }


    private boolean checkValidation() {
        boolean ret = true;

        if (rb_patientAccuntNo.isChecked() || rb_patientAccuntYes.isChecked()) {
        } else {
            Toast.makeText(BookingActivity.this, "select one ", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (et_contect_no.getText().toString().trim().equalsIgnoreCase("")) {
//            EditText flagEditfield = et_contect_no;
//            String msg = "Please Enter Contact number";
//            et_contect_no.setError(msg);
//            et_contect_no.requestFocus();
//            return false;
//        }
        return ret;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BookingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(BookingActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
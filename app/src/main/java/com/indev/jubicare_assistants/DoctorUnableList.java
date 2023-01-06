package com.indev.jubicare_assistants;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.adapter.DoctorAvilableListAdapter;
import com.indev.jubicare_assistants.model.PharmacyPatientModel;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorUnableList extends AppCompatActivity {
    DoctorAvilableListAdapter doctorAvilableListAdapter;
    private Context context = this;
    SharedPrefHelper sharedPrefHelper;
    PharmacyPatientModel pharmacyPatientModel = new PharmacyPatientModel();
    ArrayList<ContentValues> patientContentValue = new ArrayList<ContentValues>();
    private ProgressDialog mProgressDialog;
    SqliteHelper sqliteHelper;
    @BindView(R.id.rv_counsellor_list)
    RecyclerView rv_counsellor_list;
    @BindView(R.id.tv_list_count)
    TextView tv_list_count;
    String currentTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_unlable_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        setTitle("Doctor Available");
        initViews();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            currentTimeStamp = dateFormat.format(new Date()); // Find todays date
        } catch (Exception e) {
            e.printStackTrace();
        }
        callDoctorApi();
    }

    private void callDoctorApi() {
        mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
        pharmacyPatientModel.setAvailability_date(currentTimeStamp);
        pharmacyPatientModel.setAvailability("unavailable");
        Gson mGson = new Gson();
        String data = mGson.toJson(pharmacyPatientModel);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        APIClient.getClient().create(TELEMEDICINE_API.class).download_doctor_available(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        mProgressDialog.dismiss();
                        patientContentValue.clear();
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            JsonObject singledataP = response.body();
                            JsonArray data = singledataP.getAsJsonArray("data");
                            if (data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    JSONObject singledata = new JSONObject(data.get(i).toString());
                                    // Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());
                                    Iterator keys = singledata.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                    }
                                    patientContentValue.add(contentValues);
                                    /*total count of list*/
                                    tv_list_count.setText("" + patientContentValue.size());
                                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                                    doctorAvilableListAdapter = new DoctorAvilableListAdapter(patientContentValue, context);
                                    rv_counsellor_list.setLayoutManager(mLayoutManager);
                                    rv_counsellor_list.setAdapter(doctorAvilableListAdapter);
                                }
                            } else {
                                tv_list_count.setVisibility(View.GONE);
                                rv_counsellor_list.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    private void initViews() {
        sharedPrefHelper = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);
        mProgressDialog = new ProgressDialog(context);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(DoctorUnableList.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(context, HomeActivity.class);
        startActivity(intent);
        finish();

    }
}
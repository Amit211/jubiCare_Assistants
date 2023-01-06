package com.indev.jubicare_assistants;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.adapter.ContactUsAdapter;
import com.indev.jubicare_assistants.model.ContactusPojo;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUs extends AppCompatActivity {
    @BindView(R.id.tv_mobile_number)
    TextView tv_mobile_number;
    @BindView(R.id.rv_village_level)
    RecyclerView rv_village_level;
    @BindView(R.id.rv_block_level)
    RecyclerView rv_block_level;
    @BindView(R.id.rv_district_level)
    RecyclerView rv_district_level;

    /*normal widgets*/
    private Context context = this;
    String mobile_number = "";
    ProgressDialog mProgressDialog;
    SharedPrefHelper sharedPrefHelper;
    ContactUsAdapter contactUsAdapter;
    ContactusPojo contactusPojo = new ContactusPojo();
    ArrayList<ContentValues> contactUsContentValue = new ArrayList<ContentValues>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        setTitle("Contact Us");
        sharedPrefHelper = new SharedPrefHelper(this);
        EmergencyContactList();
    }

    private void EmergencyContactList() {
        mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
        contactusPojo = new ContactusPojo();
        contactusPojo.setUser_id(sharedPrefHelper.getString("user_id", ""));
        //contactusPojo.setUser_id("109");

        Gson gson = new Gson();
        String data = gson.toJson(contactusPojo);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        /*send data here*/
        getEmergencyContact(body);
    }

    private void getEmergencyContact(RequestBody body) {
        TELEMEDICINE_API api_service = APIClient.getClient().create(TELEMEDICINE_API.class);
        if (body != null && api_service != null) {
            Call<JsonObject> server_response = api_service.emergencyContactList(body);
            try {
                if (server_response != null) {
                    server_response.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                try {
                                    JsonObject singledataP = response.body();
                                    Log.e(" cbhjcbhcbs", "onResponse: " + singledataP.toString());
                                    mProgressDialog.dismiss();
                                    JsonArray data1 = singledataP.getAsJsonArray("village_level");
                                    JsonArray data2 = singledataP.getAsJsonArray("block_level");
                                    JsonArray data3 = singledataP.getAsJsonArray("district_level");
                                    try {
                                        for (int i = 0; i < data1.size(); i++) {
                                            JSONObject singledata = new JSONObject(data1.get(i).toString());
                                            Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());

                                            Iterator keys = singledata.keys();
                                            ContentValues contentValues = new ContentValues();
                                            while (keys.hasNext()) {
                                                String currentDynamicKey = (String) keys.next();
                                                contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                            }
                                            contactUsContentValue.add(contentValues);
                                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                                            contactUsAdapter = new ContactUsAdapter(context, contactUsContentValue);
                                            rv_village_level.setLayoutManager(mLayoutManager);
                                            rv_village_level.setAdapter(contactUsAdapter);

                                            contactUsAdapter.onItemClick(new ContactUsAdapter.ClickListener() {
                                                @Override
                                                public void onItemClick(int position) {

                                                }

                                                @Override
                                                public void onListItemClick(int position) {
                                                    /*mobile_number = contactUsContentValue.get(position).get("contact_no").toString();
                                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile_number));
                                                    startActivity(callIntent);*/
                                                }
                                            });
                                        }
                                        for (int i = 0; i < data2.size(); i++) {
                                            JSONObject singledata = new JSONObject(data2.get(i).toString());
                                            Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());

                                            Iterator keys = singledata.keys();
                                            ContentValues contentValues = new ContentValues();
                                            while (keys.hasNext()) {
                                                String currentDynamicKey = (String) keys.next();
                                                contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                            }
                                            contactUsContentValue.add(contentValues);
                                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                                            contactUsAdapter = new ContactUsAdapter(context, contactUsContentValue);
                                            rv_village_level.setLayoutManager(mLayoutManager);
                                            rv_village_level.setAdapter(contactUsAdapter);
                                        }
                                        for (int i = 0; i < data3.size(); i++) {
                                            JSONObject singledata = new JSONObject(data3.get(i).toString());
                                            Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());

                                            Iterator keys = singledata.keys();
                                            ContentValues contentValues = new ContentValues();
                                            while (keys.hasNext()) {
                                                String currentDynamicKey = (String) keys.next();
                                                contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                            }
                                            contactUsContentValue.add(contentValues);
                                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                                            contactUsAdapter = new ContactUsAdapter(context, contactUsContentValue);
                                            rv_village_level.setLayoutManager(mLayoutManager);
                                            rv_village_level.setAdapter(contactUsAdapter);
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.tv_mobile_number)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_mobile_number:
                //setCallOnEnteredNumber();
                break;
        }
    }

    private void setCallOnEnteredNumber() {
        mobile_number = tv_mobile_number.getText().toString().trim();
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile_number));
        startActivity(callIntent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}

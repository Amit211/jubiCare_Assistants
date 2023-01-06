package com.indev.jubicare_assistants;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.adapter.PatientListAdapter;
import com.indev.jubicare_assistants.model.PharmacyPatientModel;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;

import org.json.JSONException;
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

public class NotAssignedAppointments extends AppCompatActivity {
    @BindView(R.id.rv_not_appointment_assigned)
    RecyclerView rv_not_appointment_assigned;
    @BindView(R.id.etSearchBar)
    EditText etSearchBar;
    @BindView(R.id.btnSearch)
    ImageView btnSearch;
    @BindView(R.id.tv_list_count)
    TextView tv_list_count;
    @BindView(R.id.tvNoDataFound)
    TextView tvNoDataFound;
    @BindView(R.id.btn_go_home)
    TextView btn_go_home;

    /*normal widgets*/
    private Context context=this;
    PatientListAdapter patientListAdapter;
    PharmacyPatientModel pharmacyPatientModel = new PharmacyPatientModel();
    ArrayList<ContentValues> patientContentValue = new ArrayList<ContentValues>();
    ProgressDialog mProgressDialog;
    SharedPrefHelper sharedPrefHelper;
    String fromNotAssignment="";
    String pending_cases="";
    String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_assigned_appointments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        setTitle("Not Assigned Appointments");
        sharedPrefHelper=new SharedPrefHelper(context);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            fromNotAssignment=bundle.getString("fromNotAssignment", "");
            pending_cases=bundle.getString("pending_cases", "");
        }
        setNotAppointmentAssignedAdapter();
    }

    private void setNotAppointmentAssignedAdapter() {
        mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
        //setSearchDialog();
        if (fromNotAssignment.equalsIgnoreCase("fromNotAssignment")) {
            pharmacyPatientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
            pharmacyPatientModel.setType("not_assigned_doctor");
            pharmacyPatientModel.setMobile(searchInput);
            pharmacyPatientModel.setRole_id(sharedPrefHelper.getString("role_id",""));
        } else if (pending_cases.equalsIgnoreCase("pending_cases")) {
            pharmacyPatientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
            pharmacyPatientModel.setRole_id(sharedPrefHelper.getString("role_id",""));
            pharmacyPatientModel.setIs_doctor_done("N");
            pharmacyPatientModel.setMobile(searchInput);
        } else {
            pharmacyPatientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
            pharmacyPatientModel.setRole_id(sharedPrefHelper.getString("role_id",""));
            pharmacyPatientModel.setIs_doctor_done("Y");
            pharmacyPatientModel.setMobile(searchInput);
        }
        Gson mGson=new Gson();
        String data = mGson.toJson(pharmacyPatientModel);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        APIClient.getClient().create(TELEMEDICINE_API.class).download_patientList(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e("bchjb", "jhjhcs_ cn "+jsonObject.toString());
                        mProgressDialog.dismiss();
                        patientContentValue.clear();
                        String success = jsonObject.getString("success");
                        JsonObject singledataP = response.body();

                        JsonArray data = singledataP.getAsJsonArray("tableData");
                        if (data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            JSONObject singledata = new JSONObject(data.get(i).toString());
                            Log.e("bcjhdbjcb", "onResponse: "+singledata.toString());

                            // JSONObject singledata = data.getJSONObject(i);
                            //singledata.getString("id");
                            Iterator keys = singledata.keys();
                            ContentValues contentValues = new ContentValues();
                            while (keys.hasNext()) {
                                String currentDynamicKey = (String) keys.next();
                                contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                            }

//                            patientContentValue.add(contentValues);
//                            tv_list_count.setText("Total - " + patientContentValue.size());
//                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
//                            patientListAdapter = new PatientListAdapter(context, patientContentValue);
//                            rv_not_appointment_assigned.setLayoutManager(mLayoutManager);
//                            rv_not_appointment_assigned.setAdapter(patientListAdapter);
                            patientListAdapter.onItemClick(new PatientListAdapter.ClickListener() {
                                @Override
                                public void onItemClick(int position) {

                                }

                                @Override
                                public void onListItemClick(int position) {
                                    if (pending_cases.equalsIgnoreCase("pending_cases")) {
                                        Intent intent = new Intent(context, CommonProfile.class);
                                        intent.putExtra("pending_cases", "pending_cases");
                                        intent.putExtra("profile_patient_id", patientContentValue.get(position).get("profile_patient_id").toString());
                                        startActivity(intent);
                                    }
                                    if (fromNotAssignment.equalsIgnoreCase("fromNotAssignment")) {
                                        Intent intent = new Intent(context, PatientFillAppointment.class);
                                        intent.putExtra("not_assigned_appointments", "not_assigned_appointments");
                                        intent.putExtra("profile_patient_id", patientContentValue.get(position).get("profile_patient_id").toString());
                                        intent.putExtra("patient_appointments_id", patientContentValue.get(position).get("patient_appointments_id").toString());
                                        startActivity(intent);
                                        finish();
                                    }  else {
                                        Intent intent = new Intent(context, PatientFillAppointment.class);
                                        intent.putExtra("patient","patient");
                                        intent.putExtra("profile_patient_id", patientContentValue.get(position).get("profile_patient_id").toString());
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                        } else {
                            tv_list_count.setVisibility(View.GONE);
                            rv_not_appointment_assigned.setVisibility(View.GONE);
                            tvNoDataFound.setVisibility(View.VISIBLE);
                            btn_go_home.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
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

        /*LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        patientListAdapter = new PatientListAdapter(context, patientContentValue);
        rv_not_appointment_assigned.setLayoutManager(mLayoutManager);
        rv_not_appointment_assigned.setAdapter(patientListAdapter);*/
    }

    @OnClick({R.id.btnSearch, R.id.btn_go_home})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                searchInput = etSearchBar.getText().toString().trim();
                setNotAppointmentAssignedAdapter();
                //Intent intent1 = new Intent(context, PatientListReplicaforSearch.class);
                //intent1.putExtra("searchInput", searchInput);
                //startActivity(intent1);
                //finish();
                break;
            case R.id.btn_go_home:
                Intent intentCounsellor=new Intent(context, HomeActivity.class);
                intentCounsellor.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentCounsellor);
                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

}

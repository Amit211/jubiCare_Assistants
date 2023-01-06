package com.indev.jubicare_assistants;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.adapter.BookingListAdapter;
import com.indev.jubicare_assistants.model.PharmacyPatientModel;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchByMobile extends AppCompatActivity {
    @BindView(R.id.rg_search)
    RadioGroup rg_search;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;
    @BindView(R.id.rl_search1)
    RelativeLayout rl_search1;
    @BindView(R.id.rb_online)
    RadioButton rb_online;
    @BindView(R.id.rb_offline)
    RadioButton rb_offline;
    @BindView(R.id.etSearchBar1)
    EditText etSearchBar1;
    @BindView(R.id.etSearchBar)
    EditText etSearchBar;
    @BindView(R.id.btnSearch)
    ImageView btnSearch;
    @BindView(R.id.rl_skip)
    RelativeLayout rl_skip;
    @BindView(R.id.tv_search_by_mobile)
    TextView tv_search_by_mobile;
    @BindView(R.id.btn_get_number)
    Button btn_get_number;
  @BindView(R.id.btn_search_by_name)
  CheckBox btn_search_by_name;

    /*normal widgets*/
    private Context context=this;
    String searchInput;
    String common_search="";
    String search_by_name="false";
    SharedPrefHelper sharedPrefHelper;
    ProgressDialog mProgressDialog;
    PatientModel patientModel = new PatientModel();
    PharmacyPatientModel pharmacyPatientModel ;
SqliteHelper sqliteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sir_batayenge);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        setTitle("Search Patient ");

        sqliteHelper=new SqliteHelper(getApplicationContext());
        sharedPrefHelper = new SharedPrefHelper(context);
        mProgressDialog = new ProgressDialog(context);
        pharmacyPatientModel=new PharmacyPatientModel();
        /*get intent data here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            common_search = bundle.getString("common_search", "");
        }

        if (common_search.equalsIgnoreCase("common_search")) {
            tv_search_by_mobile.setVisibility(View.GONE);
            rl_skip.setVisibility(View.GONE);
        }




        btn_search_by_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (btn_search_by_name.isChecked()){
                    etSearchBar.setText("");
                    etSearchBar.setHint("Search By Name");
                    etSearchBar.setInputType(InputType.TYPE_CLASS_TEXT);
                    search_by_name="true";
                }else {
                    etSearchBar.setText("");
                    etSearchBar.setHint("Search By Mobile");
                    etSearchBar.setInputType(InputType.TYPE_CLASS_NUMBER);
                    search_by_name="false";
                }
            }
        });



        rg_search.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_online:
                        rl_search1.setVisibility(View.VISIBLE);
                        rl_search.setVisibility(View.GONE);
                        break;
                    case R.id.rb_offline:
                        rl_search1.setVisibility(View.GONE);
                        rl_search.setVisibility(View.VISIBLE);

                        break;

                }
            }
        });
//        btn_get_number.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent1 = new Intent(context, PatientProfileList.class);
//                intent1.putExtra("et_contect", etSearchBar.getText().toString());
//                startActivity(intent1);
//                finish();
//            }
//          });
    }
    @OnClick({R.id.btnSearch,R.id.btnSearch1, R.id.rl_skip, R.id.btn_get_number})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch1:
                searchInput = etSearchBar1.getText().toString().trim();
                if (containsDigit(searchInput) && search_by_name.equals("true")){
                    Toast.makeText(context, "Please enter name of Patient", Toast.LENGTH_SHORT).show();
                    return;
                }else if ((searchInput.equals("") || containsDigit(searchInput)==false || searchInput.length()<10) && search_by_name.equals("false")){
                    Toast.makeText(context, "Please enter mobile of Patient", Toast.LENGTH_SHORT).show();
                    return;
                }else if ((searchInput.equals("") && search_by_name.equals("true"))){
                    Toast.makeText(context, "Please enter name of Patient", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    download_Patient();
                }
                break;
            case R.id.btnSearch:
                searchInput = etSearchBar.getText().toString().trim();
                if (containsDigit(searchInput) && search_by_name.equals("true")){
                    Toast.makeText(context, "Please enter name of Patient", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent2 = new Intent(context, PatientProfileList.class);
                intent2.putExtra("searchInput", searchInput);
                intent2.putExtra("common_search", common_search);
                intent2.putExtra("search_by_name",search_by_name);
                intent2.putExtra("common_search_counsellor", "common_search_counsellor");
                startActivity(intent2);
                //finish();
                break;
            case R.id.rl_skip:
                Intent intent=new Intent(context, PatientFillAppointment.class);
                intent.putExtra("fromCounselor","fromCounselor");
                startActivity(intent);
                break;
            case R.id.btn_get_number:
                PatientFilledDataModel appointmentInput = new PatientFilledDataModel();
                appointmentInput.setUser_id(sharedPrefHelper.getString("user_id", ""));
                appointmentInput.setRole_id(sharedPrefHelper.getString("role_id", ""));
                Gson gson1 = new Gson();
                String data1 = gson1.toJson(appointmentInput);
                MediaType JSON1 = MediaType.parse("application/json; charset=utf-8");
                RequestBody body1 = RequestBody.create(JSON1, data1);

                mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
                //callFormCounsellorSearchMobile(body1);
                break;


        }
    }
    public final boolean containsDigit(String s) {
        boolean containsDigit = false;

        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (containsDigit = Character.isDigit(c)) {
                    containsDigit=true;
                    break;
                }
            }
        }

        return containsDigit;
    }


//    @OnClick({R.id.btnSearch, R.id.rl_skip, R.id.btn_get_number})
//    void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btnSearch:
//                searchInput = etSearchBar.getText().toString().trim();
//                if (containsDigit(searchInput) && search_by_name.equals("true")){
//                    Toast.makeText(context, "Please enter name of Patient", Toast.LENGTH_SHORT).show();
//                    return;
//                }
////                Intent intent1 = new Intent(context, PatientListReplicaforSearch.class);
////                intent1.putExtra("searchInput", searchInput);
////                intent1.putExtra("common_search", common_search);
////                intent1.putExtra("search_by_name",search_by_name);
////                intent1.putExtra("common_search_counsellor", "common_search_counsellor");
////                startActivity(intent1);
//                //finish();
//                break;
//            case R.id.rl_skip:
//                Intent intent=new Intent(context, PatientFillAppointment.class);
//                intent.putExtra("fromCounselor","fromCounselor");
//                startActivity(intent);
//                break;
//            case R.id.btn_get_number:
//                PatientFilledDataModel appointmentInput = new PatientFilledDataModel();
//                appointmentInput.setUser_id(sharedPrefHelper.getString("user_id", ""));
//                appointmentInput.setRole_id(sharedPrefHelper.getString("role_id", ""));
//
//                Gson gson1 = new Gson();
//                String data1 = gson1.toJson(appointmentInput);
//                MediaType JSON1 = MediaType.parse("application/json; charset=utf-8");
//                RequestBody body1 = RequestBody.create(JSON1, data1);
//
//                mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
//                callFormCounsellorSearchMobile(body1);
//                break;
//
//
//        }
//    }
//    public final boolean containsDigit(String s) {
//        boolean containsDigit = false;
//
//        if (s != null && !s.isEmpty()) {
//            for (char c : s.toCharArray()) {
//                if (containsDigit = Character.isDigit(c)) {
//                    containsDigit=true;
//                    break;
//                }
//            }
//        }
//
//        return containsDigit;
//    }
//
public void download_Patient() {
    mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
    pharmacyPatientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
    pharmacyPatientModel.setPatient_name(searchInput);
    //pharmacyPatientModel.setContact_no(searchInput);
    Gson mGson = new Gson();
    String data = mGson.toJson(pharmacyPatientModel);
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RequestBody body = RequestBody.create(JSON, data);
    APIClient.getClient().create(TELEMEDICINE_API.class).patient_search(body).enqueue(new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (response.isSuccessful()) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    mProgressDialog.dismiss();
                    String success = jsonObject.getString("success");
                    if (success.equals("1")) {
//                            sqliteHelper.dropTable("profile_patient");
//                            sqliteHelper.dropTable("patient_appointments");
                        JsonObject singledataP = response.body();
                        JsonArray data = singledataP.getAsJsonArray("tableData");
                        if (data.size() > 0)  {
                            for (int i = 0; i < data.size(); i++) {
                                JSONObject singledata = new JSONObject(data.get(i).toString());
                                Iterator keys = singledata.keys();
                                ContentValues contentValues = new ContentValues();
                                String id="";
                                while (keys.hasNext()) {
                                    String currentDynamicKey = (String) keys.next();
                                    if(currentDynamicKey.equals("id"))
                                        id=singledata.get(currentDynamicKey).toString();
                                    if (currentDynamicKey.equals("patient_appointments")) {
                                        try {
                                            JSONArray appointments = null;
                                            appointments = singledata.getJSONArray("patient_appointments");
                                            if (appointments != null) {
                                                sqliteHelper.dropTableWhere("patient_appointments","profile_patient_id",id);
                                                for (int j = 0; j < appointments.length(); j++) {
                                                    JSONObject appointmentsed = new JSONObject(appointments.get(j).toString());
                                                    Iterator keys3 = appointmentsed.keys();
                                                    ContentValues contentValues3 = new ContentValues();
                                                    String appointment_id="";
                                                    while (keys3.hasNext()) {
                                                        String currentDynamicKey3 = (String) keys3.next();
                                                        if(currentDynamicKey3.equals("id"))
                                                            appointment_id=appointmentsed.get(currentDynamicKey3).toString();
                                                        if (currentDynamicKey3.equals("appointment_medicine_prescribed")) {
                                                            try {
                                                                JSONArray medicinePrescribed = null;
                                                                medicinePrescribed = appointmentsed.getJSONArray("appointment_medicine_prescribed");
                                                                if (medicinePrescribed != null) {
                                                                    sqliteHelper.dropTableWhere("appointment_medicine_prescribed","patient_appointment_id",appointment_id);
                                                                    for (int k = 0; k < medicinePrescribed.length(); k++) {
                                                                        JSONObject singleDataMedicalPrescribed = new JSONObject(medicinePrescribed.get(k).toString());
                                                                        Iterator keys2 = singleDataMedicalPrescribed.keys();
                                                                        ContentValues contentValues2 = new ContentValues();
                                                                        while (keys2.hasNext()) {
                                                                            String currentDynamicKey2 = (String) keys2.next();
                                                                            contentValues2.put(currentDynamicKey2, singleDataMedicalPrescribed.get(currentDynamicKey2).toString());
                                                                        }
                                                                        contentValues2.put("flag", "1");
                                                                        sqliteHelper.saveMasterTable(contentValues2, "appointment_medicine_prescribed");
                                                                    }
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }else {
                                                            contentValues3.put(currentDynamicKey3, appointmentsed.get(currentDynamicKey3).toString());
                                                        }
                                                    }
                                                    contentValues3.put("flag", "1");
                                                    sqliteHelper.saveMasterTable(contentValues3, "patient_appointments");
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                    }
                                }
                                sqliteHelper.dropTableWhere("profile_patients","id",id);
                                contentValues.put("flag", "1");
                                sqliteHelper.saveMasterTable(contentValues, "profile_patients");
                            }

                        }
                        // mProgressDialog.dismiss();
                        Intent intent1 = new Intent(context, PatientProfileList.class);
                        intent1.putExtra("searchInput", searchInput);
                        intent1.putExtra("common_search", common_search);
                        intent1.putExtra("search_by_name",search_by_name);
//                        intent1.putExtra("common_search_counsellor", "common_search_counsellor");
                        startActivity(intent1);
                        //finish();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // mProgressDialog.dismiss();
                }
            }
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            mProgressDialog.dismiss();
        }
    });    }

//    private void callFormCounsellorSearchMobile(RequestBody body1) {
//        APIClient.getClient().create(TELEMEDICINE_API.class).callFromCounsellorFillFormApi(body1).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body().toString());
//                    mProgressDialog.dismiss();
//                    Log.e("hdsstt", "sbhddyy " + jsonObject.toString());
//                    String success = jsonObject.optString("success");
//                    String message = jsonObject.optString("message");
//                    String caller_no = jsonObject.optString("caller_no");
//
//                    if (success.equalsIgnoreCase("1")) {
//                        etSearchBar.setText(caller_no);
//                        Toast.makeText(context, ""+message, Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(context, "Mobile number not found. Please enter mobile number.", Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    mProgressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                if (mProgressDialog.isShowing()) {
//                    mProgressDialog.dismiss();
//                }
//            }
//        });
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}

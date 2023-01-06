package com.indev.jubicare_assistants;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.projection.MediaProjectionManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hbisoft.hbrecorder.HBRecorder;
import com.hbisoft.hbrecorder.HBRecorderListener;
import com.indev.jubicare_assistants.adapter.AppointmentListAdapter;
import com.indev.jubicare_assistants.adapter.PatientListAdapter;
import com.indev.jubicare_assistants.adapter.TestDocsAdapterInProfile;
import com.indev.jubicare_assistants.model.AppointmentMedicinePrescribedModel;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;
import com.indev.jubicare_assistants.videocallingapp.BaseActivity;
import com.indev.jubicare_assistants.videocallingapp.CallScreenActivity;
import com.indev.jubicare_assistants.videocallingapp.SinchService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonProfile extends AppCompatActivity {
    @BindView(R.id.iv_profile_pic)
    ImageView iv_profile_pic;
    @BindView(R.id.tv_patient_name)
    TextView tv_patient_name;
    @BindView(R.id.tv_gender)
    TextView tv_gender;
    @BindView(R.id.tv_dob)
    TextView tv_dob;
    @BindView(R.id.tv_age)
    TextView tv_age;
    @BindView(R.id.tv_height)
    TextView tv_height;
    @BindView(R.id.tv_weight)
    TextView tv_weight;
    @BindView(R.id.tv_blood_group)
    TextView tv_blood_group;
    @BindView(R.id.tv_height_cms)
    TextView tv_height_cms;
    @BindView(R.id.tv_weight_kgs)
    TextView tv_weight_kgs;
    @BindView(R.id.tv_bg)
    TextView tv_bg;
    @BindView(R.id.tv_adhar_no)
    TextView tv_adhar_no;
    @BindView(R.id.tv_aadhar_no)
    TextView tv_aadhar_no;
    @BindView(R.id.tv_caste_text)
    TextView tv_caste_text;
    @BindView(R.id.tv_disability_text)
    TextView tv_disability_text;
    @BindView(R.id.tv_contact_no)
    TextView tv_contact_no;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.tv_address_heading)
    TextView tv_address_heading;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_emergency_contact)
    TextView tv_emergency_contact;
    @BindView(R.id.tv_emergency_contact_no)
    TextView tv_emergency_contact_no;
    @BindView(R.id.tv_code_pin)
    TextView tv_code_pin;
    @BindView(R.id.tv_pin_code)
    TextView tv_pin_code;

    public static ScrollView scrollView;

    /*normal widgets*/
    private Context context = this;
    ProgressDialog mProgressDialog;
    PatientFilledDataModel patientFilledDataModel;
    SharedPrefHelper sharedPrefHelper;
    String profile_id = "";
    String not_assigned_appointments = "";
    String assigned_appointments = "";
    String already_assigned = "";
    String not_prescribed_medicine = "";
    String total_medicine_delivered = "";
    String pending_medicine_delivered = "";
    String total_handel_cases = "";
    String total_pending_cases = "";
    String total_emergency_cases = "";
    String test_pending_pharmacy = "";
    String pharma_pending_delivery = "";
    String pharma_medicine_delivery = "";
    String profile_tab = "";
    String already_prescribed_medicine = "";
    String contact_no = "";
    String stste_name = "";
    String cast_name = "";
    String blod_name = "";
    String district_name = "";
    String block_name = "";
    String village_name = "";
    String patient_appointments_id = "";
    AppointmentInput appointmentInput;
   SqliteHelper sqliteHelper;
   SignUpModel signUpModel;
    /*@BindView(R.id.rv_Patient_appointment_list)
    RecyclerView rv_Patient_appointment_list;*/
    AppointmentListAdapter appointmentListAdapter;
    ArrayList<AppointmentInput> appointmentInputArrayList;
    ArrayList<AppointmentMedicinePrescribedModel> medicinePrescribedModelAL;

    /*for dynamic inflate layout*/
    @BindView(R.id.ll_for_dynamic_add)
    LinearLayout ll_for_dynamic_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commom_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sqliteHelper=new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
        signUpModel=new SignUpModel();
        appointmentInput=new AppointmentInput();
        appointmentInputArrayList=new ArrayList<>();
        medicinePrescribedModelAL=new ArrayList<>();
        ButterKnife.bind(this);
        setTitle("Patient Profile");
        initViews();

        /*get intent values here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            profile_id = bundle.getString("profile_patient_id", "");
            assigned_appointments = bundle.getString("assigned_appointments", "");
            not_assigned_appointments = bundle.getString("not_assigned_appointments", "");
            already_assigned = bundle.getString("already_assigned", "");
            not_prescribed_medicine = bundle.getString("not_prescribed_medicine", "");
            total_medicine_delivered = bundle.getString("total_medicine_delivered", "");
            pending_medicine_delivered = bundle.getString("pending_medicine_delivered", "");
            total_handel_cases = bundle.getString("total_handel_cases", "");
            total_pending_cases = bundle.getString("total_pending_cases", "");
            total_emergency_cases = bundle.getString("total_emergency_cases", "");
            pharma_pending_delivery = bundle.getString("pharma_pending_delivery", "");
            pharma_medicine_delivery = bundle.getString("pharma_medicine_delivery", "");
            profile_tab = bundle.getString("profile_tab", "");
            already_prescribed_medicine = bundle.getString("already_prescribed_medicine", "");
            test_pending_pharmacy = bundle.getString("test_pending_pharmacy", "");
        }

        /*hide and show here all fields of common profile for patient*/
        if (profile_tab.equalsIgnoreCase("profile_tab")) {
        }
        if (not_assigned_appointments.equalsIgnoreCase("not_assigned_appointments")) {
        }
        if (assigned_appointments.equalsIgnoreCase("assigned_appointments")) {
        }
        if (not_prescribed_medicine.equals("not_prescribed_medicine")) {
        }
        if (total_handel_cases.equals("total_handel_cases")) {
        }

//      if(!isInternetOn()){
          signUpModel = sqliteHelper.getPatientDetail(profile_id);
          stste_name = sqliteHelper.getstateName(signUpModel.getState_id(), "state");
          cast_name = sqliteHelper.getstateName(signUpModel.getCaste_id(), "caste");
          blod_name = sqliteHelper.getstateName(signUpModel.getBlood_group_id(), "blood_group");
          district_name = sqliteHelper.getdistrictName(signUpModel.getDistrict_id(), "district");
          block_name = sqliteHelper.getblockName(signUpModel.getBlock_id(), "block");
          village_name = sqliteHelper.getvillageName(signUpModel.getVillage_id(), "village");
    tv_patient_name.setText(signUpModel.getFull_name());
          if (signUpModel.getGender().equalsIgnoreCase("M")) {
              tv_gender.setText("Male");
          } else if (signUpModel.getGender().equalsIgnoreCase("F")) {
              tv_gender.setText("Female");
          } else {
              tv_gender.setText("Other");
          }
    tv_aadhar_no.setText(signUpModel.getAadhar_no());
    tv_caste_text.setText("" + cast_name);
    tv_dob.setText(signUpModel.getDob());
    tv_age.setText(signUpModel.getAge());
    tv_height.setText(signUpModel.getHeight());
    tv_weight.setText(signUpModel.getWeight());
    tv_disability_text.setText(signUpModel.getDisability());
    tv_contact_no.setText(signUpModel.getContact_no());
    tv_blood_group.setText("" + blod_name);
    tv_location.setText(("" + village_name) + ", " +
            ("" + block_name) + ", " +
            ("" + district_name) + ", " +
            ("" + stste_name));
    tv_address.setText(signUpModel.getAddress());
          tv_pin_code.setText(signUpModel.getPin_code());
    tv_emergency_contact_no.setText(signUpModel.getEmergency_contact_no());
          String url = APIClient.IMAGE_URL + signUpModel.getProfile_pic();
          if (signUpModel.getProfile_pic().equals("")) {
              if (signUpModel.getGender().equalsIgnoreCase("M")) {
                  iv_profile_pic.setImageResource(R.drawable.male_icon);
              } else {
                  iv_profile_pic.setImageResource(R.drawable.female_icon);
              }
          } else {
              if (signUpModel.getGender().equalsIgnoreCase("M")) {
                  Picasso.with(context)
                          .load(url)
                          .placeholder(R.drawable.male_icon)
                          .into(iv_profile_pic);
              } else {
                  Picasso.with(context)
                          .load(url)
                          .placeholder(R.drawable.female_icon)
                          .into(iv_profile_pic);
              }
          }

        setdat();
}

    private void initViews() {
        mProgressDialog = new ProgressDialog(context);
        patientFilledDataModel = new PatientFilledDataModel();
        sharedPrefHelper = new SharedPrefHelper(context);
        scrollView = findViewById(R.id.scrollView);
    }


private  void setdat(){
    appointmentInputArrayList = sqliteHelper.getDataForShowAppointmentOfPatient(profile_id);

    if (appointmentInputArrayList.size()>0) {
        Gson gson = new Gson();
        String listString = gson.toJson(
                appointmentInputArrayList,
                new TypeToken<ArrayList<AppointmentInput>>() {
                }.getType());

        JSONArray data2=null;
        JSONObject singledata2 = null;
        try {
            data2=new JSONArray(listString);
            addDynamicProfile(data2, singledata2, appointmentInputArrayList.get(0).getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

    private void addDynamicProfile(JSONArray data2, JSONObject singledata2, String id) {
        for (int i = 0; i < data2.length(); i++) {
            Log.e("addmkmm", "addDynamicProfile: " + data2.toString());
            View inflatedView = getLayoutInflater().inflate(
                    R.layout.common_profile_inflater, ll_for_dynamic_add, false);
            TestDocsAdapterInProfile testDocsAdapterInProfile;
            final ImageView iv_test_docs;
            final RecyclerView rv_test_docs_inprofile;
            final Button btn_show_medical_info;
            final LinearLayout ll_medical_info;
            final TextView tv_assigned_doctor, tv_doctor_assigned, tv_patient_symptoms, tv_symptoms_patient,
                    tv_taking_any_prescription, tv_any_taking_prescription, tv_date_taking_any_prescription,
                    tv_taking_prescription_any_date, tv_any_emergency, tv_emergency_any, tv_patient_disease,
                    tv_disease_patient, tv_test_done, tv_is_test_done, tv_test_done_date, tv_is_test_done_date,
                    tv_assigned_doctor_date, tv_date_assigned_doctor, tv_assigned_pharmacists, tv_pharmacists_assigned,
                    tv_assigned_pharmacists_date, tv_date_pharmacists_assigned, tv_medicine_delivered,
                    tv_medicine_delivered_on, tv_is_verified, tv_verified_is, tv_assigned_by, tv_by_assigned,
                    tv_appointment_type, tv_type_appointment, tv_remarks_by_doctor, tv_by_remarks_doctor,
                    tv_is_doctor_done, tv_doctor_is_done, tv_is_doctor_done_on, tv_is_on_doctor_done,
                    tv_is_pharmacists_done, tv_pharmacists_is_done, tv_is_pharmacists_done_on, tv_pharmacists_is_done_on,
                    tv_patient_medicine, tv_medicine_patient, tv_days_prescription, tv_prescription_days,
                    tv_interval_prescription, tv_prescription_interval, tv_eating_schedule_prescription,
                    tv_prescription_eating_schedule, tv_sos_prescription, tv_prescription_sos, tv_name_test,
                    tv_test_name, tv_name_subtest, tv_subtest_name, tv_test_docs,tv_blood_oxygen_levelP,tv_tempratureP,tv_HemoglobinP,tv_bp_upper, tv_patient_remarks,
                    tv_bplowerP,tv_bpupperP,tv_PulseP,tv_sugarP,tv_bp_lower,tv_Pulse,tv_sugar,tv_blood_oxygen_level,tv_temprature,tv_Hemoglobin,
                    tv_patient_remarkss, view_prescription_click;
            tv_assigned_doctor = inflatedView.findViewById(R.id.tv_assigned_doctor);
            tv_doctor_assigned = inflatedView.findViewById(R.id.tv_doctor_assigned);
            tv_patient_symptoms = inflatedView.findViewById(R.id.tv_patient_symptoms);
            tv_symptoms_patient = inflatedView.findViewById(R.id.tv_symptoms_patient);
            tv_taking_any_prescription = inflatedView.findViewById(R.id.tv_taking_any_prescription);
            tv_any_taking_prescription = inflatedView.findViewById(R.id.tv_any_taking_prescription);
            tv_date_taking_any_prescription = inflatedView.findViewById(R.id.tv_date_taking_any_prescription);
            tv_taking_prescription_any_date = inflatedView.findViewById(R.id.tv_taking_prescription_any_date);
            tv_any_emergency = inflatedView.findViewById(R.id.tv_any_emergency);
            tv_emergency_any = inflatedView.findViewById(R.id.tv_emergency_any);
            tv_patient_disease = inflatedView.findViewById(R.id.tv_patient_disease);
            tv_disease_patient = inflatedView.findViewById(R.id.tv_disease_patient);
            tv_test_done = inflatedView.findViewById(R.id.tv_test_done);
            tv_is_test_done = inflatedView.findViewById(R.id.tv_is_test_done);
            tv_test_done_date = inflatedView.findViewById(R.id.tv_test_done_date);
            tv_is_test_done_date = inflatedView.findViewById(R.id.tv_is_test_done_date);
            tv_assigned_doctor_date = inflatedView.findViewById(R.id.tv_assigned_doctor_date);
            tv_date_assigned_doctor = inflatedView.findViewById(R.id.tv_date_assigned_doctor);
            tv_assigned_pharmacists = inflatedView.findViewById(R.id.tv_assigned_pharmacists);
            tv_pharmacists_assigned = inflatedView.findViewById(R.id.tv_pharmacists_assigned);
            tv_assigned_pharmacists_date = inflatedView.findViewById(R.id.tv_assigned_pharmacists_date);
            tv_date_pharmacists_assigned = inflatedView.findViewById(R.id.tv_date_pharmacists_assigned);
            tv_medicine_delivered = inflatedView.findViewById(R.id.tv_medicine_delivered);
            tv_medicine_delivered_on = inflatedView.findViewById(R.id.tv_medicine_delivered_on);
            tv_is_verified = inflatedView.findViewById(R.id.tv_is_verified);
            tv_verified_is = inflatedView.findViewById(R.id.tv_verified_is);
            tv_assigned_by = inflatedView.findViewById(R.id.tv_assigned_by);
            tv_by_assigned = inflatedView.findViewById(R.id.tv_by_assigned);
            tv_appointment_type = inflatedView.findViewById(R.id.tv_appointment_type);
            tv_type_appointment = inflatedView.findViewById(R.id.tv_type_appointment);
            tv_remarks_by_doctor = inflatedView.findViewById(R.id.tv_remarks_by_doctor);
            tv_by_remarks_doctor = inflatedView.findViewById(R.id.tv_by_remarks_doctor);
            tv_is_doctor_done = inflatedView.findViewById(R.id.tv_is_doctor_done);
            tv_doctor_is_done = inflatedView.findViewById(R.id.tv_doctor_is_done);
            tv_is_doctor_done_on = inflatedView.findViewById(R.id.tv_is_doctor_done_on);
            tv_is_on_doctor_done = inflatedView.findViewById(R.id.tv_is_on_doctor_done);
            tv_is_pharmacists_done = inflatedView.findViewById(R.id.tv_is_pharmacists_done);
            tv_pharmacists_is_done = inflatedView.findViewById(R.id.tv_pharmacists_is_done);
            tv_is_pharmacists_done_on = inflatedView.findViewById(R.id.tv_is_pharmacists_done_on);
            tv_pharmacists_is_done_on = inflatedView.findViewById(R.id.tv_pharmacists_is_done_on);
            tv_patient_medicine = inflatedView.findViewById(R.id.tv_patient_medicine);
            tv_medicine_patient = inflatedView.findViewById(R.id.tv_medicine_patient);
            tv_days_prescription = inflatedView.findViewById(R.id.tv_days_prescription);
            tv_prescription_days = inflatedView.findViewById(R.id.tv_prescription_days);
            tv_interval_prescription = inflatedView.findViewById(R.id.tv_interval_prescription);
            tv_prescription_interval = inflatedView.findViewById(R.id.tv_prescription_interval);
            tv_eating_schedule_prescription = inflatedView.findViewById(R.id.tv_eating_schedule_prescription);
            tv_prescription_eating_schedule = inflatedView.findViewById(R.id.tv_prescription_eating_schedule);
            tv_sos_prescription = inflatedView.findViewById(R.id.tv_sos_prescription);
            tv_prescription_sos = inflatedView.findViewById(R.id.tv_prescription_sos);
            tv_name_test = inflatedView.findViewById(R.id.tv_name_test);
            tv_test_name = inflatedView.findViewById(R.id.tv_test_name);
            tv_name_subtest = inflatedView.findViewById(R.id.tv_name_subtest);
            tv_subtest_name = inflatedView.findViewById(R.id.tv_subtest_name);
            tv_test_docs = inflatedView.findViewById(R.id.tv_test_docs);
            iv_test_docs = inflatedView.findViewById(R.id.iv_test_docs);
            rv_test_docs_inprofile = inflatedView.findViewById(R.id.rv_test_docs_inprofile);
            tv_patient_remarks = inflatedView.findViewById(R.id.tv_patient_remarks);
            tv_patient_remarkss = inflatedView.findViewById(R.id.tv_patient_remarkss);
            btn_show_medical_info = inflatedView.findViewById(R.id.btn_show_medical_info);
            ll_medical_info = inflatedView.findViewById(R.id.ll_medical_info);
            view_prescription_click = inflatedView.findViewById(R.id.view_prescription_click);
            tv_bplowerP = inflatedView.findViewById(R.id.tv_bplowerP);
            tv_bpupperP = inflatedView.findViewById(R.id.tv_bpupperP);
            tv_PulseP = inflatedView.findViewById(R.id.tv_PulseP);
            tv_sugarP = inflatedView.findViewById(R.id.tv_sugarP);
            tv_tempratureP = inflatedView.findViewById(R.id.tv_tempratureP);
            tv_HemoglobinP = inflatedView.findViewById(R.id.tv_HemoglobinP);
            tv_bp_upper = inflatedView.findViewById(R.id.tv_bp_upper);
            tv_bp_lower = inflatedView.findViewById(R.id.tv_bp_lower);
            tv_Pulse = inflatedView.findViewById(R.id.tv_Pulse);
            tv_sugar = inflatedView.findViewById(R.id.tv_sugar);
            tv_blood_oxygen_level = inflatedView.findViewById(R.id.tv_blood_oxygen_level);
            tv_temprature = inflatedView.findViewById(R.id.tv_temprature);
            tv_Hemoglobin = inflatedView.findViewById(R.id.tv_Hemoglobin);
            tv_blood_oxygen_levelP = inflatedView.findViewById(R.id.tv_blood_oxygen_levelP);

            /*click here for button*/
            btn_show_medical_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //btn_show_medical_info.setVisibility(View.GONE);
                    //ll_medical_info.setVisibility(View.VISIBLE);
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    if (ll_medical_info.getVisibility() == View.VISIBLE) {
                        ll_medical_info.setVisibility(View.GONE);
                    } else {
                        ll_medical_info.setVisibility(View.VISIBLE);
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }
            });
            /*click here for doctor link*/
            //String view_prescription_url = singledata2.get("view_prescription_click").toString();
            view_prescription_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TO DO
                    Intent intent = new Intent(context, ViewPrescription.class);
                    intent.putExtra("appointment_id", id);
                    startActivity(intent);
                }
            });

            try {
                //appointments details of patient
                if (data2.length() > 0) {
                    singledata2 = new JSONObject(data2.get(i).toString());

                    try {
                        String incommingDate = singledata2.get("created_at").toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date newDate = sdf.parse(incommingDate);
                        sdf = new SimpleDateFormat("dd MMM yyyy HH:mm a");
                        String outputDate = sdf.format(newDate);
                        btn_show_medical_info.setText("Appointment on: " + outputDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!singledata2.getString("symptom_name").trim().equalsIgnoreCase("null")) {
                        if (!singledata2.getString("symptom_name").trim().equalsIgnoreCase("")) {
                            tv_patient_symptoms.setText(singledata2.getString("symptom_name").trim());
                        }else{
                            tv_patient_symptoms.setVisibility(View.GONE);
                            tv_symptoms_patient.setVisibility(View.GONE);
                        }
                    }else{
                        tv_patient_symptoms.setVisibility(View.GONE);
                        tv_symptoms_patient.setVisibility(View.GONE);
                    }
                    if (!singledata2.getString("disease_name").trim().equalsIgnoreCase("null")) {
                        if (!singledata2.getString("disease_name").trim().equalsIgnoreCase("")){
                            tv_disease_patient.setText(singledata2.getString("disease_name").trim());
                        }else{
                            tv_patient_disease.setVisibility(View.GONE);
                            tv_disease_patient.setVisibility(View.GONE);
                        }
                    }else{
                        tv_patient_disease.setVisibility(View.GONE);
                        tv_disease_patient.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("bp_lower").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("bp_lower").toString().equalsIgnoreCase("")) {
                            tv_bplowerP.setText(singledata2.get("bp_lower").toString());
                        }else{
                            tv_bp_lower.setVisibility(View.GONE);
                            tv_bplowerP.setVisibility(View.GONE);
                        }
                    }else{
                        tv_bp_lower.setVisibility(View.GONE);
                        tv_bplowerP.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("bp_upper").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("bp_upper").toString().equalsIgnoreCase("")) {
                            tv_bpupperP.setText(singledata2.get("bp_upper").toString());
                        }else{
                            tv_bpupperP.setVisibility(View.GONE);
                            tv_bp_upper.setVisibility(View.GONE);
                        }
                    }else{
                        tv_bpupperP.setVisibility(View.GONE);
                        tv_bp_upper.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("sugar").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("sugar").toString().equalsIgnoreCase("")) {
                            tv_sugarP.setText(singledata2.get("sugar").toString());
                        }else{
                            tv_sugarP.setVisibility(View.GONE);
                            tv_sugar.setVisibility(View.GONE);
                        }
                    }else{
                        tv_sugarP.setVisibility(View.GONE);
                        tv_sugar.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("pulse").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("pulse").toString().equalsIgnoreCase("")) {
                            tv_PulseP.setText(singledata2.get("pulse").toString());
                        }else {
                            tv_PulseP.setVisibility(View.GONE);
                            tv_Pulse.setVisibility(View.GONE);
                        }
                    }else {
                        tv_PulseP.setVisibility(View.GONE);
                        tv_Pulse.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("temperature").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("temperature").toString().equalsIgnoreCase("")) {
                            tv_tempratureP.setText(singledata2.get("temperature").toString());
                        }else {
                            tv_tempratureP.setVisibility(View.GONE);
                            tv_temprature.setVisibility(View.GONE);
                        }
                    }else {
                        tv_tempratureP.setVisibility(View.GONE);
                        tv_temprature.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("hemoglobin").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("hemoglobin").toString().equalsIgnoreCase("")) {
                            tv_HemoglobinP.setText(singledata2.get("hemoglobin").toString());
                        }else {
                            tv_HemoglobinP.setVisibility(View.GONE);
                            tv_Hemoglobin.setVisibility(View.GONE);
                        }
                    }else {
                        tv_HemoglobinP.setVisibility(View.GONE);
                        tv_Hemoglobin.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("is_pharmacists_done").toString().equalsIgnoreCase("null")&&
                            !singledata2.get("is_pharmacists_done").toString().equalsIgnoreCase("N")) {
                        if(!singledata2.get("is_pharmacists_done").toString().equalsIgnoreCase("")) {
                            if (singledata2.get("is_pharmacists_done").toString().equalsIgnoreCase("Y")) {
                                tv_pharmacists_is_done.setText("Yes");
                            } else {
                                tv_pharmacists_is_done.setText("No");
                            }
                        }else {
                            tv_pharmacists_is_done.setVisibility(View.GONE);
                            tv_is_pharmacists_done.setVisibility(View.GONE);
                        }
                    }else {
                        tv_pharmacists_is_done.setVisibility(View.GONE);
                        tv_is_pharmacists_done.setVisibility(View.GONE);
                        tv_pharmacists_is_done_on.setVisibility(View.GONE);
                        tv_is_pharmacists_done_on.setVisibility(View.GONE);

                    }
                    if (!singledata2.get("is_pharmacists_done_on").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("is_pharmacists_done_on").toString().equalsIgnoreCase("")) {
                            String incomingDate5 = singledata2.get("is_pharmacists_done_on").toString();
                            SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date newDate = sdf5.parse(incomingDate5);
                                sdf5 = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
                                String outputDate = sdf5.format(newDate);
                                tv_pharmacists_is_done_on.setText(outputDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else {
                            tv_pharmacists_is_done_on.setVisibility(View.GONE);
                            tv_is_pharmacists_done_on.setVisibility(View.GONE);
                        }
                    }else {
                        tv_pharmacists_is_done_on.setVisibility(View.GONE);
                        tv_is_pharmacists_done_on.setVisibility(View.GONE);

                    }
                    if (!singledata2.get("is_doctor_done").toString().equalsIgnoreCase("null")&&
                            !singledata2.get("is_doctor_done").toString().equalsIgnoreCase("N")) {
                        if (!singledata2.get("is_doctor_done").toString().equalsIgnoreCase("")) {
                            if (singledata2.get("is_doctor_done").toString().equalsIgnoreCase("Y")) {
                                tv_doctor_is_done.setText("Yes");
                            } else {
                                tv_doctor_is_done.setText("No");
                            }
                        }else{
                            tv_doctor_is_done.setVisibility(View.GONE);
                            tv_is_doctor_done.setVisibility(View.GONE);
                        }
                    }else{
                        tv_doctor_is_done.setVisibility(View.GONE);
                        tv_is_doctor_done.setVisibility(View.GONE);
                        tv_is_on_doctor_done.setVisibility(View.GONE);
                        tv_is_doctor_done_on.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("is_doctor_done_on").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("is_doctor_done_on").toString().equalsIgnoreCase("")) {
                            String incomingDate4 = singledata2.get("is_doctor_done_on").toString();
                            SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date newDate = sdf4.parse(incomingDate4);
                                sdf4 = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
                                String outputDate = sdf4.format(newDate);
                                tv_is_on_doctor_done.setText(outputDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else {
                            tv_is_on_doctor_done.setVisibility(View.GONE);
                            tv_is_doctor_done_on.setVisibility(View.GONE);
                        }
                    }else {
                        tv_is_on_doctor_done.setVisibility(View.GONE);
                        tv_is_doctor_done_on.setVisibility(View.GONE);
                    }
                    /*if (!singledata2.get("is_verified").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("is_verified").toString().equalsIgnoreCase("")) {
                            tv_verified_is.setText(singledata2.get("is_verified").toString());
                        }else {
                            tv_verified_is.setVisibility(View.GONE);
                            tv_is_verified.setVisibility(View.GONE);
                        }
                    }else {*/
                        tv_verified_is.setVisibility(View.GONE);
                        tv_is_verified.setVisibility(View.GONE);
                    /*}*/
                    if (!singledata2.get("blood_oxygen_level").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("blood_oxygen_level").toString().equalsIgnoreCase("")) {
                            tv_blood_oxygen_levelP.setText(singledata2.get("blood_oxygen_level").toString());
                        }else {
                            tv_blood_oxygen_levelP.setVisibility(View.GONE);
                            tv_blood_oxygen_level.setVisibility(View.GONE);
                        }
                    }else {
                        tv_blood_oxygen_levelP.setVisibility(View.GONE);
                        tv_blood_oxygen_level.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("is_emergency").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("is_emergency").toString().equalsIgnoreCase("")) {
                            if (singledata2.get("is_emergency").toString().equalsIgnoreCase("Y")) {
                                tv_any_emergency.setText("Yes");
                            } else {
                                tv_any_emergency.setText("No");
                            }
                        }else {
                            tv_any_emergency.setVisibility(View.GONE);
                            tv_emergency_any.setVisibility(View.GONE);
                        }
                    }else {
                        tv_any_emergency.setVisibility(View.GONE);
                        tv_emergency_any.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("assigned_pharmacists").toString().equals("0")) {
                        if (!singledata2.get("assigned_pharmacists").toString().equalsIgnoreCase("null")) {
                            if (!singledata2.get("assigned_pharmacists").toString().equalsIgnoreCase("")) {
                                String pharmecy_name = sqliteHelper.getPharmecyName(singledata2.get("assigned_pharmacists").toString(),"profile_pharmacists");
                             tv_pharmacists_assigned.setText(pharmecy_name);
                               // tv_pharmacists_assigned.setText(singledata2.get("assigned_pharmacists").toString());
                            } else {
                                tv_pharmacists_assigned.setVisibility(View.GONE);
                                tv_assigned_pharmacists.setVisibility(View.GONE);
                            }
                        }
                    }else {
                        tv_pharmacists_assigned.setVisibility(View.GONE);
                        tv_assigned_pharmacists.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("assigned_pharmacists_on").toString().equalsIgnoreCase("null") && !singledata2.get("assigned_pharmacists_on").toString().equalsIgnoreCase("0000-00-00 00:00:00") ) {
                        if (!singledata2.get("assigned_pharmacists_on").toString().equalsIgnoreCase("0000-00-00 00:00:00")) {
                            String incomingDate3 = singledata2.get("assigned_pharmacists_on").toString();
                            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date newDate = sdf3.parse(incomingDate3);
                                sdf3 = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
                                String outputDate = sdf3.format(newDate);
                                tv_date_pharmacists_assigned.setText(outputDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else {
                            tv_date_pharmacists_assigned.setVisibility(View.GONE);
                            tv_assigned_pharmacists_date.setVisibility(View.GONE);
                        }
                    }else {
                        tv_date_pharmacists_assigned.setVisibility(View.GONE);
                        tv_assigned_pharmacists_date.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("prescribed_medicine").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("prescribed_medicine").toString().equalsIgnoreCase("")) {
                            tv_taking_any_prescription.setText(singledata2.get("prescribed_medicine").toString());
                        }else {
                            tv_taking_any_prescription.setVisibility(View.GONE);
                            tv_any_taking_prescription.setVisibility(View.GONE);
                        }
                    }else {
                        tv_taking_any_prescription.setVisibility(View.GONE);
                        tv_any_taking_prescription.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("prescribed_medicine_date").toString().equalsIgnoreCase("null") && !singledata2.get("prescribed_medicine_date").toString().equalsIgnoreCase("0000-00-00") ) {
                        if (!singledata2.get("prescribed_medicine_date").toString().equalsIgnoreCase("")) {
                            String incomingDate = singledata2.get("prescribed_medicine_date").toString();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date newDate = sdf.parse(incomingDate);
                                sdf = new SimpleDateFormat("dd-MM-yyyy");
                                String outputDate = sdf.format(newDate);
                                tv_date_taking_any_prescription.setText(outputDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else {
                            tv_date_taking_any_prescription.setVisibility(View.GONE);
                            tv_taking_prescription_any_date.setVisibility(View.GONE);
                        }
                    }else {
                        tv_date_taking_any_prescription.setVisibility(View.GONE);
                        tv_taking_prescription_any_date.setVisibility(View.GONE);
                    }
                    medicinePrescribedModelAL=sqliteHelper.getMedicinePrescribed(id);
                    if(medicinePrescribedModelAL.size()>0) {
                        /*for dynamic table layout of medicine*/
                        TableLayout stk = inflatedView.findViewById(R.id.table_main);
                        // stk.setBackgroundResource(R.drawable.tableborder);
                        TableRow tbrow0 = new TableRow(this);
                        //tbrow0.setBackgroundColor(getResources().getColor(R.color.color_light_gray));
                        //tbrow0.setPadding(0,0,1,1);
                        TextView tv0 = new TextView(this);
                        tv0.setText("Medicine");
                        tv0.setTextColor(Color.BLACK);
                        // tv0.setBackgroundColor(getResources().getColor(R.color.color_light_gray));
                        tv0.setBackgroundResource(R.drawable.cell_border);
                        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                        tv0.setTypeface(boldTypeface);
                        tv0.setTextSize(16);
                        tbrow0.addView(tv0);

                        TextView tv1 = new TextView(this);
                        tv1.setText("Days of Prescription");
                        tv1.setTextColor(Color.BLACK);
                        Typeface DaysofPrescription = Typeface.defaultFromStyle(Typeface.BOLD);
                        tv1.setTypeface(DaysofPrescription);
                        tv1.setTextSize(16);
                        tv1.setBackgroundResource(R.drawable.cell_border);
                        tbrow0.addView(tv1);

                        TextView tv2 = new TextView(this);
                        tv2.setText("Prescription Interval");
                        tv2.setTextColor(Color.BLACK);
                        Typeface prescription = Typeface.defaultFromStyle(Typeface.BOLD);
                        tv2.setTypeface(prescription);
                        tv2.setTextSize(16);
                        tv2.setBackgroundResource(R.drawable.cell_border);
                        tbrow0.addView(tv2);

                        TextView tv3 = new TextView(this);
                        tv3.setText("Prescription Eating Schedule");
                        tv3.setTextColor(Color.BLACK);
                        Typeface prescriptionEating = Typeface.defaultFromStyle(Typeface.BOLD);
                        tv3.setTypeface(prescriptionEating);
                        tv3.setTextSize(16);
                        tv3.setBackgroundResource(R.drawable.cell_border);
                        tbrow0.addView(tv3);

                        TextView tv4 = new TextView(this);
                        tv4.setText("Prescription SOS");
                        tv4.setTextColor(Color.BLACK);
                        Typeface prescriptionSOS = Typeface.defaultFromStyle(Typeface.BOLD);
                        tv4.setTypeface(prescriptionSOS);
                        tv4.setTextSize(16);
                        tv4.setBackgroundResource(R.drawable.cell_border);
                        tbrow0.addView(tv4);

                        TextView tv5 = new TextView(this);
                        tv5.setText("Quantity Given by Pharmacist");
                        tv5.setTextColor(Color.BLACK);
                        Typeface QuantityByPharmacist = Typeface.defaultFromStyle(Typeface.BOLD);
                        tv5.setTypeface(QuantityByPharmacist);
                        tv5.setTextSize(16);
                        tv5.setBackgroundResource(R.drawable.cell_border);
                        tbrow0.addView(tv5);

                        stk.addView(tbrow0); //for add all column

                        String medicineIds = "";
                        String medicine = "";
                        String prescriptionIds = "";
                        String prescription_days = "";
                        String prescriptionIntervalIds = "";
                        String prescription_interval = "";
                        String prescriptionEatingScheduleIds = "";
                        String prescription_eating_schedule = "";
                        String prescription_sos = "";
                        String quantity_by_chemist = "";

                        if (medicinePrescribedModelAL.size() > 0) {
                            Gson gson = new Gson();
                            String listString = gson.toJson(
                                    medicinePrescribedModelAL,
                                    new TypeToken<ArrayList<AppointmentMedicinePrescribedModel>>() {
                                    }.getType());

                            JSONArray jsonArrayMedicine = null;
                            JSONObject jsonObjectMedicine = null;
                            try {
                                jsonArrayMedicine = new JSONArray(listString);
                                if (jsonArrayMedicine != null && jsonArrayMedicine.length() > 0) {
                                    for (int l = 0; l < jsonArrayMedicine.length(); l++) {
                                        JSONObject jsonObject = jsonArrayMedicine.getJSONObject(l);
                                        medicineIds = jsonObject.getString("medicine_id");
                                        medicine = sqliteHelper.getNameById("medicine_list", "medicine_name", "id", Integer.parseInt(medicineIds));
                                        prescriptionIds = jsonObject.getString("prescription_days_id");
                                        prescription_days = sqliteHelper.getNameById("prescription_days", "name", "id", Integer.parseInt(prescriptionIds));
                                        prescriptionIntervalIds = jsonObject.getString("prescription_interval_id");
                                        prescription_interval = sqliteHelper.getNameById("prescription_interval", "name", "id", Integer.parseInt(prescriptionIntervalIds));
                                        prescriptionEatingScheduleIds = jsonObject.getString("prescription_eating_schedule_id");
                                        prescription_eating_schedule = sqliteHelper.getNameById("prescription_eating_schedule", "name", "id", Integer.parseInt(prescriptionEatingScheduleIds));
                                        prescription_sos = jsonObject.getString("prescription_sos");
                                        quantity_by_chemist = jsonObject.getString("quantity_by_chemist");

                                        TableRow tbrow = new TableRow(this);
                                        TextView t1v = new TextView(this);
                                        t1v.setText(medicine);
                                        t1v.setTextColor(Color.BLACK);
                                        t1v.setBackgroundResource(R.drawable.cell_border);
                                        tbrow.addView(t1v);

                                        TextView t2v = new TextView(this);
                                        t2v.setText(prescription_days);
                                        t2v.setTextColor(Color.BLACK);
                                        t2v.setBackgroundResource(R.drawable.cell_border);
                                        tbrow.addView(t2v);

                                        TextView t3v = new TextView(this);
                                        t3v.setText(prescription_interval);
                                        t3v.setTextColor(Color.BLACK);
                                        t3v.setBackgroundResource(R.drawable.cell_border);
                                        tbrow.addView(t3v);

                                        TextView t4v = new TextView(this);
                                        t4v.setText(prescription_eating_schedule);
                                        t4v.setTextColor(Color.BLACK);
                                        t4v.setBackgroundResource(R.drawable.cell_border);
                                        tbrow.addView(t4v);

                                        TextView t5v = new TextView(this);
                                        t5v.setText(prescription_sos);
                                        t5v.setTextColor(Color.BLACK);
                                        t5v.setBackgroundResource(R.drawable.cell_border);
                                        tbrow.addView(t5v);

                                        TextView t6v = new TextView(this);
                                        t6v.setText(quantity_by_chemist);
                                        t6v.setTextColor(Color.BLACK);
                                        t6v.setBackgroundResource(R.drawable.cell_border);
                                        tbrow.addView(t6v);

                                        stk.addView(tbrow);
                                    }
                                }
                                else {
                                    tv_patient_medicine.setVisibility(View.GONE);
                                    tv_medicine_patient.setVisibility(View.GONE);
                                    tv_days_prescription.setVisibility(View.GONE);
                                    tv_prescription_days.setVisibility(View.GONE);
                                    tv_interval_prescription.setVisibility(View.GONE);
                                    tv_prescription_interval.setVisibility(View.GONE);
                                    tv_eating_schedule_prescription.setVisibility(View.GONE);
                                    tv_prescription_eating_schedule.setVisibility(View.GONE);
                                    tv_sos_prescription.setVisibility(View.GONE);
                                    tv_prescription_sos.setVisibility(View.GONE);
                                    stk.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (!singledata2.get("remarkrs_by_doctor").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("remarkrs_by_doctor").toString().equalsIgnoreCase("")) {
                            tv_by_remarks_doctor.setText(singledata2.get("remarkrs_by_doctor").toString());
                        }else {
                            tv_by_remarks_doctor.setVisibility(View.GONE);
                            tv_remarks_by_doctor.setVisibility(View.GONE);
                        }
                    }else {
                        tv_by_remarks_doctor.setVisibility(View.GONE);
                        tv_remarks_by_doctor.setVisibility(View.GONE);
                    }
                    String assignedDoctorId=singledata2.get("assigned_doctor").toString();
                    if (!assignedDoctorId.equalsIgnoreCase("")) {
                        tv_assigned_doctor.setText(sqliteHelper.getNameById("profile_doctors", "full_name", "id", Integer.parseInt(assignedDoctorId)));
                    }else {
                        tv_assigned_doctor.setVisibility(View.GONE);
                        tv_doctor_assigned.setVisibility(View.GONE);
                    }
                    if (!singledata2.get("assigned_doctor_on").toString().equalsIgnoreCase("null")) {
                        if (!singledata2.get("assigned_doctor_on").toString().equalsIgnoreCase("")) {
                            String incomingDate2 = singledata2.get("assigned_doctor_on").toString();
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date newDate = sdf2.parse(incomingDate2);
                                sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
                                String outputDate = sdf2.format(newDate);
                                tv_date_assigned_doctor.setText(outputDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else {
                            tv_date_assigned_doctor.setVisibility(View.GONE);
                            tv_assigned_doctor_date.setVisibility(View.GONE);
                        }
                    }else {
                        tv_date_assigned_doctor.setVisibility(View.GONE);
                        tv_assigned_doctor_date.setVisibility(View.GONE);
                    }
                }
                else {
                    tv_any_taking_prescription.setVisibility(View.GONE);
                    tv_taking_any_prescription.setVisibility(View.GONE);
                    tv_taking_prescription_any_date.setVisibility(View.GONE);
                    tv_date_taking_any_prescription.setVisibility(View.GONE);
                    tv_test_done.setVisibility(View.GONE);
                    tv_is_test_done.setVisibility(View.GONE);
                    tv_is_test_done_date.setVisibility(View.GONE);
                    tv_test_done_date.setVisibility(View.GONE);
                    tv_assigned_doctor.setVisibility(View.GONE);
                    tv_doctor_assigned.setVisibility(View.GONE);
                    tv_assigned_doctor_date.setVisibility(View.GONE);
                    tv_date_assigned_doctor.setVisibility(View.GONE);
                    tv_assigned_pharmacists.setVisibility(View.GONE);
                    tv_pharmacists_assigned.setVisibility(View.GONE);
                    tv_date_pharmacists_assigned.setVisibility(View.GONE);
                    tv_assigned_pharmacists_date.setVisibility(View.GONE);
                    tv_medicine_delivered_on.setVisibility(View.GONE);
                    tv_medicine_delivered.setVisibility(View.GONE);
                    tv_is_verified.setVisibility(View.GONE);
                    tv_verified_is.setVisibility(View.GONE);
                    tv_emergency_any.setVisibility(View.GONE);
                    tv_any_emergency.setVisibility(View.GONE);
                    tv_assigned_by.setVisibility(View.GONE);
                    tv_by_assigned.setVisibility(View.GONE);
                    tv_appointment_type.setVisibility(View.GONE);
                    tv_type_appointment.setVisibility(View.GONE);
                    tv_remarks_by_doctor.setVisibility(View.GONE);
                    tv_by_remarks_doctor.setVisibility(View.GONE);
                    tv_is_doctor_done.setVisibility(View.GONE);
                    tv_doctor_is_done.setVisibility(View.GONE);
                    tv_is_doctor_done_on.setVisibility(View.GONE);
                    tv_is_on_doctor_done.setVisibility(View.GONE);
                    tv_is_pharmacists_done.setVisibility(View.GONE);
                    tv_pharmacists_is_done.setVisibility(View.GONE);
                    tv_is_pharmacists_done_on.setVisibility(View.GONE);
                    tv_pharmacists_is_done_on.setVisibility(View.GONE);
                    tv_patient_disease.setVisibility(View.GONE);
                    tv_disease_patient.setVisibility(View.GONE);
                    tv_symptoms_patient.setVisibility(View.GONE);
                    tv_patient_symptoms.setVisibility(View.GONE);
                    tv_patient_medicine.setVisibility(View.GONE);
                    tv_medicine_patient.setVisibility(View.GONE);
                    tv_days_prescription.setVisibility(View.GONE);
                    tv_prescription_days.setVisibility(View.GONE);
                    tv_interval_prescription.setVisibility(View.GONE);
                    tv_prescription_interval.setVisibility(View.GONE);
                    tv_eating_schedule_prescription.setVisibility(View.GONE);
                    tv_prescription_eating_schedule.setVisibility(View.GONE);
                    tv_sos_prescription.setVisibility(View.GONE);
                    tv_prescription_sos.setVisibility(View.GONE);
                    tv_name_test.setVisibility(View.GONE);
                    tv_test_name.setVisibility(View.GONE);
                    tv_name_subtest.setVisibility(View.GONE);
                    tv_subtest_name.setVisibility(View.GONE);
                    tv_test_docs.setVisibility(View.GONE);
                    iv_test_docs.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ll_for_dynamic_add.addView(inflatedView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        /*hide and show */
        if (assigned_appointments.equalsIgnoreCase("assigned_appointments")
                || already_prescribed_medicine.equalsIgnoreCase("already_prescribed_medicine")
                || not_prescribed_medicine.equalsIgnoreCase("not_prescribed_medicine")
                || total_medicine_delivered.equalsIgnoreCase("total_medicine_delivered")
                || pending_medicine_delivered.equalsIgnoreCase("pending_medicine_delivered")
                || total_handel_cases.equalsIgnoreCase("total_handel_cases")
                || test_pending_pharmacy.equalsIgnoreCase("test_pending_pharmacy")) {
        }else {
            MenuItem item = menu.findItem(R.id.addAppointment);
            item.setVisible(true);
        }
            MenuItem item = menu.findItem(R.id.Appointment);
            item.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(context, PatientProfileList.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.addAppointment) {
            Intent intent = new Intent(CommonProfile.this, SignUp.class);
            intent.putExtra("commomProfile", "commomProfile");
            intent.putExtra("profile_id", profile_id);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.Appointment) {
            Intent intent = new Intent(CommonProfile.this, PatientFillAppointment.class);
           // intent.putExtra("fromCounselor", "fromCounselor");
            intent.putExtra("fromcommon", "fromcommon");
            intent.putExtra("patient", "patient");
            intent.putExtra("profile_patient_id", profile_id);
            startActivity(intent);
            
        }

            return super.onOptionsItemSelected(item);
        }

        private void callDoctor (RequestBody body){
            APIClient.getClient().create(TELEMEDICINE_API.class).callFromDoctorApi(body).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        mProgressDialog.dismiss();
                        Log.e("hdss", "sbhdd " + jsonObject.toString());
                        String success = jsonObject.optString("success");
                        String message = jsonObject.optString("message");
                        String did_no = jsonObject.optString("did_no");
                        String call_id = jsonObject.optString("call_id");
                        /*save call id in preference and send at the time of common profile*/
                        sharedPrefHelper.setString("call_id", call_id);

                        if (success.equalsIgnoreCase("1")) {
                            //dial call
                            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + did_no));
                            startActivity(callIntent);

                        } else {
                            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            Intent intent = new Intent(context, PatientProfileList.class);
            startActivity(intent);
            finish();
    }
}


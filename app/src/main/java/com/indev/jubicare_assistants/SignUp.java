package com.indev.jubicare_assistants;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    private int mYear;
    private int mMonth;
    private int mDay;
    @BindView(R.id.et_name)
    EditText et_name;
    static EditText et_date_of_birth;
    static EditText et_age;
    @BindView(R.id.et_aadhar_card)
    EditText et_aadhar_card;
    @BindView(R.id.et_contact_number)
    EditText et_contact_number;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.et_emergency_contact)
    EditText et_emergency_contact;
    @BindView(R.id.et_pin_code)
    EditText et_pin_code;
    @BindView(R.id.et_height)
    EditText et_height;
    @BindView(R.id.et_weight)
    EditText et_weight;
    @BindView(R.id.rg_gender)
    RadioGroup rg_gender;
    @BindView(R.id.rg_disability)
    RadioGroup rg_disability;
    @BindView(R.id.rb_male)
    RadioButton rb_male;
    @BindView(R.id.rb_yes)
    RadioButton rb_yes;
    @BindView(R.id.rb_no)
    RadioButton rb_no;
    @BindView(R.id.rb_female)
    RadioButton rb_female;
    @BindView(R.id.rb_other)
    RadioButton rb_other;
    @BindView(R.id.iv_profile_image)
    ImageView iv_profile_image;

    @BindView(R.id.spn_caste)
    Spinner spn_caste;
    @BindView(R.id.spn_state)
    Spinner spn_state;
    @BindView(R.id.spn_blood_Group)
    Spinner spn_blood_Group;
    @BindView(R.id.ll_spn_blood_Group)
    LinearLayout ll_spn_blood_Group;
    @BindView(R.id.ll_blood_group)
    LinearLayout ll_blood_group;
    @BindView(R.id.et_blood_group)
    EditText et_blood_group;
    @BindView(R.id.spn_district)
    Spinner spn_district;
    @BindView(R.id.spn_village)
    Spinner spn_village;
    @BindView(R.id.spn_block)
    Spinner spn_block;
    @BindView(R.id.spn_month)
    Spinner spn_month;

    @BindView(R.id.btn_sign_up)
    Button btn_sign_up;
    /*new changes on 12-05-2020*/
    @BindView(R.id.et_state)
    EditText et_state;
    @BindView(R.id.et_district)
    EditText et_district;
    @BindView(R.id.et_block)
    EditText et_block;
    @BindView(R.id.spn_post_office)
    Spinner spn_post_office;

    /*normal widgets*/
    private Context context = this;
    SharedPrefHelper sharedPrefHelper;
    SqliteHelper sqliteHelper;
    SignUpModel signUpModel;
    ProgressDialog mProgressDialog;
    String name = "", gender = "", date_of_birth = "",
            aadhar_card = "", contact_number = "", address = "",
            emergency_contact = "", pincode = "", weight = "", height = "", bp_high = "", bp_low = "", sugar = "";
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 2000;
    String encodedImage = "";
    String add = "";
    String fromcommon = "";
    String month = "";
    String Newadd = "";
    String commomProfile = "";
    String select_caste = "";
int blood_group_id;
    String profile_id = "";
    String select_blood = "";
    int state_id, district_id, block_id, post_office_id, village_id;
    HashMap<String, Integer> stateNameHM, districtNameHM, blockNameHM, postOfficeNameHM, villageNameHM, casteNameHM,bloodNameHM;
    ArrayList<String> stateArrayList, distrcitArrayList, blockArrayList, postOfficeArrayList, villageArrayList, casteArrayList,bloodArrayList;
    ArrayList<String> postOfficeNameAL = new ArrayList<>();
    String postOfficeName = "";
    ArrayList<String> districtAL = new ArrayList<>();
    String districtName = "";
    ArrayList<String> blockAL = new ArrayList<>();
    String blockName = "";
    ArrayList<String> bloodGroupArrayList = new ArrayList<>();
    String bloodGroup = "";
    String caste = "";
    String bloodGroupId = "";
    String blood_group_id_edit = "";
    /*for validation*/
    private static EditText flagEditfield;
    private static String msg = "";
    PatientFilledDataModel patientFilledDataModel;
    String state_id_edit = "", district_id_edit = "", block_id_edit = "", post_office_id_edit = "", village_id_edit = "";
    /*set selection*/
    boolean isEditable = false;
    String state_name = "";
    String cast_name = "";
    String district_name = "";
    String block_name = "";
    String village_name = "";
    String post_office_name = "";
    String blood_group_name = "";
    android.app.Dialog add_profile_alert;
    String coveredArea = "Y";
    private String[] masterTables = {"state", "district", "block", "village", "post_office", "symptom", "disease", "medicine_list", "test", "sub_tests", "prescription_eating_schedule", "prescription_days", "prescription_interval", "blood_group","caste"};
    @BindView(R.id.rg_age)
    RadioGroup rg_age;
    @BindView(R.id.rb_age)
    RadioButton rb_age;
    @BindView(R.id.rb_dob)
    RadioButton rb_dob;
    public static String age_in_month;
    @BindView(R.id.ll_age)
    LinearLayout ll_age;
    @BindView(R.id.ll_dob)
    LinearLayout ll_dob;
    String disability = "N/A";
    int caste_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        initViews();
        signUpModel = new SignUpModel();

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR); // current year
        mMonth = c.get(Calendar.MONTH); // current month
        mDay = c.get(Calendar.DAY_OF_MONTH); //current Day.

        /*download master tables here*/
        getMasterTables(SignUp.this);

        /*for change title dynamically*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            add = bundle.getString("addMember", "");
            Newadd = bundle.getString("New Patient", "");
            commomProfile = bundle.getString("commomProfile", "");
            profile_id = bundle.getString("profile_id", "");

        }

        //change dynamically title here
        if (add.equalsIgnoreCase("addMember")) {
            setTitle("Add Member");
        } else if (commomProfile.equals("commomProfile")) {
            setTitle("Edit Profile");
        } else if(Newadd.equalsIgnoreCase("New Patient")) {
            setTitle("New Patient");
        }else {
            setTitle("Add Profile");
        }

        rg_disability.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_yes:
                        disability = "Yes";
                        break;
                    case R.id.rb_no:
                        disability = "No";
                        break;
                }
            }
        });


        if (commomProfile.equals("commomProfile")) {
            //mProgressDialog = ProgressDialog.show(context ,"", "Please wait", true);
//            patientFilledDataModel.setProfile_patient_id(profile_id);
//            patientFilledDataModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
//            patientFilledDataModel.setRole_id(sharedPrefHelper.getString("role_id", ""));
//
//            Gson gson = new Gson();
//            String data = gson.toJson(patientFilledDataModel);
//            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//            RequestBody body = RequestBody.create(JSON, data);
//            getDetailsPatientAlreadyFilled(body);

            signUpModel = sqliteHelper. getPatientDetail(profile_id);
            et_name.setText(signUpModel.getFull_name());
            if (signUpModel.getGender().equalsIgnoreCase("M")) {
                rb_male.setChecked(true);
            } else if (signUpModel.getGender().equalsIgnoreCase("F")) {
                rb_female.setChecked(true);
            } else {
                rb_other.setChecked(true);
            }
            if (signUpModel.getDisability().equals("Yes")) {
                rb_yes.setChecked(true);
            } else if (signUpModel.getDisability().equals("No")) {
                rb_no.setChecked(true);
            }

            if (signUpModel.getCaste_id().equals("") || signUpModel.getCaste_id()!=null){
                cast_name = sqliteHelper.getstateName(signUpModel.getCaste_id(), "caste");
//                spn_caste.setSelection(Integer.parseInt(caste_id));
            }

            if (signUpModel.getAge().equalsIgnoreCase("age")) {
                rb_age.setChecked(true);
            } else    {
                rb_dob.setChecked(true);
            }

            et_date_of_birth.setText(signUpModel.getDob());
//            if (signUpModel != null) {
//                spn_month.setSelection(Integer.parseInt(signUpModel.getAge_month()));
//            }

            et_height.setText(signUpModel.getHeight());
            et_weight.setText(signUpModel.getWeight());
            et_age.setText(signUpModel.getAge());
            et_aadhar_card.setText(signUpModel.getAadhar_no());
            et_contact_number.setText(signUpModel.getContact_no());
            et_emergency_contact.setText(signUpModel.getEmergency_contact_no());
            et_contact_number.setText(signUpModel.getContact_no());
            et_address.setText(signUpModel.getAddress());
            if (!signUpModel.getPin_code().equalsIgnoreCase("null") && !signUpModel.getPin_code().equalsIgnoreCase("0")) {
                et_pin_code.setText(signUpModel.getPin_code());
            } else {
                et_pin_code.setText("");
            }
            String url = APIClient.IMAGE_URL + signUpModel.getProfile_pic();
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.camera_icon)
                    .into(iv_profile_image);


        }
        //send sign up data
        getTextFromFields();

        getAllStateFromTable();
        getAllCasteFromTable();
        getAllBloodFromTable();
        setCasteSpinner();

        setStateSpinner();
        setDistrictSpinner();
        setBlockSpinner();
        setBloodkSpinner();
        //setPostOfficeSpinner();
        setVillageSpinner();
        //setBloodGroupSpinner();
        setSpinnermonth();
        setSpinner();
        //callPinCodeApi();
        //setSpinnerDistrict();
        //setSpinnerBlock();
        //setSpinnerPostOffice();
        //setEditableFalse();

        /*get date of birth form age*/
        rg_age.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_age:
                        ll_age.setVisibility(View.VISIBLE);
                        ll_dob.setVisibility(View.GONE);
                        getDateOfBirthFromAge();
                        break;
                    case R.id.rb_dob:
                        ll_dob.setVisibility(View.VISIBLE);
                        ll_age.setVisibility(View.GONE);
                        break;
                }
            }
        });
        /*if (et_date_of_birth.getText().toString().equalsIgnoreCase("")) {
            getDateOfBirthFromAge();
        }*/


        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_male:
                        gender = "M";
                        break;
                    case R.id.rb_female:
                        gender = "F";
                        break;
                    case R.id.rb_other:
                        gender = "O";
                        break;
                }
            }
        });
    }

    private void getDateOfBirthFromAge() {
        et_age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                float aa = 0;
                int year = 0;
                int month = 0;
                String ageee = et_age.getText().toString().trim();
                if (ageee.length() > 0) {
                    if (ageee.contains(".")) {
                        String[] dobDate = ageee.split("\\.");
                        year = Integer.parseInt(dobDate[0]);
                        if (dobDate.length > 1) {
                            month = Integer.parseInt(dobDate[1]);
                        }
                    } else {
                        year = Integer.parseInt(ageee);
                    }
                    if (et_age.getText().toString().trim().equalsIgnoreCase("")) {
                        year = 0;
                    } else {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - year);
                        calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 1 - month);
                        calendar.set(Calendar.DAY_OF_MONTH, 0);
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd", Locale.getDefault());
                        String formatted = dateFormat.format(calendar.getTime());
                        et_date_of_birth.setText(formatted);
                    }
                } else {
                    et_date_of_birth.setText("");
                }
            }
        });
    }

//    private void setCasteSpinner() {
//        casteArrayList = sqliteHelper.getspnCasteData();
//        spn_caste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (!spn_caste.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_caste))) {
//                    int index = spn_caste.getSelectedItemPosition();
//                    caste = casteArrayList.get(index);
//                    caste_id = sqliteHelper.getSelectedItemId("caste", caste);
//                    //Toast.makeText(context, "" + bloodGroupId, Toast.LENGTH_SHORT).show();
//                } else {
//                    caste_id = String.valueOf(1);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        casteArrayList.add(0, getString(R.string.select_caste));
//        final ArrayAdapter Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, casteArrayList);
//        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spn_caste.setAdapter(Adapter);
////        if (!signUpModel.getCaste_id().equals("null")) {
////            int spinnerPosition = 0;
////            String strpos1 = sqliteHelper.getstateName(signUpModel.getCaste_id(), "caste");
////            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
////                spinnerPosition = Adapter.getPosition(strpos1);
////                spn_caste.setSelection(spinnerPosition);
////                spinnerPosition = 0;
////            }
////
////        }
//    }
//
//    private void setBloodGroupSpinner() {
//        bloodGroupArrayList = sqliteHelper.getspnBloodGroupData();
//        spn_blood_Group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (!spn_blood_Group.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_bloodgroup))) {
//                    int index = spn_blood_Group.getSelectedItemPosition();
//                    bloodGroup = bloodGroupArrayList.get(index);
//                    bloodGroupId = sqliteHelper.getSelectedItemId("blood_group", bloodGroup);
//                    //Toast.makeText(context, "" + bloodGroupId, Toast.LENGTH_SHORT).show();
//                } else {
//                    bloodGroupId = String.valueOf(9);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        bloodGroupArrayList.add(0, getString(R.string.select_bloodgroup));
//        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.simple_spinner_item, bloodGroupArrayList);
//        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spn_blood_Group.setAdapter(Adapter);
////        if (!signUpModel.getBlood_group_id().equals("null")) {
////            int spinnerPosition = 0;
////            String strpos1 = sqliteHelper.getstateName(signUpModel.getBlood_group_id(), "blood_group");
////            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
////                spinnerPosition = Adapter.getPosition(strpos1);
////                spn_blood_Group.setSelection(spinnerPosition);
////                spinnerPosition = 0;
////            }
////
////        }
//    }
private void setSpinnermonth() {
    ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.months));
    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spn_month.setAdapter(aa);

}

    private void setSpinner() {


//        spn_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (!spn_month.getSelectedItem().toString().trim()) {
//                    if (spn_month.getSelectedItem().toString().trim() != null) {
//                        month = spn_month.getSelectedItem().toString().trim();
//
//                    }
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//
//        });


            spn_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    month = spn_month.getSelectedItem().toString().trim();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
    }
    private void setSpinnerDistrict() {
        /*Set<String> set = new HashSet<>(districtAL);
        districtAL.clear();
        districtAL.addAll(set);*/
        spn_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = spn_district.getSelectedItemPosition();
                districtName = districtAL.get(index);
                //Toast.makeText(context, ""+districtName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        districtAL.add(0, getString(R.string.select_district));
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.simple_spinner_item, districtAL);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_district.setAdapter(Adapter);
    }

    private void setSpinnerBlock() {
        spn_block.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = spn_block.getSelectedItemPosition();
                blockName = blockAL.get(index);
                //Toast.makeText(context, ""+blockName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        blockAL.add(0, getString(R.string.select_block));
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.simple_spinner_item, blockAL);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_block.setAdapter(Adapter);
    }

    private void setSpinnerPostOffice() {
        spn_post_office.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = spn_post_office.getSelectedItemPosition();
                postOfficeName = postOfficeNameAL.get(index);
                //Toast.makeText(context, ""+postOfficeName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        postOfficeNameAL.add(0, getString(R.string.select_post_office));
        final ArrayAdapter Adapter = new ArrayAdapter(this, R.layout.simple_spinner_item, postOfficeNameAL);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_post_office.setAdapter(Adapter);
    }

    private void setEditableFalse() {
        et_state.setEnabled(false);
        /*et_district.setEnabled(false);
        et_block.setEnabled(false);*/
    }

//    private void getDetailsPatientAlreadyFilled(RequestBody body) {
//        TELEMEDICINE_API api_service = APIClient.getClient().create(TELEMEDICINE_API.class);
//        if (body != null && api_service != null) {
//            Call<JsonObject> server_response = api_service.download_profile(body);
//            try {
//                if (server_response != null) {
//                    server_response.enqueue(new Callback<JsonObject>() {
//                        @Override
//                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                            if (response.isSuccessful()) {
//                                JsonObject singledataP = response.body();
//                                Log.e("hbshbshb-jxb", "onResponse: " + singledataP.toString());
//                                mProgressDialog.dismiss();
//                                JsonArray data = singledataP.getAsJsonArray("tableData");
//                                JsonArray data2 = singledataP.getAsJsonArray("Appointmenthistory");
//                                // comment because they send Appointmenthistory = null instead of Appointmenthistory = [null]
//
//                                JSONObject singledata = null;
//                                JSONObject singledata2 = null;
//                                try {
//                                    if (!data.isJsonNull() && data.size() > 0) {
//                                        singledata = new JSONObject(data.get(0).toString());
//
//                                        String full_name = singledata.getString("full_name");
//                                        String contact_no = singledata.getString("contact_no");
//                                        String gender = singledata.getString("gender");
//                                        String age = singledata.getString("age");
//                                        String height = singledata.getString("height");
//                                        String weight = singledata.getString("weight");
//                                        blood_group_name = singledata.getString("blood_group_name");
//                                        String dob = singledata.getString("dob");
//                                        String aadhar_no = singledata.getString("aadhar_no");
//                                        String pin_code = singledata.getString("pin_code");
//                                        String state_id = singledata.getString("state_id");
//                                        state_name = singledata.getString("state_name");
//                                        String district_id = singledata.getString("district_id");
//                                        district_name = singledata.getString("district_name");
//                                        String block_id = singledata.getString("block_id");
//                                        block_name = singledata.getString("block_name");
//                                        String village_id = singledata.getString("village_id");
//                                        village_name = singledata.getString("village_name");
//                                        String post_office_id = singledata.getString("post_office_id");
//                                        post_office_name = singledata.getString("post_office_name");
//                                        String address = singledata.getString("address");
//                                        String caste_name = singledata.getString("caste_name");
//                                        String disability = singledata.getString("disability");
//                                        String emergency_contact_no = singledata.getString("emergency_contact_no");
//                                        if (disability.equals("Yes")) {
//                                            rb_yes.setChecked(true);
//                                        } else if (disability.equals("No")) {
//                                            rb_no.setChecked(true);
//                                        }
////                                        if (caste_name.equals("") || caste_name!=null){
////                                                caste_id = sqliteHelper.getSelectedItemId("caste",caste_name);
////                                                spn_caste.setSelection(Integer.parseInt(caste_id));
////                                        }
//                                        et_name.setText(full_name);
//                                        if (gender.equalsIgnoreCase("M")) {
//                                            rb_male.setChecked(true);
//                                        } else if (gender.equalsIgnoreCase("F")) {
//                                            rb_female.setChecked(true);
//                                        } else {
//                                            rb_other.setChecked(true);
//                                        }
//
//                                        if (age.equalsIgnoreCase("age")) {
//                                            rb_age.setChecked(true);
//                                        } else    {
//                                            rb_dob.setChecked(true);
//                                        }
//                                        et_date_of_birth.setText(dob);
//                                        et_height.setText(height);
//                                        et_weight.setText(weight);
//                                        et_age.setText(age);
//                                        et_aadhar_card.setText(aadhar_no);
//                                        et_contact_number.setText(contact_no);
//                                        et_emergency_contact.setText(emergency_contact_no);
//                                        et_contact_number.setText(contact_no);
//                                        et_address.setText(address);
//                                        if (!pin_code.equalsIgnoreCase("") && !pin_code.equalsIgnoreCase("0")) {
//                                            et_pin_code.setText(pin_code);
//                                        } else {
//                                            et_pin_code.setText("");
//                                        }
//                                        et_state.setText(state_name);
//                                        et_district.setText(district_name);
//                                        et_block.setText(block_name);
//                                        if (!blood_group_name.equalsIgnoreCase("null")) {
//                                            et_blood_group.setText(blood_group_name);
//                                        } else {
//                                            et_blood_group.setText("");
//                                        }
//                                        /*set selection*/
//                                        if (blood_group_name.equalsIgnoreCase("O+")) {
//                                            spn_blood_Group.setSelection(1);//spinner set selected with item position
//                                        }
//                                        if (blood_group_name.equalsIgnoreCase("O-")) {
//                                            spn_blood_Group.setSelection(2);//spinner set selected with item position
//                                        }
//                                        if (blood_group_name.equalsIgnoreCase("A+")) {
//                                            spn_blood_Group.setSelection(3);//spinner set selected with item position
//                                        }
//                                        if (blood_group_name.equalsIgnoreCase("A-")) {
//                                            spn_blood_Group.setSelection(4);//spinner set selected with item position
//                                        }
//                                        if (blood_group_name.equalsIgnoreCase("B+")) {
//                                            spn_blood_Group.setSelection(5);//spinner set selected with item position
//                                        }
//                                        if (blood_group_name.equalsIgnoreCase("B-")) {
//                                            spn_blood_Group.setSelection(6);//spinner set selected with item position
//                                        }
//                                        if (blood_group_name.equalsIgnoreCase("AB+")) {
//                                            spn_blood_Group.setSelection(7);//spinner set selected with item position
//                                        }
//                                        if (blood_group_name.equalsIgnoreCase("AB-")) {
//                                            spn_blood_Group.setSelection(8);//spinner set selected with item position
//                                        }
//                                    }
//                                    isEditable = true;
//                                    getAllStateFromTable();
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonObject> call, Throwable t) {
//                            Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                            mProgressDialog.dismiss();
//                        }
//
//                    });
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//            }
//        }
//    }

    private void callPinCodeApi() {
        et_pin_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                et_state.setText("");
                et_district.setText("");
                et_block.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //sendPinCodeData();
            }
        });
    }



    private void getAllStateFromTable() {
        stateArrayList.clear();
        stateNameHM = sqliteHelper.getAllState();
        for (int i = 0; i < stateNameHM.size(); i++) {
            stateArrayList.add(stateNameHM.keySet().toArray()[i].toString().trim());
        }
        Collections.sort(stateArrayList);
        if (isEditable) {
            stateArrayList.add(0, state_name);
            getAllDistrictFromTable(0);
            getAllBlockFromTable(0);
          //  getAllPostOfficeFromTable(0);
            getAllVillageFromTable(0);
        } else {
            stateArrayList.add(0, getString(R.string.select_state));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stateArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_state.setAdapter(Adapter);
        if (!signUpModel.getState_id().equals("null")) {
            int spinnerPosition = 0;
            String strpos1 = sqliteHelper.getstateName(signUpModel.getState_id(), "state");
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_state.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }

        }
    }


    private void setStateSpinner() {
        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!spn_state.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_state))) {
                    if (spn_state.getSelectedItem().toString().trim() != null) {
                        state_id = stateNameHM.get(spn_state.getSelectedItem().toString().trim());
                        getAllDistrictFromTable(state_id);
                    }

                } else {
                    getAllDistrictFromTable(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getAllDistrictFromTable(int state_id) {
        distrcitArrayList.clear();
        districtNameHM = sqliteHelper.getAllDistrict(state_id);
        for (int i = 0; i < districtNameHM.size(); i++) {
            distrcitArrayList.add(districtNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            distrcitArrayList.add(0, district_name);
        } else {
            distrcitArrayList.add(0, getString(R.string.select_district));
        }

        final ArrayAdapter Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, distrcitArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_district.setAdapter(Adapter);
        if (!signUpModel.getDistrict_id().equals("null")) {
            int spinnerPosition = 0;
            String strpos1 = sqliteHelper.getstateName(signUpModel.getDistrict_id(), "district");
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_district.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }

        }
    }


    private void setDistrictSpinner() {
        spn_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_district.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_district))) {
                    if (spn_district.getSelectedItem().toString().trim() != null) {
                        district_id = districtNameHM.get(spn_district.getSelectedItem().toString().trim());
                        getAllBlockFromTable(district_id);
                    }
                } else {
                    getAllBlockFromTable(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllBlockFromTable(int district_id) {
        blockArrayList.clear();
        blockNameHM = sqliteHelper.getAllBlock(district_id);
        for (int i = 0; i < blockNameHM.size(); i++) {
            blockArrayList.add(blockNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            blockArrayList.add(0, block_name);
        } else {
            blockArrayList.add(0, getString(R.string.select_block));
            //blockArrayList.add(getString(R.string.Other));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, blockArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_block.setAdapter(Adapter);
        if (!signUpModel.getBlock_id().equals("null")) {
            int spinnerPosition = 0;
            String strpos1 = sqliteHelper.getstateName(signUpModel.getBlock_id(), "block");
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_block.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }

        }
    }

    private void setBlockSpinner() {
        spn_block.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_block.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_block))) {
                    if (spn_block.getSelectedItem().toString().trim() != null) {
                        block_id = blockNameHM.get(spn_block.getSelectedItem().toString().trim());
                        getAllVillageFromTable(block_id);
                    }
                } else {
                    getAllVillageFromTable(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

//    private void getAllPostOfficeFromTable(int block_id) {
//        postOfficeArrayList.clear();
//        postOfficeNameHM = sqliteHelper.getAllPostOffice(block_id);
//        for (int i = 0; i < postOfficeNameHM.size(); i++) {
//            postOfficeArrayList.add(postOfficeNameHM.keySet().toArray()[i].toString().trim());
//        }
//        if (isEditable) {
//            postOfficeArrayList.add(0, post_office_name);
//        } else {
//            postOfficeArrayList.add(0, getString(R.string.select_post_office));
//            //postOfficeArrayList.add(1, getString(R.string.Other));
//        }
//
//        final ArrayAdapter Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, postOfficeArrayList);
//        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spn_post_office.setAdapter(Adapter);
//        if (!signUpModel.getPost_office_id().equals("null")) {
//            int spinnerPosition = 0;
//            String strpos1 = sqliteHelper.getstateName(signUpModel.getPost_office_id(), "post_office");
//            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
//                spinnerPosition = Adapter.getPosition(strpos1);
//                spn_post_office.setSelection(spinnerPosition);
//                spinnerPosition = 0;
//            }
//
//        }
//    }
//
//    private void setPostOfficeSpinner() {
//        spn_post_office.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (!spn_post_office.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_post_office))) {
//                    if (spn_post_office.getSelectedItem().toString().trim() != null) {
//                        post_office_id = postOfficeNameHM.get(spn_post_office.getSelectedItem().toString().trim());
//                        getAllVillageFromTable(post_office_id);
//                    }
//                } else {
//                    getAllVillageFromTable(0);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }

    private void getAllVillageFromTable(int block_id) {
        villageArrayList.clear();
        villageNameHM = sqliteHelper.getAllVillage(block_id);
        for (int i = 0; i < villageNameHM.size(); i++) {
            villageArrayList.add(villageNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            villageArrayList.add(0, village_name);
        } else {
            villageArrayList.add(0, getString(R.string.select_village));
            //villageArrayList.add(1, getString(R.string.Other));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, villageArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_village.setAdapter(Adapter);
        if (!signUpModel.getVillage_id().equals("null")) {
            int spinnerPosition = 0;
            String strpos1 = sqliteHelper.getstateName(signUpModel.getVillage_id(), "village");
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_village.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }

        }
    }

    private void setVillageSpinner() {
        spn_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_village.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_village))) {
                    village_id = villageNameHM.get(spn_village.getSelectedItem().toString().trim());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllCasteFromTable() {
        casteArrayList.clear();
        casteNameHM = sqliteHelper.getAllCaste();
        for (int i = 0; i < casteNameHM.size(); i++) {
            casteArrayList.add(casteNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            casteArrayList.add(0, select_caste);
        } else {
            casteArrayList.add(0, getString(R.string.select_caste));
            //blockArrayList.add(getString(R.string.Other));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, casteArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_caste.setAdapter(Adapter);
                if (!signUpModel.getCaste_id().equals("null")) {
            int spinnerPosition = 0;
            String strpos1 = sqliteHelper.getstateName(signUpModel.getCaste_id(), "caste");
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_caste.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }

        }

    }
    private void setCasteSpinner() {
        spn_caste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_caste.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_caste))) {
                    caste_id = casteNameHM.get(spn_caste.getSelectedItem().toString().trim());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllBloodFromTable() {
        bloodArrayList.clear();
        bloodNameHM = sqliteHelper.getAllblood_group();
        for (int i = 0; i < bloodNameHM.size(); i++) {
            bloodArrayList.add(bloodNameHM.keySet().toArray()[i].toString().trim());
        }
        if (isEditable) {
            bloodArrayList.add(0, select_blood);
        } else {
            bloodArrayList.add(0, getString(R.string.select_bloodgroup));
            //blockArrayList.add(getString(R.string.Other));
        }
        final ArrayAdapter Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodArrayList);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_blood_Group.setAdapter(Adapter);
        if (!signUpModel.getBlood_group_id().equals("null")) {
            int spinnerPosition = 0;
            String strpos1 = sqliteHelper.getstateName(signUpModel.getBlood_group_id(), "blood_group");
            if (strpos1 != null || !strpos1.equals(null) || !strpos1.equals("")) {
                spinnerPosition = Adapter.getPosition(strpos1);
                spn_blood_Group.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }

        }

    }
    private void setBloodkSpinner() {
        spn_blood_Group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spn_blood_Group.getSelectedItem().toString().trim().equalsIgnoreCase(getString(R.string.select_bloodgroup))) {
                    blood_group_id = bloodNameHM.get(spn_blood_Group.getSelectedItem().toString().trim());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initViews() {
        et_date_of_birth = findViewById(R.id.et_date_of_birth);
        et_age = findViewById(R.id.et_age);
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        patientFilledDataModel = new PatientFilledDataModel();

        stateNameHM = new HashMap<>();
        districtNameHM = new HashMap<>();
        blockNameHM = new HashMap<>();
        postOfficeNameHM = new HashMap<>();
        villageNameHM = new HashMap<>();
        casteNameHM = new HashMap<>();
        bloodNameHM = new HashMap<>();


        stateArrayList = new ArrayList<>();
        distrcitArrayList = new ArrayList<>();
        blockArrayList = new ArrayList<>();
        postOfficeArrayList = new ArrayList<>();
        villageArrayList = new ArrayList<>();
        casteArrayList = new ArrayList<>();
        bloodArrayList = new ArrayList<>();
    }

    @OnClick({R.id.iv_profile_image,
            R.id.et_date_of_birth})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_profile_image:
                setProfilePhotoClick();
                break;
            case R.id.et_date_of_birth:
                //setDateOfBirthClick();
                selectDate();
                break;
        }
    }

    private void selectDate() {
        datePickerDialog = new DatePickerDialog(SignUp.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mYear = i;
                mMonth = i1;
                mDay = i2;
                Calendar c = Calendar.getInstance();
                c.set(i, i1, i2);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                String dob = sdf1.format(c.getTime());
                String formattedDate = sdf.format(c.getTime());
                et_date_of_birth.setText(formattedDate);
                et_age.setText(getAge(dob));
            }

        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    private void setDateOfBirthClick() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @SuppressLint("NewApi")
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            String dt = day + "-" + month + "-" + year;
            Calendar c = Calendar.getInstance();
            c.set(year, month, day, 0, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            String dob = sdf1.format(c.getTime());
            String formattedDate = sdf.format(c.getTime());
            et_date_of_birth.setText(formattedDate);
            et_age.setText(getAge(dob));

            /*get age in month for dob*/
            // Using Calendar - calculating number of months between two dates
            Calendar birthDay = new GregorianCalendar(year, month, day);
            Calendar today = new GregorianCalendar();
            today.setTime(new Date());

            int yearsInBetween = today.get(Calendar.YEAR)
                    - birthDay.get(Calendar.YEAR);
            int monthsDiff = today.get(Calendar.MONTH)
                    - birthDay.get(Calendar.MONTH);
            long ageInMonths = yearsInBetween * 12 + monthsDiff;
            long age = yearsInBetween;

            Log.e("ageInMonths", "xnxkjnc: " + ageInMonths);
            Log.e("age", "nkjsans: " + age);
        }
    }

    public static int getAAge(String dobString) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    public static String getAge(String date) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = format.parse(date);
            dob.setTime(date1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        long d = dob.getTimeInMillis();
        int year = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        int month = 0, totalDaysDifference = 0;
        if (today.get(Calendar.MONTH) >= dob.get(Calendar.MONTH)) {
            month = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
        } else {
            month = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
            month = 12 + month;
            year--;
        }

        if (today.get(Calendar.DAY_OF_MONTH) >= dob.get(Calendar.DAY_OF_MONTH)) {
            totalDaysDifference = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);
        } else {
            totalDaysDifference = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);
            totalDaysDifference = 30 + totalDaysDifference;
            month--;
        }
        double age = (year * 12) + month;
        Integer ageInt = (int) age;

        age_in_month = ageInt.toString(); //for months return this.
        int calAge = (Integer.parseInt(age_in_month) / 12); //for years return this.
        return String.valueOf(calAge);
    }

    private void setProfilePhotoClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                iv_profile_image.setImageBitmap(photo);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getTextFromFields() {
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    name = et_name.getText().toString().trim();
                    date_of_birth = et_date_of_birth.getText().toString().trim();
                    aadhar_card = et_aadhar_card.getText().toString().trim();
                    contact_number = et_contact_number.getText().toString().trim();
                    address = et_address.getText().toString().trim();
                    emergency_contact = et_emergency_contact.getText().toString().trim();
                    pincode = et_pin_code.getText().toString().trim();
                    weight = et_weight.getText().toString().trim();
                    height = et_height.getText().toString().trim();
                    /*sign up model*/
                    signUpModel = new SignUpModel();
                    signUpModel.setFull_name(name);
                    signUpModel.setDob(date_of_birth);
                    signUpModel.setAadhar_no(aadhar_card);
                    signUpModel.setAddress(address);
                    signUpModel.setEmergency_contact_no(emergency_contact);
                    signUpModel.setContact_no(contact_number);
                    signUpModel.setGender(gender);
                    signUpModel.setWeight(weight);
                    signUpModel.setHeight(height);
                    signUpModel.setPin_code(et_pin_code.getText().toString().trim());
                    signUpModel.setState_id(String.valueOf(state_id));
                    signUpModel.setDistrict_id(String.valueOf(district_id));
                    signUpModel.setBlock_id(String.valueOf(block_id));
                    signUpModel.setPost_office_id(String.valueOf("0"));
                    signUpModel.setVillage_id(String.valueOf(village_id));
                    signUpModel.setAge_month((month));
                    signUpModel.setCaste_id(String.valueOf(caste_id));
                    signUpModel.setDisability(disability);
                    /*other option for patient*/
                    if (spn_block.getSelectedItem().toString().equalsIgnoreCase("Other")
                           // || spn_post_office.getSelectedItem().toString().equalsIgnoreCase("Other")
                            || spn_village.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                        coveredArea = "N";
                    }
                    signUpModel.setCovered_area(coveredArea);
                    signUpModel.setBlood_group_id(String.valueOf(blood_group_id));
                    signUpModel.setRole_id("8");
                    signUpModel.setCovered_area("Y");
                    signUpModel.setProfile_pic(encodedImage);
                    signUpModel.setMobile_token("1234");
                    /*here age condition for month*/
                    if (et_age.getText().toString().trim().equalsIgnoreCase("0")) {
                        if (age_in_month.length() == 1) {
                            age_in_month = "0.0" + age_in_month;
                        }
                        if (age_in_month.length() == 2) {
                            age_in_month = "0." + age_in_month;
                        }
                        signUpModel.setAge(age_in_month);
                    } else {
                        signUpModel.setAge(et_age.getText().toString().trim());
                    }

                    signUpModel.setProfile_type("0");
                    if (add.equals("addMember")) {
                        signUpModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
                   }
//                    if (Newadd.equals("New Patient")) {
//                        signUpModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
//                    }
                    else {
                        signUpModel.setUser_id("");
                    }
//                    if (commomProfile.equals("commomProfile")) {
////                        signUpModel.setProfile_patient_id(Integer.parseInt(profile_id));
////                        signUpModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
////                        sqliteHelper.saveSignupData(signUpModel, String.valueOf(signUpModel.getProfile_patient_id()));
////                        signUpModel=sqliteHelper.syncSignUpData();
//
//                    }


                    /*send data here*/
                    if (commomProfile.equals("commomProfile")) {
                        signUpModel.setProfile_patient_id((profile_id));
                        signUpModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
                        sqliteHelper.saveSignupData(signUpModel, String.valueOf(signUpModel.getProfile_patient_id()));

                        if (!isInternetOn()){
                            Toast.makeText(context, "Data Updated successfully locally", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else if (isInternetOn()){
                            signUpModel=sqliteHelper.syncSignUpData1(profile_id);
                            Gson gson = new Gson();
                            String data = gson.toJson(signUpModel);
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);
                            sendEditProfile(body,profile_id);
                        }

                    }
                    else {
                        Gson gson = new Gson();
                        String data = gson.toJson(signUpModel);
                        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(JSON, data);
                        //sendSignUpData(body);
                    }
                }
            }
        });


    }

    private void sendEditProfile(RequestBody body,String profile_id) {
        mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);

        APIClient.getClient().create(TELEMEDICINE_API.class).sendEditProfileData(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    mProgressDialog.dismiss();
                    Log.e("jdsbc", "hcdhbd " + jsonObject.toString());
                    String success = jsonObject.optString("success");
                    String user_id = jsonObject.optString("user_id");
                    String message = jsonObject.optString("message");
                    if (success.equals("1")) {
                       // sqliteHelper.updateFlag("profile_patients", 0, 1);
                        sqliteHelper.updateFlagInTable("profile_patients", "id",  Integer.parseInt((profile_id)), 1);
                        Toast.makeText(SignUp.this, "Edited Successfully" , Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(SignUp.this, "Something Went Wrong " , Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

//    private void sendSignUpData(final RequestBody signUpModel) {
//        //mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
//
//        TELEMEDICINE_API api_service = APIClient.getClient().create(TELEMEDICINE_API.class);
//        if (signUpModel != null && api_service != null) {
//            Call<JsonObject> server_response = api_service.sendSignupData(signUpModel);
//            try {
//                if (server_response != null) {
//                    server_response.enqueue(new Callback<JsonObject>() {
//                        @Override
//                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                            if (response.isSuccessful()) {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(response.body().toString());
//                                    mProgressDialog.dismiss();
//                                    Log.e("kjnncnnjn", "onResponse: " + jsonObject.toString());
//                                    String success = jsonObject.getString("success");
//                                    if (success.equals("1")) {
//
//                                        String user_id = jsonObject.getString("user_id");
//                                        if (add.equals("")) {
////                                            String otp = jsonObject.getString("otp");
//                                            Intent intent = new Intent(context, HomeActivity.class);
//                                            intent.putExtra("user_id", user_id);
////                                            intent.putExtra("otp", otp);
//                                            intent.putExtra("mobile", et_contact_number.getText().toString());
//                                            intent.putExtra("mobile_for_show", et_contact_number.getText().toString());
//                                            startActivity(intent);
//                                            finish();
//                                        }  else {
//                                            mProgressDialog.dismiss();
//                                            showAlertDialogForAddProfile();
//                                        }
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonObject> call, Throwable t) {
//                            mProgressDialog.dismiss();
//                        }
//
//                    });
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//            }
//        }
//    }

    private void showAlertDialogForAddProfile() {
        add_profile_alert = new android.app.Dialog(this);

        add_profile_alert.setContentView(R.layout.submit_appointment_from_patient_dialog);
        add_profile_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = add_profile_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        TextView tv_appointment_added = (TextView) add_profile_alert.findViewById(R.id.tv_appointment_added);
        TextView tv_appointment_msg = (TextView) add_profile_alert.findViewById(R.id.tv_appointment_msg);
        Button btn_ok = (Button) add_profile_alert.findViewById(R.id.btn_ok);

        /*set dynamically changed text*/
        tv_appointment_added.setText("Profile Added");

        tv_appointment_msg.setText("Your profile added successfully.");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_profile_alert.dismiss();
                if (Newadd.equals("")){
                    Intent intent = new Intent(context, BookinlistActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(context, BookingActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        add_profile_alert.show();
        add_profile_alert.setCanceledOnTouchOutside(false);
    }

    private boolean checkValidation() {
        if (et_name.getText().toString().trim().length() == 0) {
            flagEditfield = et_name;
            msg = "Please enter name!";
            flagEditfield.setError(msg);
            et_name.requestFocus();
            return false;
        }
        if (!et_name.getText().toString().trim().matches("[a-zA-Z ]+")) {
            flagEditfield = et_name;
            msg = "The name can only consist of alphabets!";
            flagEditfield.setError(msg);
            et_name.requestFocus();
            return false;
        }
        if (rg_gender.getVisibility() == View.VISIBLE) {
            int selected_id = rg_gender.getCheckedRadioButtonId();
            if (selected_id <= 0) {
                Toast.makeText(context, "Please select gender!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (rg_age.getVisibility() == View.VISIBLE) {
            int selectedId = rg_age.getCheckedRadioButtonId();
            if (selectedId <= 0) {
                Toast.makeText(context, "Please choose option 'age or date of birth'.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (rb_age.isChecked() && et_age.getText().toString().trim().length() == 0) {
            flagEditfield = et_age;
            msg = "Please enter age!";
            flagEditfield.setError(msg);
            et_age.requestFocus();
            return false;
        }
        if (rb_dob.isChecked() && et_date_of_birth.getText().toString().trim().length() == 0) {
            flagEditfield = et_date_of_birth;
            msg = "Please select date of birth!";
            flagEditfield.setError(msg);
            et_date_of_birth.requestFocus();
            return false;
        }
        if (et_contact_number.getText().toString().trim().length() == 0) {
            flagEditfield = et_contact_number;
            msg = "Please enter contact number!";
            flagEditfield.setError(msg);
            et_contact_number.requestFocus();
            return false;
        }
        if (et_contact_number.getText().toString().trim().length() < 10) {
            flagEditfield = et_contact_number;
            msg = "Please enter 10 digits contact number!";
            flagEditfield.setError(msg);
            et_contact_number.requestFocus();
            return false;
        }
        if (et_pin_code.getText().toString().trim().length() == 0) {
            flagEditfield = et_pin_code;
            msg = "Please enter pin code!";
            flagEditfield.setError(msg);
            et_pin_code.requestFocus();
            return false;
        }
        if (spn_state.getSelectedItem().toString().trim().equalsIgnoreCase("Select State")) {
            TextView errorText = (TextView) spn_state.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select state!");
            Toast.makeText(context, "Please select state!", Toast.LENGTH_LONG).show();
            errorText.requestFocus();
            return false;
        }
        if (spn_district.getSelectedItem().toString().trim().equalsIgnoreCase("Select District")) {
            TextView errorText = (TextView) spn_district.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select district!");
            Toast.makeText(context, "Please select district!", Toast.LENGTH_LONG).show();
            errorText.requestFocus();
            return false;
        }
        if (spn_block.getSelectedItem().toString().trim().equalsIgnoreCase("Select Block")) {
            TextView errorText = (TextView) spn_block.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select block!");
            Toast.makeText(context, "Please select block!", Toast.LENGTH_LONG).show();
            errorText.requestFocus();
            return false;
        }
//        if (spn_post_office.getSelectedItem().toString().trim().equalsIgnoreCase("Select Post Office")) {
//            TextView errorText = (TextView) spn_post_office.getSelectedView();
//            errorText.setError("");
//            errorText.setTextColor(Color.RED);//just to highlight that this is an error
//            errorText.setText("Please select post office!");
//            Toast.makeText(context, "Please select post office!", Toast.LENGTH_LONG).show();
//            errorText.requestFocus();
//            return false;
//        }
        if (spn_village.getSelectedItem().toString().trim().equalsIgnoreCase("Select Village")) {
            TextView errorText = (TextView) spn_village.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please select village!");
            Toast.makeText(context, "Please select village!", Toast.LENGTH_LONG).show();
            errorText.requestFocus();
            return false;
        }
        if (et_address.getText().toString().trim().length() == 0) {
            flagEditfield = et_address;
            msg = "Please enter address!";
            flagEditfield.setError(msg);
            et_address.requestFocus();
            return false;
        }
        return true;
    }

    public void getMasterTables(final Activity context) {
        final SqliteHelper sqliteHelper = new SqliteHelper(context);
        sqliteHelper.openDataBase();
       // mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);

        for (int j = 0; j < masterTables.length; j++) {
            DataDownloadInput dataDownloadInput = new DataDownloadInput();
            dataDownloadInput.setTable_name(masterTables[j]);
            String date = "";
            if (masterTables[j].equals("symptom")) {
                date = sqliteHelper.getUpdatedOn(masterTables[j]);
            } else if (masterTables[j].equals("disease")) {
                date = sqliteHelper.getUpdatedOn(masterTables[j]);

            } else if (masterTables[j].equals("medicine_list")) {
                date = sqliteHelper.getUpdatedOn(masterTables[j]);

            } else if (masterTables[j].equals("test")) {
                date = sqliteHelper.getUpdatedOn(masterTables[j]);

            } else if (masterTables[j].equals("sub_tests")) {
                date = sqliteHelper.getUpdatedOn(masterTables[j]);

            } else {
                date = sqliteHelper.getUpdatedDate(masterTables[j]);
            }

            dataDownloadInput.setUpdated_at(date);
            Gson mGson = new Gson();
            String data = mGson.toJson(dataDownloadInput);
            Log.e("Data", data);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, data);
            final TELEMEDICINE_API apiService = APIClient.getClient().create(TELEMEDICINE_API.class);
            Call<JsonObject> call = apiService.getMasterTables(body);
            final int finalJ = j;
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        //JsonArray data = response.body();
                        JsonObject singledataP = response.body();
                        Log.e("bb", "bbb " + singledataP.toString());
                        //   sqliteHelper.dropTable(masterTables[finalJ]);
                        //String tableData=singledata.getString("tableData");
                        JsonArray data = singledataP.getAsJsonArray("tableData");
                        Log.e("cc", "ccc " + data.toString());

                        for (int i = 0; i < data.size(); i++) {
                            JSONObject singledata = new JSONObject(data.get(i).toString());
                            // JSONObject singledata = data.getJSONObject(i);
                            //singledata.getString("id");
                            Iterator keys = singledata.keys();
                            ContentValues contentValues = new ContentValues();
                            while (keys.hasNext()) {
                                String currentDynamicKey = (String) keys.next();
                                contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                            }

                            sqliteHelper.saveMasterTable(contentValues, masterTables[finalJ]);

                        }
                       // mProgressDialog.dismiss();
                    } catch (Exception s) {
                        s.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
               //     Log.d("", "");
                //    mProgressDialog.dismiss();
                }
            });

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(SignUp.this, CommonProfile.class);
            intent.putExtra("commomProfile", "commomProfile");
            intent.putExtra("profile_patient_id", profile_id);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (commomProfile.equalsIgnoreCase("commomProfile")) {
            Intent intent = new Intent(context, CommonProfile.class);
            intent.putExtra("commomProfile", "commomProfile");
            intent.putExtra("profile_patient_id", profile_id);
            startActivity(intent);
            finish();
        }else {

            Intent intent = new Intent(context, BookinlistActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private boolean isInternetOn() {
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

}
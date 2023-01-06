package com.indev.jubicare_assistants;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.projection.MediaProjectionManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.hbisoft.hbrecorder.HBRecorder;
import com.hbisoft.hbrecorder.HBRecorderListener;
import com.indev.jubicare_assistants.adapter.PatientListAdapter;
import com.indev.jubicare_assistants.model.PharmacyPatientModel;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.service.Config;
import com.indev.jubicare_assistants.service.NotificationUtils;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;
import com.indev.jubicare_assistants.videocallingapp.BaseActivity;
import com.indev.jubicare_assistants.videocallingapp.CallScreenActivity;
import com.indev.jubicare_assistants.videocallingapp.SinchService;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushTokenRegistrationCallback;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.UserController;
import com.sinch.android.rtc.UserRegistrationCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginAcivity extends BaseActivity implements HBRecorderListener, SinchService.StartFailedListener, PushTokenRegistrationCallback, UserRegistrationCallback {
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.tv_sign_up)
    TextView tv_signup;
    @BindView(R.id.tv_forgot_password)
    TextView tv_forgot_password;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.cb_showPassword)
    CheckBox cb_showPassword;
    PharmacyPatientModel pharmacyPatientModel = new PharmacyPatientModel();
    SqliteHelper sqliteHelper;
    android.app.Dialog appointment_alert;

    public static RelativeLayout rl_technology_partner;
    /*normal widgets*/

    //private ProgressDialog mProgressDialog;
    private Context context = this;
    private SharedPrefHelper sharedPrefHelper;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = LoginAcivity.class.getSimpleName();
    /*--for validation--*/
    private EditText flagEditfield;
    private String msg = "";
    boolean mPushTokenIsRegistered;

    private String mUserId;
    private long mSigningSequence = 1;
    private String sinchId="";
    private String jubicareAssistanceUserName="";//this the user which is used to connect call.
    private String jubicareAssistanceMobileToken="";//this the token which is used to connect call.
    private String doctorUserName="";//this is the doctor user name which is register to make call.
    private String doctorPassword="";//this is the doctor password.
    private String ivr_calling_masking_id="";//this is the masking id.
    private String masking_id="";
    private String patient_name="";
    private String uid="";
    private String timeStamp="";
    private String startTime="";
    private String time_duration="";
    private String call_status="";

    //recorder
    private static final int SCREEN_RECORD_REQUEST_CODE = 777;
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = PERMISSION_REQ_ID_RECORD_AUDIO + 1;
    private boolean hasPermissions = false;
    //Declare HBRecorder
    private HBRecorder hbRecorder;
    boolean wasHDSelected = false;
    boolean edit = false;
    boolean isAudioEnabled = true;
    private ProgressDialog mProgressDialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (sharedPrefHelper.getString("assistance_application_login_by_doctor", "").equals("assistance_application_login_by_doctor")) {
            Intent callScreen = new Intent(Intent.ACTION_MAIN);
            callScreen.setClassName("com.indev.telemedicine", "com.indev.telemedicine.acitivities.doctor.DoctorHome");
            callScreen.putExtra("ivr_call_masking_id", masking_id);
            callScreen.putExtra("patient_name", patient_name);
            callScreen.putExtra("uid", uid);
            callScreen.putExtra("end_time", timeStamp);
            callScreen.putExtra("start_time", startTime);
            callScreen.putExtra("time_duration", time_duration);
            callScreen.putExtra("call_status", call_status);
            startActivity(callScreen);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login_acivity);

        ButterKnife.bind(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        sqliteHelper=new SqliteHelper(this);
        //  btn_login=findViewById(R.id.btn_login);
        getSupportActionBar().hide();
        displayPassword();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //  txtMessage.setText(message);
                }
            }
        };
        displayFirebaseRegId();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    callLoginApi();
                }
//                Intent intent = new Intent(LoginAcivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginAcivity.this, ForgetPassword.class);
                startActivity(intent);
                finish();
            }
        });

        hbRecorder = new HBRecorder(this, this);
        LinearLayout ll_main_layout=findViewById(R.id.ll_main_layout);
        Button btnStartVideoCall=findViewById(R.id.btnStartVideoCall);
        Button btnBack=findViewById(R.id.btnBack);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sinchId = bundle.getString("sinchId", "");
            jubicareAssistanceUserName = bundle.getString("user_name", "");
            jubicareAssistanceMobileToken = bundle.getString("mobile_token", "");
            doctorUserName = bundle.getString("doctor_user_name", "");
            doctorPassword = bundle.getString("doctor_password", "");
            ivr_calling_masking_id = bundle.getString("masking_id", "");
            if (!sinchId.equals("")){
                ll_main_layout.setVisibility(View.GONE);
                btnStartVideoCall.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.VISIBLE);
            }
        }
        if (!sinchId.equals("")) {
            ProgressDialog mProgressDialog = ProgressDialog.show(context, "", "Please wait while start calling", true);
            LoginModel mLoginModel = new LoginModel();

            String username=doctorUserName;
            mLoginModel.setPassword(doctorPassword);
            mLoginModel.setUser_name(doctorUserName);

            Gson mGson = new Gson();
            String data = mGson.toJson(mLoginModel);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, data);
            APIClient.getClient().create(TELEMEDICINE_API.class).newLogin(body).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        mProgressDialog.dismiss();
                        Log.e("ncxxnx", "bfhdhf " + jsonObject.toString());
                        if (jsonObject.optString("success").equalsIgnoreCase("1")) {
                            sharedPrefHelper.setString("assistance_application_login_by_doctor", "assistance_application_login_by_doctor");
                            String user_id = jsonObject.optString("user_id");
                            String success = jsonObject.optString("success");
                            String role_id = jsonObject.optString("role_id");
                            String full_name = jsonObject.optString("full_name");
                            String profile_pic = jsonObject.optString("profile_pic");
                            String mobile_token = jsonObject.optString("mobile_token");
                            sharedPrefHelper.setString("user_id", user_id);
                            sharedPrefHelper.setString("role_id", role_id);
                            sharedPrefHelper.setString("full_name", full_name);
                            sharedPrefHelper.setString("profile_pic", profile_pic);
                            sharedPrefHelper.setString("userName", username);
                            sharedPrefHelper.setString("mobile_token", mobile_token);

                            if (success.equalsIgnoreCase("1") && role_id.equalsIgnoreCase("4")) {
                                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                                if (!sharedPrefHelper.getString("sinch_user","").equals("")) {
                                    if (getSinchServiceInterface() != null) {
                                        UserController uc = Sinch.getUserControllerBuilder()
                                                .context(getApplicationContext())
                                                .applicationKey(SinchService.APP_KEY)
                                                .userId(sharedPrefHelper.getString("sinch_user", ""))
                                                .environmentHost(SinchService.ENVIRONMENT)
                                                .build();
                                        uc.unregisterPushToken();

                                        getSinchServiceInterface().stopClient();
                                    }
                                }

                                String sinchId=full_name+"~"+mobile_token;
                                getSinchServiceInterface().setUsername(sinchId);
                                if ((getSinchServiceInterface() != null && !getSinchServiceInterface().isStarted())) {
                                    getSinchServiceInterface().startClient();
                                }
                                mUserId = sinchId;
                                sharedPrefHelper.setString("sinch_user",mUserId);
                                UserController uc = Sinch.getUserControllerBuilder()
                                        .context(getApplicationContext())
                                        .applicationKey(SinchService.APP_KEY)
                                        .userId(mUserId)
                                        .environmentHost(SinchService.ENVIRONMENT)
                                        .build();
                                uc.registerUser(LoginAcivity.this,LoginAcivity.this);
                                //Toast.makeText(context, "User register successfully on sinch", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            mProgressDialog.dismiss();
                            //Toast.makeText(context, "Unable to register user for calling.", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    mProgressDialog.dismiss();
                }
            });
        }

        //on click
        btnStartVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSinchServiceInterface().setUsername(sharedPrefHelper.getString("userName",""));
                getSinchServiceInterface().startClient();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //first check if permissions was granted
                    if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO) && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE)) {
                        hasPermissions = true;
                    }
                    if (hasPermissions) {
                        //check if recording is in progress
                        //and stop it if it is
                        startRecordingScreen();
                    }
                } else {
                    showLongToast("This library requires API 21>");
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go-back on doctor home
                Intent callScreen = new Intent(Intent.ACTION_MAIN);
                callScreen.setClassName("com.indev.telemedicine", "com.indev.telemedicine.acitivities.doctor.DoctorHome");
                callScreen.putExtra("ivr_call_masking_id", masking_id);
                callScreen.putExtra("patient_name", patient_name);
                callScreen.putExtra("uid", uid);
                callScreen.putExtra("end_time", timeStamp);
                callScreen.putExtra("start_time", startTime);
                callScreen.putExtra("time_duration", time_duration);
                callScreen.putExtra("call_status", call_status);
                startActivity(callScreen);
                finish();
            }
        });
    }

    /**/
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginAcivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                sharedPrefHelper.setString("Token", mToken);
                Log.e("Token", mToken);
            }
        });
        String regId = pref.getString("regId", null);
        //Log.e(TAG, "Firebase reg id: " + regId);
        if (!TextUtils.isEmpty(regId)) {
            //txtRegId.setText("Firebase Reg Id: " + regId);
        } else {
            //txtRegId.setText("Firebase Reg Id is not received yet!");
        }
    }

    private void displayPassword() {
        cb_showPassword.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // show password
                et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // hide password
                et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    private void callLoginApi() {
        String username = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        //Snackbar.make(view, "Authenticating online" + "!!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        if (checkInternetConnection(context) == false) {
            new AlertDialog.Builder(context)
                    .setTitle("Alert !")
                    .setMessage("Network Error, check your network connection.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else {
            mProgressDialog = ProgressDialog.show(context, "Login", "Please Wait...", true);
            LoginModel mLoginModel = new LoginModel();
            mLoginModel.setPassword(password);
            mLoginModel.setUser_name(username);
            mLoginModel.setFirebase_token(sharedPrefHelper.getString("Token", ""));
            Gson mGson = new Gson();
            String data = mGson.toJson(mLoginModel);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, data);
            APIClient.getClient().create(TELEMEDICINE_API.class).login(body).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                       // mProgressDialog.dismiss();
                        if (jsonObject.optString("success").equalsIgnoreCase("1")) {
                            /**
                             * Drop tables here before download data in local database
                             **/
                            sqliteHelper.dropTable("patient_appointments");
                            sqliteHelper.dropTable("profile_patients");
                            sqliteHelper.dropTable("appointment_medicine_prescribed");

                            sharedPrefHelper.setString("is_login", "1");
                            String user_id = jsonObject.optString("user_id");
                            String message = jsonObject.optString("message");
                            String success = jsonObject.optString("success");
                            String role_id = jsonObject.optString("role_id");
                            String full_name = jsonObject.optString("full_name");
                            String state_id = jsonObject.optString("state_id");
                            String district_id = jsonObject.optString("district_id");
                            String block_id = jsonObject.optString("block_id");
                            String profile_pic = jsonObject.optString("profile_pic");
                            String mobile_token = jsonObject.optString("mobile_token");
                            sharedPrefHelper.setString("state_id", state_id);
                            sharedPrefHelper.setString("district_id", district_id);
                            sharedPrefHelper.setString("block_id", block_id);
                            sharedPrefHelper.setString("user_id", user_id);
                            sharedPrefHelper.setString("role_id", role_id);
                            sharedPrefHelper.setString("full_name", full_name);
                            sharedPrefHelper.setString("profile_pic", profile_pic);
                            sharedPrefHelper.setString("userName", username);
                            sharedPrefHelper.setString("mobile_token", mobile_token);
                            download_patientlist("profile_patients");
                            download_Commonfile("patient_appointments");
                            //download_doctor_list("doctor_list");
                            download_doctoravil("doctor_available");

                            if (success.equalsIgnoreCase("1")) {
                                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                                if (!sharedPrefHelper.getString("sinch_user","").equals("")){
                                    if (getSinchServiceInterface() != null) {
                                        UserController uc = Sinch.getUserControllerBuilder()
                                                .context(getApplicationContext())
                                                .applicationKey(SinchService.APP_KEY)
                                                .userId(sharedPrefHelper.getString("sinch_user", ""))
                                                .environmentHost(SinchService.ENVIRONMENT)
                                                .build();
                                        uc.unregisterPushToken();
                                        getSinchServiceInterface().stopClient();
                                    }
                                }
                                String sinchId=username+"~"+mobile_token;
                                getSinchServiceInterface().setUsername(sinchId);
                                getSinchServiceInterface().startClient();
                                mUserId = sinchId;
                                sharedPrefHelper.setString("sinch_user",mUserId);
                                sharedPrefHelper.setString("sinch_user_call",mUserId);
                                UserController uc = Sinch.getUserControllerBuilder()
                                        .context(getApplicationContext())
                                        .applicationKey(SinchService.APP_KEY)
                                        .userId(mUserId)
                                        .environmentHost(SinchService.ENVIRONMENT)
                                        .build();
                                uc.registerUser(LoginAcivity.this,LoginAcivity.this);
                                //Toast.makeText(context, "User register successfully on sinch", Toast.LENGTH_LONG).show();
                                mProgressDialog.dismiss();
                                Intent intent = new Intent(LoginAcivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }


                        }
                        else if (jsonObject.optString("success").equalsIgnoreCase("2")){
                            String user_idF = jsonObject.optString("user_id");
                            sharedPrefHelper.setString("user_idF", user_idF);

                            showAlertAppointmentDeleted();

                            mProgressDialog.dismiss();
                           // Toast.makeText(context, "Invalid Credential", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context,"Please enter correct username and password",Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    public static boolean checkInternetConnection(Context context) {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void download_patientlist(String table_name) {
        //ProgressDialog mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
        pharmacyPatientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
        pharmacyPatientModel.setRole_id(sharedPrefHelper.getString("role_id", ""));
        pharmacyPatientModel.setTable_name(table_name);
        pharmacyPatientModel.setAppointment_ID("0");

        Gson mGson = new Gson();
        String data = mGson.toJson(pharmacyPatientModel);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        APIClient.getClient().create(TELEMEDICINE_API.class).download_Data(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        //mProgressDialog.dismiss();
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            sqliteHelper.dropTable(table_name);
                            JsonObject singledataP = response.body();
                            JsonArray data = singledataP.getAsJsonArray("tableData");
                            if (singledataP.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    JSONObject singledata = new JSONObject(data.get(i).toString());
                                    Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());

                                    Iterator keys = singledata.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                        contentValues.put("flag", "1");
                                    }
                                    sqliteHelper.saveMasterTable(contentValues, table_name);



                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressDialog.dismiss();
            }
        });

    }

    public void download_Commonfile(String table_name) {
        //ProgressDialog mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
        pharmacyPatientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
        pharmacyPatientModel.setRole_id(sharedPrefHelper.getString("role_id", ""));
        pharmacyPatientModel.setTable_name(table_name);
        pharmacyPatientModel.setAppointment_ID("0");

        Gson mGson = new Gson();
        String data = mGson.toJson(pharmacyPatientModel);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        APIClient.getClient().create(TELEMEDICINE_API.class).download_Data(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        //mProgressDialog.dismiss();
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            sqliteHelper.dropTable(table_name);
                            sqliteHelper.dropTable("appointment_medicine_prescribed");
                            JsonObject singledataP = response.body();
                            JsonArray data = singledataP.getAsJsonArray("tableData");
                            if (data.size() > 0)  {
                                for (int i = 0; i < data.size(); i++) {
                                    JSONObject singledata = new JSONObject(data.get(i).toString());

                                    Iterator keys = singledata.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        if (currentDynamicKey.equals("appointment_medicine_prescribed")) {
                                            try {
                                                JSONArray medicinePrescribed = null;
                                                medicinePrescribed = singledata.getJSONArray("appointment_medicine_prescribed");
                                                if (medicinePrescribed != null) {
                                                    for (int j = 0; j < medicinePrescribed.length(); j++) {
                                                        try {
                                                            JSONObject singleDataMedicalPrescribed = new JSONObject(medicinePrescribed.get(j).toString());
                                                            Iterator keys2 = singleDataMedicalPrescribed.keys();
                                                            ContentValues contentValues2 = new ContentValues();
                                                            while (keys2.hasNext()) {
                                                                String currentDynamicKey2 = (String) keys2.next();

                                                                contentValues2.put(currentDynamicKey2, singleDataMedicalPrescribed.get(currentDynamicKey2).toString());
                                                            }
                                                            contentValues2.put("flag", "1");
                                                            sqliteHelper.saveMasterTable(contentValues2, "appointment_medicine_prescribed");
                                                        }catch(JSONException ex){
                                                            Log.d("JsonException",ex.getMessage());
                                                        }
                                                        catch(Exception ex){
                                                            Log.d("Exception",ex.getMessage());
                                                        }
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
                                    contentValues.put("flag", "1");
                                    sqliteHelper.saveMasterTable(contentValues, table_name);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressDialog.dismiss();
            }
        });

    }

    public void download_doctoravil(String table_name) {
        //ProgressDialog mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
        pharmacyPatientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
        pharmacyPatientModel.setRole_id(sharedPrefHelper.getString("role_id", ""));
        pharmacyPatientModel.setDoctor_id("0");
        pharmacyPatientModel.setTable_name(table_name);

        Gson mGson = new Gson();
        String data = mGson.toJson(pharmacyPatientModel);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        APIClient.getClient().create(TELEMEDICINE_API.class).download_Data(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        //mProgressDialog.dismiss();
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            sqliteHelper.dropTable(table_name);
                            JsonObject singledataP = response.body();
                            JsonArray data = singledataP.getAsJsonArray("tableData");
                            if (data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    JSONObject singledata = new JSONObject(data.get(i).toString());
                                    Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());

                                    Iterator keys = singledata.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                        contentValues.put("flag", "1");
                                    }
                                    sqliteHelper.saveMasterTable(contentValues, table_name);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressDialog.dismiss();
            }
        });

    }

    public void download_doctor_list(String table_name) {
        //ProgressDialog mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
//        pharmacyPatientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
//        pharmacyPatientModel.setRole_id(sharedPrefHelper.getString("role_id", ""));
//        //pharmacyPatientModel.setTable_name(table_name);

//        //pharmacyPatientModel.setMobile(searchInput);
        Gson mGson = new Gson();
        String data = mGson.toJson(pharmacyPatientModel);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        APIClient.getClient().create(TELEMEDICINE_API.class).doctor_Data(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        //mProgressDialog.dismiss();
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            sqliteHelper.dropTable(table_name);
                            JsonObject singledataP = response.body();
                            JsonArray data = singledataP.getAsJsonArray("tableData");
                            if (data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    JSONObject singledata = new JSONObject(data.get(i).toString());
                                    Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());

                                    Iterator keys = singledata.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                        contentValues.put("flag", "1");
                                    }
                                    sqliteHelper.saveMasterTable(contentValues, table_name);

                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressDialog.dismiss();
            }
        });

    }

    private void showAlertAppointmentDeleted() {
        appointment_alert = new android.app.Dialog(this);
        appointment_alert.setContentView(R.layout.submit_appointment_from_logout_dialog);
        appointment_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = appointment_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        TextView tv_appointment_added = (TextView) appointment_alert.findViewById(R.id.tv_appointment_added);
        TextView tv_appointment_msg = (TextView) appointment_alert.findViewById(R.id.tv_appointment_msg);
        Button btn_ok = (Button) appointment_alert.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointment_alert.dismiss();
                callLogoutApi();
            }
        });
        appointment_alert.show();
        appointment_alert.setCanceledOnTouchOutside(false);
    }

    public void callLogoutApi() {
        CountInput countInput = new CountInput();
        countInput.setUser_id(sharedPrefHelper.getString("user_idF", ""));
        Gson mGson = new Gson();
        String data = mGson.toJson(countInput);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        //ProgressDialog finalProgressDialog = mProgressDialog;
        APIClient.getClient().

                create(TELEMEDICINE_API.class).callLogoutApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                  //  finalProgressDialog.dismiss();
                    Log.e("jbfd", "nsh " + jsonObject.toString());
                    if (jsonObject.optString("success").equalsIgnoreCase("1")) {
//                        if (checkValidation()) {
                            callLoginApi();
//                        }

                       //sharedPrefHelper.setString("is_login", "");
//                        Intent i = new Intent(Lo.this, LoginAcivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
                    } else {
                        Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                  //  finalProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
              //  finalProgressDialog.dismiss();
            }
        });


    }


    private boolean checkValidation() {
        if (et_email.getText().toString().trim().length() == 0) {
            flagEditfield = et_email;
            msg = "Please enter username";
            flagEditfield.setError(msg);
            et_email.requestFocus();
            return false;
        } else if (et_password.getText().toString().trim().length() == 0) {
            flagEditfield = et_password;
            msg = "Please enter password";
            flagEditfield.setError(msg);
            et_password.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

        if (sharedPrefHelper.getString("assistance_application_login_by_doctor", "").equals("assistance_application_login_by_doctor")) {
            masking_id=sharedPrefHelper.getString("ivr_call_masking_id", "");
            patient_name=sharedPrefHelper.getString("patient_name", "");
            uid=sharedPrefHelper.getString("uid", "");
            timeStamp=sharedPrefHelper.getString("end_time", "");
            startTime=sharedPrefHelper.getString("start_time", "");
            time_duration=sharedPrefHelper.getString("time_duration", "");
            call_status=sharedPrefHelper.getString("call_status", "");
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    //Handle permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_ID_RECORD_AUDIO:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
                } else {
                    hasPermissions = false;
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                }
                break;
            case PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermissions = true;
                    //Permissions was provided
                    //Start screen recording
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startRecordingScreen();
                    }
                } else {
                    hasPermissions = false;
                    showLongToast("No permission for " + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            default:
                break;
        }
    }

    private void showLongToast(final String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void startRecordingScreen() {
        quickSettings();
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = mediaProjectionManager != null ? mediaProjectionManager.createScreenCaptureIntent() : null;
        startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    //Set file path or Uri depending on SDK version
                    setOutputPath();
                    //Start screen recording
                    hbRecorder.startScreenRecording(data, resultCode, this);
                    com.sinch.android.rtc.calling.Call calls = getSinchServiceInterface().callUserVideo(jubicareAssistanceUserName  + "~" + jubicareAssistanceMobileToken);
                    Log.e("ids", jubicareAssistanceUserName + "~" + jubicareAssistanceMobileToken);
                    String callId = calls.getCallId();
                    Intent callScreen = new Intent(LoginAcivity.this, CallScreenActivity.class);
                    callScreen.putExtra(SinchService.CALL_ID, callId);
                    callScreen.putExtra("patient_name", jubicareAssistanceUserName);
                    callScreen.putExtra("masking_id", ivr_calling_masking_id);
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                    callScreen.putExtra("startTime", timeStamp);
                    startActivity(callScreen);
                }
            }
        }
    }

    ContentResolver resolver;
    ContentValues contentValues;
    Uri mUri;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setOutputPath() {
        String filename = generateFileName();
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            resolver = getContentResolver();
            contentValues = new ContentValues();
            contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/" + "HBRecorder");
            contentValues.put(MediaStore.Video.Media.TITLE, filename);
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
            mUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
            //FILE NAME SHOULD BE THE SAME
            hbRecorder.setFileName(filename);
            hbRecorder.setOutputUri(mUri);
        }else{*/
        createFolder();
        hbRecorder.setOutputPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/HBRecorder");

        // }
    }

    private void createFolder() {
        File f1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "HBRecorder");
        if (!f1.exists()) {
            if (f1.mkdirs()) {
                sharedPrefHelper.setString("FileName", f1.getPath());
                Log.i("Folder ", "created");
            }
        }
    }

    //Generate a timestamp to be used as a file name
    private String generateFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        java.sql.Date curDate = new java.sql.Date(System.currentTimeMillis());
        return formatter.format(curDate).replace(" ", "");
    }


    //drawable to byte[]
    private byte[] drawable2ByteArray(@DrawableRes int drawableId) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), drawableId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void quickSettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String audio_source = prefs.getString("key_audio_source", null);

        hbRecorder.setAudioBitrate(128000);
        hbRecorder.setAudioSamplingRate(44100);
        hbRecorder.recordHDVideo(wasHDSelected);
        hbRecorder.enableCustomSettings();
        hbRecorder.isAudioEnabled(isAudioEnabled);
        hbRecorder.setVideoEncoder("DEFAULT");
        hbRecorder.setAudioSource("DEFAULT");
        hbRecorder.setVideoBitrate(450000);
        hbRecorder.setOutputFormat("DEFAULT");
        hbRecorder.setVideoFrameRate(24);
        hbRecorder.setScreenDimensions(426, 240);
        //Customise Notification
        hbRecorder.setNotificationSmallIcon((drawable2ByteArray(R.drawable.icon)));
        hbRecorder.setNotificationTitle("Recording your screen");
        hbRecorder.setNotificationDescription("Drag down to stop the recording");
    }

    @Override
    public void HBRecorderOnStart() {

    }

    @Override
    public void HBRecorderOnComplete() {

    }

    @Override
    public void HBRecorderOnError(int errorCode, String reason) {

    }

    @Override
    public void onBindingDied(ComponentName name) {

    }

    @Override
    public void onNullBinding(ComponentName name) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {

    }


    @Override
    public void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    public void tokenRegistered() {
        mPushTokenIsRegistered = true;
    }

    @Override
    public void tokenRegistrationFailed(SinchError sinchError) {
        mPushTokenIsRegistered = false;
    }

    @Override
    public void onCredentialsRequired(ClientRegistration clientRegistration) {
        String toSign = mUserId + SinchService.APP_KEY + mSigningSequence + SinchService.APP_SECRET;
        String signature;
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hash = messageDigest.digest(toSign.getBytes("UTF-8"));
            signature = Base64.encodeToString(hash, Base64.DEFAULT).trim();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }

        clientRegistration.register(signature, mSigningSequence++);
    }

    @Override
    public void onUserRegistered() {

    }

    @Override
    public void onUserRegistrationFailed(SinchError sinchError) {

    }
}
package com.indev.jubicare_assistants;

import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import com.indev.jubicare_assistants.utils.CommonClass;

import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppDrwer {
    LinearLayout book, Oldpatient;
    CardView avilabe,unAvilabe;
    TextView count,tvDoctorNotAvailable;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    DoctorAvailableDatePojo availableDatePojo;

    private String[] masterTables = {"state", "district", "block", "village", "post_office", "symptom", "disease", "medicine_list", "test", "sub_tests","prescription_eating_schedule","prescription_days","prescription_interval","blood_group","caste","profile_pharmacists","profile_doctors"};
    /**
     * in app update if new version of app updated on play-store*
     **/
    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /**
         * update app code 
         **/
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        checkUpdate();

        setTitle("Home");
        sqliteHelper=new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
        book = findViewById(R.id.book);
        Oldpatient = findViewById(R.id.Oldpatient);
        avilabe = findViewById(R.id.avilabe);
        unAvilabe = findViewById(R.id.unAvilabe);
        count = findViewById(R.id.count);
        tvDoctorNotAvailable=findViewById(R.id.tvDoctorNotAvailable);
        availableDatePojo=new DoctorAvailableDatePojo();

        //here the code for doctor status
        availableDatePojo=sqliteHelper.getDoctorNotAvailable(CommonClass.getCurrentDate());
        tvDoctorNotAvailable.setText(availableDatePojo.getDoctorNotAvailable());
        String doctorNotAvailable=availableDatePojo.getDoctor_id();
        sharedPrefHelper.setString("doctorNotAvailable", doctorNotAvailable);
        int doctorAvailable=0;
        if (doctorNotAvailable==null) {
            doctorAvailable = sqliteHelper.getDoctorAvailable("");
        }else {
            doctorAvailable = sqliteHelper.getDoctorAvailable(doctorNotAvailable);
        }
        count.setText(doctorAvailable + "");

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        avilabe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, DoctorAvilableList.class);
                startActivity(intent);
                finish();
                //Toast.makeText(HomeActivity.this, "Work in progress", Toast.LENGTH_SHORT).show();
            }
        });  unAvilabe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, DoctorUnableList.class);
                startActivity(intent);
                finish();
                //Toast.makeText(HomeActivity.this, "Work in progress", Toast.LENGTH_SHORT).show();
            }
        });

        Oldpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PatientProfileList.class);
                startActivity(intent);
                finish();

            }
        });

        /*download master tables here*/
        if(isInternetOn()){ getMasterTables(HomeActivity.this);}

    }

    private void checkUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if  (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, HomeActivity.IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure to want to exit application");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finishAffinity();
            }
        });
        builder.setNegativeButton(
                "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void getMasterTables(final Activity context) {
        final SqliteHelper sqliteHelper = new SqliteHelper(context);
        sqliteHelper.openDataBase();
        //mProgressDialog= ProgressDialog.show(context, "", "Please Wait...", true);

        for (int j = 0; j < masterTables.length; j++) {
            DataDownloadInput dataDownloadInput = new DataDownloadInput();
            dataDownloadInput.setTable_name(masterTables[j]);
            String date="";
            if (masterTables[j].equals("symptom")){
                date= sqliteHelper.getUpdatedOn(masterTables[j]);
            }else if (masterTables[j].equals("disease")){
                date= sqliteHelper.getUpdatedOn(masterTables[j]);

            }else if (masterTables[j].equals("medicine_list")){
                date= sqliteHelper.getUpdatedOn(masterTables[j]);

            }else if (masterTables[j].equals("test")){
                date= sqliteHelper.getUpdatedOn(masterTables[j]);

            }else if (masterTables[j].equals("sub_tests")){
                date= sqliteHelper.getUpdatedOn(masterTables[j]);

            }else  {
                date= sqliteHelper.getUpdatedDate(masterTables[j]);
            }

            dataDownloadInput.setUpdated_at(date);
            Gson mGson = new Gson();
            String data = mGson.toJson(dataDownloadInput);
            Log.e("Data",data);
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
                        JsonArray data= singledataP.getAsJsonArray("tableData");
                        Log.e("cc", "ccc " + data.toString());

                        for (int i = 0; i < data.size(); i++) {
                            JSONObject singledata = new JSONObject(data.get(i).toString());
                            // JSONObject singledata = data.getJSONObject(i);
                            //singledata.getString("id");
                            Iterator keys = singledata.keys();
                            ContentValues contentValues = new ContentValues();
                            String id="";
                            while (keys.hasNext()) {
                                String currentDynamicKey = (String) keys.next();
                                if(currentDynamicKey.equals("id"))
                                    id=singledata.get(currentDynamicKey).toString();
                                contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                            }
                            if(!sqliteHelper.checkExist(masterTables[finalJ], "id", id))
                                sqliteHelper.saveMasterTable(contentValues, masterTables[finalJ]);
                            else
                                sqliteHelper.updateMasterTable(contentValues, masterTables[finalJ],"id",id);

                        }
                        // mProgressDialog.dismiss();
                    } catch (Exception s) {
                        s.printStackTrace();
                    }

                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("", "");
                    //  mProgressDialog.dismiss();
                }
            });
        }
    }
}
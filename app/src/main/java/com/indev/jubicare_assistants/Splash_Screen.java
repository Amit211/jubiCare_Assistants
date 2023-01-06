package com.indev.jubicare_assistants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash_Screen extends AppCompatActivity {
SharedPrefHelper sharedPrefHelper;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    SqliteHelper sqliteHelper;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        getSupportActionBar().hide();

        sqliteHelper = new SqliteHelper(this);
        sqliteHelper.openDataBase();
        sharedPrefHelper = new SharedPrefHelper(this);
        getPermissionLocation();

     //  String title= sharedPrefHelper.getString("title1","");
        String isSplashLoaded=sharedPrefHelper.getString("isSplashLoaded","");
        String isLogin=sharedPrefHelper.getString("is_login","");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    if (isSplashLoaded.equals("")) {
                        DataDownload dataDownload = new DataDownload();
                        dataDownload.getMasterTables(Splash_Screen.this);
                    } else {
                        if(isLogin.equals("1")) {
                            Intent intent = new Intent(Splash_Screen.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intentL = new Intent(Splash_Screen.this, LoginAcivity.class);
                            startActivity(intentL);
                            finish();
                        }
                    }



            }
        }, SPLASH_DISPLAY_LENGTH);


//        if (title.equals("'Logout'")){
//            callLogoutApi();
//        }
    }
    private void getPermissionLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE},100);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(Splash_Screen.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this,
                        Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(Splash_Screen.this,
                            new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    ActivityCompat.requestPermissions(Splash_Screen.this,
                            new String[]{Manifest.permission.CAMERA}, 1);
                }
            }
        }
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

                        //sharedPrefHelper.setString("is_login", "");
                        Intent i = new Intent(Splash_Screen.this, LoginAcivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        Toast.makeText(Splash_Screen.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
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

}

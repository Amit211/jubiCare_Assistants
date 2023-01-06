package com.indev.jubicare_assistants;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;
import com.indev.jubicare_assistants.videocallingapp.BaseActivity;
import com.indev.jubicare_assistants.videocallingapp.SinchService;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.UserController;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppDrwer extends BaseActivity implements SinchService.StartFailedListener {
    private DrawerLayout drawerLayout;
    public Toolbar toolbar;
    ImageView imageView;
    ProgressDialog progressDialog;
    private View view;
    private Menu menu;
    private Menu navMenu;
    private FrameLayout frame;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Context context = this;
    TextView tv_name, tv_email;
    private SharedPrefHelper sharedPrefHelper;
    private SqliteHelper sqliteHelper;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        view = getLayoutInflater().inflate(R.layout.activity_app_drwer, null);
        frame = view.findViewById(R.id.frame);
        getLayoutInflater().inflate(layoutResID, frame, true);

        super.setContentView(view);
        sharedPrefHelper = new SharedPrefHelper(context);
        sqliteHelper = new SqliteHelper(context);
        drawerLayout = findViewById(R.id.drawer_layout);
        imageView = findViewById(R.id.imageView);
        navigationView = findViewById(R.id.nvView);
        navigationView.inflateMenu(R.menu.new_menu);
        menu = navigationView.getMenu();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        //get preference user date here
        String full_name = sharedPrefHelper.getString("full_name", "");
        String role_id = sharedPrefHelper.getString("role_id", "");
        String profile_pic = sharedPrefHelper.getString("profile_pic", "");
        View header = navigationView.getHeaderView(0);
        tv_name = (TextView) header.findViewById(R.id.name);
        tv_email = (TextView) header.findViewById(R.id.email);
        getSupportActionBar().hide();

        tv_name.setText(full_name);
        tv_email.setText("");

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);//when using our custom drawer icon
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //call method
        initializeView();
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        navMenu = navigationView.getMenu();
    }

    private void initializeView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                int id = item.getItemId();
                switch (id) {
                    case R.id.option_home:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.option_change_password:
                        Intent intent = new Intent(context, ChangePassword.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(AppDrwer.this, "Change Password", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.option_privacy_policy:
                        Intent intentDisclaimer = new Intent(context, Disclaimer.class);
                        startActivity(intentDisclaimer);
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.option_search:
                        Intent intentSearch = new Intent(context, SearchByMobile.class);
                        intentSearch.putExtra("common_search", "common_search");
                        startActivity(intentSearch);
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.option_Contact_us:
                        Intent intentContactUs = new Intent(context, ContactUs.class);
                        startActivity(intentContactUs);
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.option_logout:
                        if (isInternetOn()) {
                            callLogoutApi();

                        } else {
                            Toast.makeText(context, "Please Check Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                }
                return true;
            }

        });
    }

    public void callLogoutApi() {
        SharedPreferences preferences = getSharedPreferences("Sinch", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        if (!sharedPrefHelper.getString("sinch_user", "").equals("")) {
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

        progressDialog = ProgressDialog.show(context, "Logout", "Please Wait...", true);
        CountInput countInput = new CountInput();
        countInput.setUser_id(sharedPrefHelper.getString("user_id", ""));
        Gson mGson = new Gson();
        String data = mGson.toJson(countInput);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        ProgressDialog finalProgressDialog = progressDialog;
        APIClient.getClient().

                create(TELEMEDICINE_API.class).callLogoutApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    finalProgressDialog.dismiss();
                    Log.e("jbfd", "nsh " + jsonObject.toString());
                    if (jsonObject.optString("success").equalsIgnoreCase("1")) {
                        sqliteHelper.dropTable("patient_appointments");
                        sqliteHelper.dropTable("profile_patients");
                        sharedPrefHelper.setString("is_login", "");
                        Intent i = new Intent(AppDrwer.this, LoginAcivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    finalProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean isInternetOn() {
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

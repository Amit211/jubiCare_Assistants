package com.indev.jubicare_assistants;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.model.ChangePasswordModel;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    @BindView(R.id.changePasswordButton)
    Button changePasswordButton;
    @BindView(R.id.etOldPassword)
    EditText etOldPassword;
    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    /*normal widgets*/
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    ProgressDialog mProgressDialog;
    ChangePasswordModel changePasswordModel;
    String strOldpassword = "";
    String strnewpassword = "";
    private Context context = this;
    /*--for validation--*/
    private EditText flagEditfield;
    private String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        setTitle("Change Password");
        initViews();

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    strOldpassword = etOldPassword.getText().toString();
                    strnewpassword = etNewPassword.getText().toString();

                    mProgressDialog = ProgressDialog.show(context, "Change Password", "Please Wait...", true);
                    changePasswordModel = new ChangePasswordModel();
                    changePasswordModel.setNew_password(strnewpassword);
                    changePasswordModel.setOld_password(strOldpassword);
                    changePasswordModel.setUser_id(sharedPrefHelper.getString("user_id", ""));

                    Gson gson = new Gson();
                    String data = gson.toJson(changePasswordModel);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);
                    /*send data here*/
                    sendChangePasswordData(body);
                }
            }
        });
    }

    private void initViews() {
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        mProgressDialog = new ProgressDialog(context);
    }

    private void sendChangePasswordData(final RequestBody changePasswordModel) {
        TELEMEDICINE_API api_service = APIClient.getClient().create(TELEMEDICINE_API.class);
        if (changePasswordModel != null && api_service != null) {
            Call<JsonObject> server_response = api_service.sendChangePassword(changePasswordModel);
            try {
                if (server_response != null) {
                    server_response.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                Log.e("dvfgfh", "onResponse: " + jsonObject.toString());
                                mProgressDialog.dismiss();
                                String success = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                String user_id = jsonObject.getString("user_id");
                                if (success.equalsIgnoreCase("1")) {
                                    sharedPrefHelper.setString("user_id", user_id);
                                    Intent intent1 = new Intent(ChangePassword.this, LoginAcivity.class);
                                    Toast.makeText(ChangePassword.this, "Password "+message, Toast.LENGTH_SHORT).show();
                                    startActivity(intent1);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            mProgressDialog.dismiss();
                        }

                    });
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    private boolean checkValidation() {
        if (etOldPassword.getText().toString().trim().length() == 0) {
            flagEditfield = etOldPassword;
            msg = "Please enter old password!";
            flagEditfield.setError(msg);
            etOldPassword.requestFocus();
            return false;
        }
        if (etNewPassword.getText().toString().trim().length() == 0) {
            flagEditfield = etNewPassword;
            msg = "Please enter new password!";
            flagEditfield.setError(msg);
            etNewPassword.requestFocus();
            return false;
        }
        if (etConfirmPassword.getText().toString().trim().length() == 0) {
            flagEditfield = etConfirmPassword;
            msg = "Please enter confirm password!";
            flagEditfield.setError(msg);
            etConfirmPassword.requestFocus();
            return false;
        }
        if (!etConfirmPassword.getText().toString().trim().equals(etNewPassword.getText().toString().trim())) {
            flagEditfield = etConfirmPassword;
            msg = "Password and confirm password are not matching!";
            flagEditfield.setError(msg);
            etConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
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


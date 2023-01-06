package com.indev.jubicare_assistants;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {
    @BindView(R.id.resetPassword)
    Button resetPassword;
    @BindView(R.id.etMobileNo)
    EditText etMobileNo;

    /*normal widgets*/
    private Context context=this;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    ProgressDialog mProgressDialog;
    ForgetPassworModel forgetPasswordMode;
    String mobile = "";
    /*--for validation--*/
    private EditText flagEditfield;
    private String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ButterKnife.bind(this);
        setTitle("Forgot Password");

        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        mProgressDialog = new ProgressDialog(context);

    }

    @OnClick(R.id.resetPassword)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.resetPassword:
                if (checkValidation()) {
                    forgetPasswordMode = new ForgetPassworModel();
                    mobile = etMobileNo.getText().toString().trim();
                    forgetPasswordMode.setContact_no(mobile);
                    mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
                    Gson gson = new Gson();
                    String data = gson.toJson(forgetPasswordMode);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);
                    /*send data here*/
                    sendForgetPasswordData(body);
                }
                break;
        }
    }

    private void sendForgetPasswordData(final RequestBody forgetPasswordMode) {
        TELEMEDICINE_API api_service = APIClient.getClient().create(TELEMEDICINE_API.class);
        if (forgetPasswordMode != null && api_service != null) {
            Call<JsonObject> server_response = api_service.sendForgetPassword(forgetPasswordMode);
            try {
                if (server_response != null) {
                    server_response.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                Log.e("dvfgfh", "onResponse: "+jsonObject.toString());
                                mProgressDialog.dismiss();
                                String success = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                String otp =jsonObject.getString("otp");
                                String user_id =jsonObject.getString("user_id");
                                if (success.equalsIgnoreCase("1"))  {
                                    Intent intent=new Intent(context, HomeActivity.class);
                                    sharedPrefHelper.setString("set_otp", otp);
                                    sharedPrefHelper.setString("user_id", user_id);
                                    sharedPrefHelper.setString("mobile_no", mobile);
                                    sharedPrefHelper.setString("mobile_no_for_show", mobile);
                                    intent.putExtra("forgot_pass", "forgot_pass");
                                    startActivity(intent);
                                    finish();
                                }

                            } catch (JSONException e) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkValidation() {
        if (etMobileNo.getText().toString().trim().length() == 0) {
            flagEditfield = etMobileNo;
            msg = "Please enter registered mobile number!";
            flagEditfield.setError(msg);
            etMobileNo.requestFocus();
            return false;
        }
        if (etMobileNo.getText().toString().trim().length() < 10) {
            flagEditfield = etMobileNo;
            msg = "Please enter 10 digits mobile number!";
            flagEditfield.setError(msg);
            etMobileNo.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);

    }
}
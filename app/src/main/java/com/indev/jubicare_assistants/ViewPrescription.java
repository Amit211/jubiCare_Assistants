package com.indev.jubicare_assistants;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.model.AppointmentMedicinePrescribedModel;
import com.indev.jubicare_assistants.model.DownloadPrescription;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPrescription extends AppCompatActivity {
    @BindView(R.id.webView)
    WebView webView;
    String appointment_id="";
    String prescription_url="";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prescription);
        ButterKnife.bind(this);
        setTitle("View Prescription");

        /*get intent here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle!= null) {
            appointment_id=bundle.getString("appointment_id", "");
        }
        callDownloadPrescriptionApi(appointment_id);
    }

    private void callDownloadPrescriptionApi(String appointment_id) {
        progressDialog=ProgressDialog.show(this,"","Please wait while loading the '.PDF' file.",true);
        DownloadPrescription downloadPrescription=new DownloadPrescription();

        downloadPrescription.setAppointment_id(appointment_id);
        Gson gson = new Gson();
        String data = gson.toJson(downloadPrescription);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        APIClient.getClientJubiCare().create(TELEMEDICINE_API.class).downloadPrescription(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            prescription_url = jsonObject.getString("prescription_url");
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.getSettings().setLoadWithOverviewMode(true);
                            webView.getSettings().setUseWideViewPort(true);
                            webView.setWebViewClient(new WebViewClient(){
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    progressDialog.dismiss();
                                    view.loadUrl(url);
                                    return true;
                                }
                                @Override
                                public void onPageFinished(WebView view, final String url) {
                                    progressDialog.dismiss();
                                }
                            });
                            //webView.loadUrl("http://www.teluguoneradio.com/rssHostDescr.php?hostId=147");
                            try {
                                prescription_url = URLEncoder.encode(prescription_url, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            //webView.loadUrl(prescription_url);
                            webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+prescription_url);
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Please try again after some time",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("onFailure-TAG>>>", "onFailure: "+t.toString());
            }
        });
    }
}

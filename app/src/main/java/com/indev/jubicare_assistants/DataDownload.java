package com.indev.jubicare_assistants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataDownload {
    private String[] masterTables = {"state", "district", "block", "village", "post_office", "disease", "medicine_list", "test", "sub_tests","prescription_eating_schedule","prescription_days","prescription_interval","blood_group","caste", "symptom","profile_pharmacists","profile_doctors"};
    SharedPrefHelper sharedPrefHelper;
    Context context;
    ProgressDialog mProgressDialog;

    public void getMasterTables(final Activity context) {
        final SqliteHelper sqliteHelper = new SqliteHelper(context);
        sqliteHelper.openDataBase();
//        mProgressDialog= ProgressDialog.show(context, "", "Please Wait...", true);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (int j = 0; j < masterTables.length; j++) {
                    DataDownloadInput dataDownloadInput = new DataDownloadInput();
                    dataDownloadInput.setTable_name(masterTables[j]);
                    dataDownloadInput.setUpdated_at("");
                    Gson mGson = new Gson();
                    String data = mGson.toJson(dataDownloadInput);
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
                                sqliteHelper.dropTable(masterTables[finalJ]);
                                //String tableData=singledata.getString("tableData");
                                JsonArray data= singledataP.getAsJsonArray("tableData");
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
                                if(masterTables[finalJ].equals("symptom")){
                                    sharedPrefHelper=new SharedPrefHelper(context);
                                    sharedPrefHelper.setString("isSplashLoaded","Yes");
                                    Intent intent=new Intent(context, LoginAcivity.class);
                                    context.startActivity(intent);
                                    context.finish();
                                }
                            } catch (Exception s) {
                                s.printStackTrace();
                            }

                        }
                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d("", "");
                        }
                    });

                }
                return null;

            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }
}

package com.indev.jubicare_assistants;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.adapter.BookingListAdapter;
import com.indev.jubicare_assistants.adapter.PatientListAdapter;
import com.indev.jubicare_assistants.interfaces.OnLoadMoreListener;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookinlistActivity extends AppCompatActivity {
    @BindView(R.id.rv_Patient_patient_list)
    RecyclerView rv_Patient_patient_list;
    @BindView(R.id.tv_addprofile)
    TextView tv_addprofile;
    @BindView(R.id.etSearchBar)
    EditText etSearchBar;
    @BindView(R.id.btnSearch)
    ImageView btnSearch;
    @BindView(R.id.tv_list_count)
    TextView tv_list_count;
    @BindView(R.id.tvNoDataFound)
    TextView tvNoDataFound;
    @BindView(R.id.btn_go_home)
    TextView btn_go_home;
    ProgressDialog mprogressDialog;
    /*normal widgets*/
    private Context context = this;
    SharedPrefHelper sharedPrefHelper;
    BookingListAdapter bookingListAdapter;
    PatientModel patientModel = new PatientModel();
PatientPojo patientPojo;
SqliteHelper sqliteHelper;
    ArrayList<PatientPojo> patientPojoArrayList ;
    private ProgressDialog mProgressDialog;
    String searchInput;
    String contect_no;
    String contact;
    SignUpModel signUpModel;
    public static OnLoadMoreListener mOnLoadMoreListener;
    public static boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int offset=0, limit=10;
    private int totalListCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookinlist);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        setTitle("Patient List");
        tv_addprofile = findViewById(R.id.tv_addprofile);
sqliteHelper =new SqliteHelper(context);
patientPojo =new PatientPojo();
        initViews();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            contect_no=bundle.getString("et_contect", "");
        }
        setData();
        if (contect_no!=null) {
         //  callPatientListApi();
        }
        else {
           // callPatientListApi1();
            Toast.makeText(context, "No Any member", Toast.LENGTH_SHORT).show();
        }

        }



    @OnClick({R.id.tv_addprofile, R.id.btnSearch, R.id.btn_go_home})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_addprofile:
                Intent intent = new Intent(context, SignUp.class);
                intent.putExtra("addMember", "addMember");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.btnSearch:
                searchInput = etSearchBar.getText().toString().trim();
               // callPatientListApi1();
                break;
            case R.id.btn_go_home:
                Intent intentPatient = new Intent(context, HomeActivity.class);
                intentPatient.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentPatient);
                finish();
                break;
        }

    }

    private void setData() {
        if (!contect_no.equalsIgnoreCase("")) {
            patientPojoArrayList = sqliteHelper.getpatientlist1(contect_no);
            tv_list_count.setText("Total - " + patientPojoArrayList.size());
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            bookingListAdapter = new BookingListAdapter(context, patientPojoArrayList);
            rv_Patient_patient_list.setLayoutManager(mLayoutManager);
            rv_Patient_patient_list.setAdapter(bookingListAdapter);
            bookingListAdapter.onItemClick(new BookingListAdapter.ClickListener() {
                @Override
                public void onItemClick(int position) {
                }

                @Override
                public void onListItemClick(int position) {
                    Intent intent = new Intent(context, PatientFillAppointment.class);
                    intent.putExtra("patient", "patient");
                    intent.putExtra("profile_patient_id", patientPojoArrayList.get(position).getProfile_patient_id());
                    startActivity(intent);
                    finish();
                }
            });
        }
        else {
            patientPojoArrayList = sqliteHelper.getpatientlist(offset, limit);
            /*for (int i = 0; i < 10; i++) {
                PatientPojo user = new PatientPojo();
                user.setFull_name("Name " + i);
                user.setAge("25");
                user.setGender("M");
                user.setFlag("1");
                user.setState_id("1");
                user.setDistrict_id("15");
                user.setBlock_id("2");
                user.setVillage_id("3");
                user.setContact_no("999" + i + "380292");
                user.setProfile_patient_id("1" + i + "1");
                patientArrayList.add(user);
            }*/
            tv_list_count.setText("Total - " + totalListCount+"");
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            bookingListAdapter = new BookingListAdapter(context, patientPojoArrayList);
            rv_Patient_patient_list.setLayoutManager(mLayoutManager);
            rv_Patient_patient_list.setAdapter(bookingListAdapter);
            bookingListAdapter.onItemClick(new BookingListAdapter.ClickListener() {
                @Override
                public void onItemClick(int position) {
                }

                @Override
                public void onListItemClick(int position) {
                    Intent intent = new Intent(context, PatientFillAppointment.class);
                    intent.putExtra("patient", "patient");
                    intent.putExtra("profile_patient_id", patientPojoArrayList.get(position).getProfile_patient_id());
                    startActivity(intent);
                    finish();
                }
            });

            /**
             * implementation of load more functionality on scroll
             **/
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv_Patient_patient_list.getLayoutManager();
            rv_Patient_patient_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (totalListCount>totalItemCount) {
                            if (mOnLoadMoreListener != null) {
                                mOnLoadMoreListener.onLoadMore();
                            }
                            isLoading = true;
                        }else {
                            //  Toast.makeText(context, "No more data left to load.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            bookingListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override public void onLoadMore() {
                    Log.e("haint", "Load More");
                    patientPojoArrayList.add(null);
                    bookingListAdapter.notifyItemInserted(patientPojoArrayList.size() - 1);
                    //Load more data for reyclerview
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            Log.e("haint", "Load More 2");
                            //Remove loading item
                            patientPojoArrayList.remove(patientPojoArrayList.size() - 1);
                            bookingListAdapter.notifyItemRemoved(patientPojoArrayList.size());
                            //Load data
                            int index = patientPojoArrayList.size();
                            limit=index+10;//add previous load data with 10 to exceed limit
                            int end = index + 10;
                            //offset=end;//add end index to with 10 more item to offset
                            /*for (int i = index; i < end; i++) {
                                PatientPojo user = new PatientPojo();
                                user.setFull_name("Name " + i);
                                user.setAge("25");
                                user.setGender("M");
                                user.setFlag("1");
                                user.setState_id("1");
                                user.setDistrict_id("15");
                                user.setBlock_id("2");
                                user.setVillage_id("3");
                                user.setContact_no("999" + i + "380292");
                                user.setProfile_patient_id("1" + i + "1");
                                patientArrayList.add(user);
                            }*/
                            ArrayList<PatientPojo> patientArrayListN = sqliteHelper.getpatientlist(index, limit);//get more data
                            patientPojoArrayList.addAll(patientArrayListN);
                            bookingListAdapter.notifyDataSetChanged();
                            bookingListAdapter.setLoaded();
                        }
                    }, 5000);
                }
            });
        }
    }
    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

//            patientPojoArrayList = sqliteHelper.getpatientlist(0,0);
//            tv_list_count.setText("Total - " + patientPojoArrayList.size());
//            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
//            bookingListAdapter = new BookingListAdapter(context, patientPojoArrayList);
//            rv_Patient_patient_list.setLayoutManager(mLayoutManager);
//            rv_Patient_patient_list.setAdapter(bookingListAdapter);
//
//            bookingListAdapter.onItemClick(new BookingListAdapter.ClickListener() {
//                @Override
//                public void onItemClick(int position) {
//                }
//                @Override
//                public void onListItemClick(int position) {
//                    Intent intent = new Intent(context, PatientFillAppointment.class);
//                    intent.putExtra("patient", "patient");
//                    intent.putExtra("profile_patient_id", patientPojoArrayList.get(position).getProfile_patient_id());
//                    startActivity(intent);
//                    finish();
//                }
//            });






//    private void callPatientListApi() {
//        mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
//        patientModel.setContact_no(contect_no);
//        patientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
//        patientModel.setRole_id(sharedPrefHelper.getString("role_id", ""));
//        patientModel.setMobile(searchInput);
//
//        Gson mGson = new Gson();
//        String data = mGson.toJson(patientModel);
//
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, data);
//
//        APIClient.getClient().create(TELEMEDICINE_API.class).searchData(body).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().toString());
//                        mProgressDialog.dismiss();
//                        patientContentValue.clear();
//                        String success = jsonObject.getString("success");
//                        if (success.equals("1")) {
//
//                        }
//                        JsonObject singledataP = response.body();
//                        JsonArray data = singledataP.getAsJsonArray("tableData");
//                        if (data.size() > 0) {
//                            for (int i = 0; i < data.size(); i++) {
//                                JSONObject singledata = new JSONObject(data.get(i).toString());
//                                Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());
//
//                                Iterator keys = singledata.keys();
//                                ContentValues contentValues = new ContentValues();
//                                while (keys.hasNext()) {
//                                    String currentDynamicKey = (String) keys.next();
//                                    contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
//                                }
//                                patientContentValue.add(contentValues);
//                                /*total count of list*/
//                                tv_list_count.setText("Total - " + patientContentValue.size());
//
//                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
//                                bookingListAdapter = new BookingListAdapter(context, patientContentValue);
//                                rv_Patient_patient_list.setLayoutManager(mLayoutManager);
//                                rv_Patient_patient_list.setAdapter(bookingListAdapter);
//                                bookingListAdapter.onItemClick(new BookingListAdapter.ClickListener() {
//                                    @Override
//                                    public void onItemClick(int position) {
//
//                                    }
//
//                                    @Override
//                                    public void onListItemClick(int position) {
//                                        Intent intent = new Intent(context, PatientFillAppointment.class);
//                                        intent.putExtra("patient", "patient");
//                                        intent.putExtra("profile_patient_id", patientContentValue.get(position).get("profile_patient_id").toString());
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                });
//                            }
//                        } else {
//                            tv_list_count.setVisibility(View.GONE);
//                            rv_Patient_patient_list.setVisibility(View.GONE);
//                            tvNoDataFound.setVisibility(View.VISIBLE);
//                            btn_go_home.setVisibility(View.VISIBLE);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                mProgressDialog.dismiss();
//            }
//        });
//    }
//
//    private void callPatientListApi1() {
//        mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
////        patientModel.setContact_no(contect_no);
//        patientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
//        patientModel.setRole_id(sharedPrefHelper.getString("role_id", ""));
//        patientModel.setMobile(searchInput);
//
//        Gson mGson = new Gson();
//        String data = mGson.toJson(patientModel);
//
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, data);
//
//        APIClient.getClient().create(TELEMEDICINE_API.class).patientListingApi(body).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().toString());
//                        mProgressDialog.dismiss();
//                        patientContentValue.clear();
//                        String success = jsonObject.getString("success");
//                        if (success.equals("1")) {
//
//                        }
//                        JsonObject singledataP = response.body();
//                        JsonArray data = singledataP.getAsJsonArray("tableData");
//                        if (data.size() > 0) {
//                            for (int i = 0; i < data.size(); i++) {
//                                JSONObject singledata = new JSONObject(data.get(i).toString());
//                                Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());
//
//                                Iterator keys = singledata.keys();
//                                ContentValues contentValues = new ContentValues();
//                                while (keys.hasNext()) {
//                                    String currentDynamicKey = (String) keys.next();
//                                    contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
//                                }
//                                patientContentValue.add(contentValues);
//                                /*total count of list*/
//                                tv_list_count.setText("Total - " + patientContentValue.size());
//
//                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
//                                bookingListAdapter = new BookingListAdapter(context, patientContentValue);
//                                rv_Patient_patient_list.setLayoutManager(mLayoutManager);
//                                rv_Patient_patient_list.setAdapter(bookingListAdapter);
//                                bookingListAdapter.onItemClick(new BookingListAdapter.ClickListener() {
//                                    @Override
//                                    public void onItemClick(int position) {
//
//                                    }
//
//                                    @Override
//                                    public void onListItemClick(int position) {
//                                        Intent intent = new Intent(context, PatientFillAppointment.class);
//                                        intent.putExtra("patient", "patient");
//                                        intent.putExtra("profile_patient_id", patientContentValue.get(position).get("profile_patient_id").toString());
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                });
//                            }
//                        } else {
//                            tv_list_count.setVisibility(View.GONE);
//                            rv_Patient_patient_list.setVisibility(View.GONE);
//                            tvNoDataFound.setVisibility(View.VISIBLE);
//                            btn_go_home.setVisibility(View.VISIBLE);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                mProgressDialog.dismiss();
//            }
//        });
//    }
//



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(BookinlistActivity.this, BookingActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void initViews() {
        sharedPrefHelper = new SharedPrefHelper(this);
        signUpModel=new SignUpModel();
        mProgressDialog = new ProgressDialog(context);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BookinlistActivity.this, BookingActivity.class);
        startActivity(intent);
        finish();
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
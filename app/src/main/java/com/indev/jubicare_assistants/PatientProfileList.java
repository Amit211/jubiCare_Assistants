package com.indev.jubicare_assistants;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.adapter.BookingListAdapter;
import com.indev.jubicare_assistants.adapter.PatientListAdapter;
import com.indev.jubicare_assistants.interfaces.OnLoadMoreListener;
import com.indev.jubicare_assistants.model.PharmacyPatientModel;
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

public class PatientProfileList extends AppCompatActivity {
    @BindView(R.id.rv_profile)
    RecyclerView rv_profile;
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
    String contect_no;
    SqliteHelper sqliteHelper;
    /*normal widgets*/
    private Context context = this;
    SharedPrefHelper sharedPrefHelper;
    PatientListAdapter patientListAdapter;
    ArrayList<PatientPojo> patientArrayList=new ArrayList<>();
    String searchInput;
    //on Load more
    public static OnLoadMoreListener mOnLoadMoreListener;
    public static boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int offset=0, limit=10;
    private int totalListCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        setTitle("Patient  List");
        initViews();
        sqliteHelper =new SqliteHelper(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            searchInput = bundle.getString("searchInput", "");
//            appointment_for = bundle.getString("appointment_for", "");
//            common_search = bundle.getString("common_search", "");
//            common_search_counsellor = bundle.getString("common_search_counsellor", "");
           // search_by_name = bundle.getString("search_by_name", "");
            etSearchBar.setText(searchInput);
            searchInput = etSearchBar.getText().toString().trim();
            contect_no = bundle.getString("et_contect", "");
        }
        if (contect_no!=null) {
            // callPatientListApi1();
        }else {
            //  callPatientListApi();
        }

        // setList();

        searchInput = etSearchBar.getText().toString();
        setList(searchInput);

        etSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchInput = etSearchBar.getText().toString();
                setList(searchInput);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @OnClick({R.id.btnSearch, R.id.btn_go_home})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                searchInput = etSearchBar.getText().toString().trim();
                // callPatientListApi2();
                break;
            case R.id.btn_go_home:
                Intent intentPatient = new Intent(context, HomeActivity.class);
                intentPatient.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentPatient);
                finish();
                break;
        }
    }
    private  void setList(String searchInput){
        totalListCount=sqliteHelper.getTotalListCount();
        if (!searchInput.equalsIgnoreCase("")) {
            patientArrayList = sqliteHelper.getpatientlist1(searchInput);
            tv_list_count.setText("Total - " + totalListCount+"");
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            patientListAdapter = new PatientListAdapter(context, patientArrayList);
            rv_profile.setLayoutManager(mLayoutManager);
            rv_profile.setAdapter(patientListAdapter);
            patientListAdapter.onItemClick(new PatientListAdapter.ClickListener() {
                @Override
                public void onItemClick(int position) {
                }
                @Override
                public void onListItemClick(int position) {
                    Intent intent = new Intent(context, CommonProfile.class);
                    intent.putExtra("profile_tab", "profile_tab");
                    intent.putExtra("profile_patient_id", patientArrayList.get(position).getProfile_patient_id());
                    startActivity(intent);
                }
            });

        }
        else {
            patientArrayList = sqliteHelper.getpatientlist(offset, limit);
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
            patientListAdapter = new PatientListAdapter(context, patientArrayList);
            rv_profile.setLayoutManager(mLayoutManager);
            rv_profile.setAdapter(patientListAdapter);
            patientListAdapter.onItemClick(new PatientListAdapter.ClickListener() {
                @Override
                public void onItemClick(int position) {
                }

                @Override
                public void onListItemClick(int position) {
                    Intent intent = new Intent(context, CommonProfile.class);
                    intent.putExtra("profile_tab", "profile_tab");
                    intent.putExtra("profile_patient_id", patientArrayList.get(position).getProfile_patient_id());
                    startActivity(intent);
                }
            });

            /**
             * implementation of load more functionality on scroll
             **/
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv_profile.getLayoutManager();
            rv_profile.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

            patientListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override public void onLoadMore() {
                    Log.e("haint", "Load More");
                    patientArrayList.add(null);
                    patientListAdapter.notifyItemInserted(patientArrayList.size() - 1);
                    //Load more data for reyclerview
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            Log.e("haint", "Load More 2");
                            //Remove loading item
                            patientArrayList.remove(patientArrayList.size() - 1);
                            patientListAdapter.notifyItemRemoved(patientArrayList.size());
                            //Load data
                            int index = patientArrayList.size();
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
                            patientArrayList.addAll(patientArrayListN);
                            patientListAdapter.notifyDataSetChanged();
                            patientListAdapter.setLoaded();
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

    private void initViews() {
        sharedPrefHelper = new SharedPrefHelper(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(PatientProfileList.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PatientProfileList.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}

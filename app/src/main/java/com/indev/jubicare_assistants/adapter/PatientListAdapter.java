package com.indev.jubicare_assistants.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.AppointmentInput;
import com.indev.jubicare_assistants.HomeActivity;
import com.indev.jubicare_assistants.PatientPojo;
import com.indev.jubicare_assistants.PatientProfileList;
import com.indev.jubicare_assistants.R;
import com.indev.jubicare_assistants.SharedPrefHelper;
import com.indev.jubicare_assistants.SignUpModel;
import com.indev.jubicare_assistants.interfaces.OnLoadMoreListener;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<PatientPojo> listModels;

    ClickListener clickListener;
    private long different;
    private long ellapsedmonth;
    private long ellapsedDays;
    private long ellapsedHours;
    private long ellapsedMinutes;
    private long ellapsedSeconds;
    SharedPrefHelper sharedPrefHelper;
    SqliteHelper sqliteHelper;
    String district_name = "";
    String block_name = "";
    String village_name = "";
    String stste_name = "";
    // String flag = "";
    String profile_id = "";
    String local_id = "";
    String localId = "";
    android.app.Dialog appointment_alert;
    int counter;
    ProgressDialog mProgressDialog;
    SignUpModel signUpModel;
    AppointmentInput appointmentInput;
    ArrayList<AppointmentInput> appointmentInputArrayList;
    private String flagPatientProfile="";
    //on load more
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public PatientListAdapter(Context context, List<PatientPojo> listModels) {
        this.context = context;
        this.listModels = listModels;
        sharedPrefHelper = new SharedPrefHelper(context);
        sqliteHelper=new SqliteHelper(context);
        signUpModel=new SignUpModel();
        mProgressDialog=new ProgressDialog(context);
        appointmentInput=new AppointmentInput();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        PatientProfileList.mOnLoadMoreListener = mOnLoadMoreListener;
    }
    @Override public int getItemViewType(int position) {
        return listModels.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_patient_list, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_item, parent, false);
            return new PatientProfileList.LoadingViewHolder(view);
        }
        return null;
        /*View view = LayoutInflater.from(context).inflate(R.layout.activity_patient_list, parent, false);
        return new ViewHolder(view);*/
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tv_patient_name.setText(listModels.get(position).getFull_name());
            viewHolder.tv_age.setText(listModels.get(position).getAge() + " Year");
            viewHolder.tv_contact_no.setText(listModels.get(position).getContact_no());
            //profile_id = (listModels.get(position).getProfile_patient_id());
            flagPatientProfile = listModels.get(position).getFlag();
            // flag =holder.text.getText().toString();
            stste_name = sqliteHelper.getstateName(listModels.get(position).getState_id(), "state");
            district_name = sqliteHelper.getdistrictName(listModels.get(position).getDistrict_id(), "district");
            block_name = sqliteHelper.getblockName(listModels.get(position).getBlock_id(), "block");
            village_name = sqliteHelper.getvillageName(listModels.get(position).getVillage_id(), "village");
            viewHolder.tv_location.setText(("" + village_name) + ", " +
                    ("" + block_name) + ", " +
                    ("" + district_name) + ", " +
                    ("" + stste_name));
            try {
                if (flagPatientProfile.equals("1")) {
                    viewHolder.ib_checked.setVisibility(View.GONE);
                    appointmentInputArrayList = sqliteHelper.syCAppointmentOFLineData1(listModels.get(position).getProfile_patient_id());
                    if (appointmentInputArrayList.size() > 0) {
                        viewHolder.ib_checked1.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.ib_checked1.setVisibility(View.GONE);
                    }
                } else {
                    viewHolder.ib_checked.setVisibility(View.VISIBLE);
                    viewHolder.ib_checked1.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int count = sqliteHelper.getAppointmentCount(Integer.parseInt(listModels.get(position).getProfile_patient_id()));
            viewHolder.tv_appointment_count.setText("Appointment Count - " + count);

            viewHolder.ib_checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.ib_checked.setEnabled(false);
                    mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
                    signUpModel = sqliteHelper.SyncOFlinePatientDetail(listModels.get(position).getProfile_patient_id());
                    Gson gson = new Gson();
                    local_id = signUpModel.getProfile_patient_id();
                    //oldId = signUpModel.getProfile_patient_id();
                    String data = gson.toJson(signUpModel);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);
                    /*send data here*/
                    TELEMEDICINE_API api_service = APIClient.getClient().create(TELEMEDICINE_API.class);
                    if (signUpModel != null && api_service != null) {
                        Call<JsonObject> server_response = api_service.sendSignupData(body);
                        try {
                            if (server_response != null) {
                                server_response.enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        if (response.isSuccessful()) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                                mProgressDialog.dismiss();
                                                String success = jsonObject.getString("success");
                                                if (success.equals("1")) {
                                                    // holder.ib_checke.sdetBackgroundColor(Color.GREEN);
                                                    String profile_id = jsonObject.getString("profile_id");
                                                    sqliteHelper.updateFlagInTable("profile_patients", "id", Integer.parseInt((local_id)), 1);
                                                    sqliteHelper.updatePatientIdInTable("profile_patients", "id", Integer.parseInt((local_id)), Integer.parseInt(profile_id));
                                                    sqliteHelper.updateDependentTable("patient_appointments", "profile_patient_id", "profile_patient_id", Integer.parseInt((local_id)), Integer.parseInt(profile_id));

                                                    appointmentInput = sqliteHelper.syncAppointmentOFlinData(profile_id);
                                                    localId = appointmentInput.getProfile_patient_id();
                                                    Gson gson = new Gson();
                                                    String data = gson.toJson(appointmentInput);
                                                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                                    RequestBody body = RequestBody.create(JSON, data);
                                                    /*send data here*/
                                                    sendAppointmentData(body, Integer.parseInt(localId));
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        //  Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();
                                    }

                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }
            });

            viewHolder.ib_checked1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appointmentInputArrayList = sqliteHelper.syCAppointmentOFLineData1(listModels.get(position).getProfile_patient_id());
                    if (appointmentInputArrayList.size() > 0) {
                        for (int i = 0; i < appointmentInputArrayList.size(); i++) {
                            Gson gson = new Gson();
                            String data = gson.toJson(appointmentInputArrayList.get(i));
                            String localid = appointmentInputArrayList.get(i).getLocal_id();
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(JSON, data);
                            /*send data here*/
                            mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
                            APIClient.getClient().create(TELEMEDICINE_API.class).sendAppointdata(body).enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().toString());
                                        mProgressDialog.dismiss();
                                        String success = jsonObject.optString("success");
                                        if (success.equals("1")) {
                                            String message = jsonObject.optString("message");
                                            String appointment_id = jsonObject.optString("appointment_id");
                                            sqliteHelper.updateFlagInTable("patient_appointments", "local_id", Integer.parseInt((localid)), 1);
                                            sqliteHelper.updateDependentTable("patient_appointments", "id", "local_id", Integer.parseInt(localid), Integer.parseInt(appointment_id));

                                            appointment_alert = new android.app.Dialog(context);
                                            appointment_alert.setContentView(R.layout.submit_appointment_from_counsellor_dialog);
                                            appointment_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            WindowManager.LayoutParams params = appointment_alert.getWindow().getAttributes();
                                            params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
                                            TextView tv_appointment_added = (TextView) appointment_alert.findViewById(R.id.tv_appointment_added);
                                            TextView tv_appointment_msg = (TextView) appointment_alert.findViewById(R.id.tv_appointment_msg);
                                            Button btn_ok = (Button) appointment_alert.findViewById(R.id.btn_ok);
                                            Button btn_assigned_doctor = (Button) appointment_alert.findViewById(R.id.btn_assigned_doctor);

                                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    appointment_alert.dismiss();
                                                    context.startActivity(new Intent(context, HomeActivity.class));
                                                }
                                            });

                                            appointment_alert.show();
                                            appointment_alert.setCanceledOnTouchOutside(false);


                                            // Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

                                        } else {
                                            mProgressDialog.dismiss();
                                            Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    // Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    mProgressDialog.dismiss();
                                }
                            });
                        }
                    }
                }
            });

            /*for date in sec, mint, hours and days*/
            String upCommeingDate = "";
            if (sharedPrefHelper.getString("role_id", "").equalsIgnoreCase("")) {
                if (listModels.get(position).getCreated_at() != null) {
                    upCommeingDate = listModels.get(position).getCreated_at();
                }
            } else {
                if (listModels.get(position).getCreated_at() != null) {
                    upCommeingDate = listModels.get(position).getCreated_at();
                }
            }
            if (upCommeingDate != null) {
                calculateTimeDifference(upCommeingDate, viewHolder.tv_time_ago);
            }

            /*get image path*/
            String url = APIClient.IMAGE_URL + listModels.get(position).getProfile_pic();
            if (listModels.get(position).getProfile_pic() == null || listModels.get(position).getProfile_pic().trim().length()<1) {
                if (listModels.get(position).getGender().equalsIgnoreCase("M")) {
                    viewHolder.iv_patient_image.setImageResource(R.drawable.male_icon);
                } else {
                    viewHolder.iv_patient_image.setImageResource(R.drawable.female_icon);
                }
            } else {
                try {
                    if (listModels.get(position).getGender().equalsIgnoreCase("M")) {
                        Picasso.with(context)
                                .load(url)
                                .placeholder(R.drawable.male_icon)
                                .into(viewHolder.iv_patient_image);
                    } else {
                        Picasso.with(context)
                                .load(url)
                                .placeholder(R.drawable.female_icon)
                                .into(viewHolder.iv_patient_image);
                    }
                }catch(Exception ex){
                    Log.d("Picasso Error",ex.getMessage());
                }
            }

            viewHolder.ll_patient_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onListItemClick(position);
                }
            });
        } else if (holder instanceof PatientProfileList.LoadingViewHolder) {
            PatientProfileList.LoadingViewHolder loadingViewHolder = (PatientProfileList.LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    private void sendAppointmentData(RequestBody body,int localId) {
        mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
        APIClient.getClient().create(TELEMEDICINE_API.class).sendAppointdata(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    mProgressDialog.dismiss();
                    Log.e("chjJC", "njkdvnv " + jsonObject.toString());
                    String success = jsonObject.optString("success");
                    if (success.equals("1")) {
                        String message = jsonObject.optString("message");
                        String appointment_id = jsonObject.optString("appointment_id");
                        sqliteHelper.updateFlagInTable("patient_appointments", "profile_patient_id",  ((localId)), 1);
                        sqliteHelper.updateDependentTable("patient_appointments","id", "profile_patient_id", (localId), Integer.parseInt(appointment_id));

                        //Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        showAlertDialogForPatientAppointment();
                    } else {
                        mProgressDialog.dismiss();
                        // Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    private void showAlertDialogForPatientAppointment() {
        appointment_alert = new android.app.Dialog(context);

        appointment_alert.setContentView(R.layout.submit_appointment_from_patient_dialog);
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
                Intent intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        appointment_alert.show();
        appointment_alert.setCanceledOnTouchOutside(false);
    }
    //convert time into days, month and years
    // to get the difference between two dates

    public void calculateTimeDifference(String strEndDate, TextView textView) {
        //SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            /*todo get current date time and change its format*/
            Calendar calendar = Calendar.getInstance();
            Date startDate = calendar.getTime();

            String mySdate = outputDateFormat.format(startDate);
            Date dateStart = outputDateFormat.parse(mySdate);

            /*todo change date time format of end date*/

            Date sDate = inputDateFormat.parse(strEndDate);



            //inputDateFormat.setTimeZone(TimeZone.getDefault());
            String myFormattedDate = outputDateFormat.format(sDate);

            Date dateEnd = outputDateFormat.parse(myFormattedDate);

            getTimeDifference(dateEnd, dateStart, textView);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void getTimeDifference(Date startDate, Date endDate, TextView textView) {
        //milliseconds
        different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long month = daysInMilli * 30;


        ellapsedmonth = different / month;
        different = different % month;

        ellapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        ellapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        ellapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        ellapsedSeconds = different / secondsInMilli;


        if (ellapsedmonth > 0) {

            textView.setText(" " + String.valueOf(ellapsedmonth) + " month" + "-" + ellapsedDays + " days");

        } else if (ellapsedDays > 0) {
            if (ellapsedDays > 1) {
                textView.setText(" " + String.valueOf(ellapsedDays) + " days ago");

            } else {
                textView.setText(" " + String.valueOf(ellapsedDays) + " day ago");
            }
        } else if (ellapsedHours > 0) {
            if (ellapsedHours > 1) {
                textView.setText(" " + String.valueOf(ellapsedHours) + " hours ago");
            } else {
                textView.setText(" " + String.valueOf(ellapsedHours) + " hour ago");
            }

        } else if (ellapsedMinutes > 0) {
            if (ellapsedMinutes > 1) {
                textView.setText(" " + String.valueOf(ellapsedMinutes) + " min ago");
            } else {
                textView.setText(" " + String.valueOf(ellapsedMinutes) + " min ago");
            }

        } else if (ellapsedSeconds > 0) {
            if (ellapsedSeconds > 1) {
                textView.setText(" " + String.valueOf(ellapsedSeconds) + " sec ago");
            } else {
                textView.setText(" " + String.valueOf(ellapsedSeconds) + " sec ago");
            }
        } else {
            textView.setText("0");
        }
    }

    public String getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return ("" + (int) dayCount + " day(s) ago");
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ib_checked)
        TextView ib_checked;
        @BindView(R.id.ib_checked1)
        TextView ib_checked1;
        @BindView(R.id.tv_contact_no)
        TextView tv_contact_no;
        @BindView(R.id.text)
        TextView text;
        @BindView(R.id.tv_patient_name)
        TextView tv_patient_name;
        @BindView(R.id.iv_patient_image)
        ImageView iv_patient_image;
        @BindView(R.id.cv_main)
        CardView cv_main;
        /*@BindView(R.id.tv_state)
        TextView tv_state;
        @BindView(R.id.tv_district)
        TextView tv_district;
        @BindView(R.id.tv_block)
        TextView tv_block;
        @BindView(R.id.tv_village)
        TextView tv_village;*/
        @BindView(R.id.tv_age)
        TextView tv_age;
        @BindView(R.id.ll_patient_list)
        LinearLayout ll_patient_list;
        @BindView(R.id.tv_time_ago)
        TextView tv_time_ago;
        @BindView(R.id.tv_location)
        TextView tv_location;
        @BindView(R.id.tv_appointment_count)
        TextView tv_appointment_count;
//        @BindView(R.id.ll_addapppointment)
//        TextView ll_addapppointment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ClickListener {
        void onItemClick(int position);

        void onListItemClick(int position);
    }

    public void onItemClick(ClickListener listener) {
        this.clickListener = listener;
    }

    public void setLoaded() {
        PatientProfileList.isLoading = false;
    }
    
}

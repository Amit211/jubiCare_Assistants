package com.indev.jubicare_assistants.service;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.R;
import com.indev.jubicare_assistants.SharedPrefHelper;
import com.indev.jubicare_assistants.Splash_Screen;
import com.indev.jubicare_assistants.model.AppointmentMedicinePrescribedModel;
import com.indev.jubicare_assistants.model.PharmacyPatientModel;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;
import com.indev.jubicare_assistants.videocallingapp.IncomingCallScreenActivity;
import com.indev.jubicare_assistants.videocallingapp.SinchService;
import com.sinch.android.rtc.NotificationResult;
import com.sinch.android.rtc.SinchHelpers;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallNotificationResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService implements ServiceConnection, CallClientListener {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static String CHANNEL_ID = "Sinch Push Notification Channel";

    private final String PREFERENCE_FILE = "com.sinch.android.rtc.sample.push.shared_preferences";
    SharedPreferences sharedPreferences;
    private NotificationUtils notificationUtils;
    SharedPrefHelper sharedPrefHelper;
    SqliteHelper sqliteHelper;
    //private ProgressDialog mProgressDialog;
    PharmacyPatientModel pharmacyPatientModel= new PharmacyPatientModel();
    String user_id="";Context context=this;
    public SinchService.SinchServiceInterface mSinchServiceInterface;
    String callID="";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sharedPrefHelper=new SharedPrefHelper(getApplicationContext());
        sqliteHelper=new SqliteHelper(getApplicationContext());
        if (!remoteMessage.getData().containsKey("data")) {
            Map data = remoteMessage.getData();
            if (SinchHelpers.isSinchPushPayload(data)) {
                new ServiceConnection() {
                    private Map payload;

                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {

                        Context context = getApplicationContext();
                        sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);

                        if (payload != null) {
                            SinchService.SinchServiceInterface sinchService = (SinchService.SinchServiceInterface) service;
                            if (sinchService != null) {
                                NotificationResult result = sinchService.relayRemotePushNotificationPayload(payload);
                                // handle result, e.g. show a notification or similar
                                // here is example for notifying user about missed/canceled call:
                                if (result.isValid() && result.isCall()) {
                                    CallNotificationResult callResult = result.getCallResult();
                                    if (callResult != null && result.getDisplayName() != null) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(callResult.getRemoteUserId(), result.getDisplayName());
                                        editor.commit();
                                    }
                                    if (callResult.isCallCanceled()) {
                                        String displayName = result.getDisplayName();
                                        if (displayName == null) {
                                            displayName = sharedPreferences.getString(callResult.getRemoteUserId(), "n/a");
                                        }
                                        createMissedCallNotification(displayName != null && !displayName.isEmpty() ? displayName : callResult.getRemoteUserId());
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            context.deleteSharedPreferences(PREFERENCE_FILE);
                                        }
                                    }
                                }
                            }
                        }
                        payload = null;
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                    }

                    public void relayMessageData(Map<String, String> data) {
                        payload = data;
                        createNotificationChannel(NotificationManager.IMPORTANCE_MAX);
                        getApplicationContext().bindService(new Intent(getApplicationContext(), SinchService.class), this, BIND_AUTO_CREATE);
                    }
                }.relayMessageData(data);
            }

        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void createNotificationChannel(int importance) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Sinch";
            String description = "Incoming Sinch Push Notifications.";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createMissedCallNotification(String userId) {
        createNotificationChannel(NotificationManager.IMPORTANCE_DEFAULT);
        Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), IncomingCallScreenActivity.class), 0);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle("Missed call from ")
                        .setContentText(userId)
                        .setSound(defaultRintoneUri)
                        .setContentIntent(contentIntent)
                        .setDefaults(android.app.Notification.DEFAULT_VIBRATE)
                        .setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, builder.build());
    }

    ///To check if the app is in foreground ///
    public static boolean foregrounded() {
        ActivityManager.RunningAppProcessInfo appProcessInfo =
                new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == IMPORTANCE_FOREGROUND
                || appProcessInfo.importance == IMPORTANCE_VISIBLE);
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in app_bg_theme, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        user_id = sharedPrefHelper.getString("user_id", "");

        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            //get doctor not available
            String doctorNotAvailableToday=message.substring(message.lastIndexOf("=")+1);
            sharedPrefHelper.setString("doctorNotAvailable", doctorNotAvailableToday);
            //get last last word of the string.
            String lastWord = message.substring(message.lastIndexOf("-")+1);
            //String newLastWord=lastWord.substring(0, lastWord.length() - 1);
            sharedPrefHelper.setString("title1",title);
            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);

            Log.e("00123", "bhvhbv " + data.toString());
            if(sharedPrefHelper.getString("title1","").equals("'Logout'")){
                sharedPrefHelper.setString("is_login", "");
            }

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                Log.e("0123", "bhvhbv " + data.toString());

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                download_Commonfile("patient_appointments");
                download_doctoravil("doctor_available",doctorNotAvailableToday);
                //downloadAppointmentMedicinePrescribed("appointment_medicine_prescribed", lastWord);

                Log.e("123", "bhvhbv " + data.toString());
                Intent resultIntent = new Intent(getApplicationContext(), Splash_Screen.class);
                sharedPrefHelper.setString("isSplashLoaded", "No");
                sharedPrefHelper.setString("isNotification", "Yes");
                resultIntent.putExtra("message", message);

                // check for image attachmentr
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            } else {
                download_Commonfile("patient_appointments");
                download_doctoravil("doctor_available", doctorNotAvailableToday);
                downloadAppointmentMedicinePrescribed("appointment_medicine_prescribed", lastWord);

                // app is in app_bg_theme, show the notification in notification tray
                Log.e("fggghhh", "bhvhbv " + data.toString());
                Intent resultIntent = new Intent(getApplicationContext(), Splash_Screen.class);
                sharedPrefHelper.setString("isSplashLoaded", "No");
                sharedPrefHelper.setString("isNotification", "Yes");
//                splashLoaded = sharedPrefHelper.getString("isSplashLoaded", "  ");
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void downloadAppointmentMedicinePrescribed(String tablename, String patient_appointment_id) {
        AppointmentMedicinePrescribedModel appointmentMedicinePrescribedModel=new AppointmentMedicinePrescribedModel();
        appointmentMedicinePrescribedModel.setTable_name(tablename);
        appointmentMedicinePrescribedModel.setPatient_appointment_id(patient_appointment_id);

        Gson gson = new Gson();
        String data = gson.toJson(appointmentMedicinePrescribedModel);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        APIClient.getClient().create(TELEMEDICINE_API.class).downloadAppointmentMedicinePrescription(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            JsonObject singledataP = response.body();
                            JsonArray data = singledataP.getAsJsonArray("tableData");
                            if (data.size() > 0) {
                                sqliteHelper.dropTableWhere(tablename,"patient_appointment_id",patient_appointment_id);
                                for (int i = 0; i < data.size(); i++) {
                                    JSONObject singledata = new JSONObject(data.get(i).toString());

                                    Iterator keys = singledata.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                        contentValues.put("flag", "1");
                                    }
                                    sqliteHelper.saveMasterTable(contentValues, tablename);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.toString());
            }
        });
    }

    public void download_doctoravil(String table_name, String doctorNotAvailableToday) {
//        mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
        pharmacyPatientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
        pharmacyPatientModel.setRole_id(sharedPrefHelper.getString("role_id", ""));
        pharmacyPatientModel.setDoctor_id(doctorNotAvailableToday);
        pharmacyPatientModel.setTable_name(table_name);

//        //pharmacyPatientModel.setMobile(searchInput);
        Gson mGson = new Gson();
        String data = mGson.toJson(pharmacyPatientModel);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        APIClient.getClient().create(TELEMEDICINE_API.class).download_Data(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
//                        mProgressDialog.dismiss();
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            //sqliteHelper.dropTable(table_name);
                            JsonObject singledataP = response.body();
                            JsonArray data = singledataP.getAsJsonArray("tableData");
                            if (data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    JSONObject singledata = new JSONObject(data.get(i).toString());
                                    Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());

                                    Iterator keys = singledata.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                        contentValues.put("flag", "1");
                                    }
                                    sqliteHelper.saveMasterTable(contentValues, table_name);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressDialog.dismiss();
            }
        });
    }

    public void download_Commonfile(String table_name) {
        //mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
        pharmacyPatientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
        pharmacyPatientModel.setRole_id(sharedPrefHelper.getString("role_id", ""));
        pharmacyPatientModel.setTable_name(table_name);

        //pharmacyPatientModel.setMobile(searchInput);
        Gson mGson = new Gson();
        String data = mGson.toJson(pharmacyPatientModel);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        APIClient.getClient().create(TELEMEDICINE_API.class).download_Data(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        //mProgressDialog.dismiss();
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            sqliteHelper.dropTable(table_name);
                            JsonObject singledataP = response.body();
                            JsonArray data = singledataP.getAsJsonArray("tableData");
                            if (data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    JSONObject singledata = new JSONObject(data.get(i).toString());
                                    Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());

                                    Iterator keys = singledata.keys();
                                    ContentValues contentValues = new ContentValues();
                                    while (keys.hasNext()) {
                                        String currentDynamicKey = (String) keys.next();
                                        contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                        contentValues.put("flag", "1");
                                    }
                                    sqliteHelper.saveMasterTable(contentValues, table_name);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressDialog.dismiss();
            }
        });

    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
            onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
            onServiceDisconnected();
        }
    }

    protected void onServiceConnected() {
        // for subclasses
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }

    protected SinchService.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }

    @Override
    public void onIncomingCall(CallClient callClient, com.sinch.android.rtc.calling.Call call) {
        callID= call.getCallId();
    }
}


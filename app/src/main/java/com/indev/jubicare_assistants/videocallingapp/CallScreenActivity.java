package com.indev.jubicare_assistants.videocallingapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.projection.MediaProjectionManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hbisoft.hbrecorder.HBRecorder;
import com.hbisoft.hbrecorder.HBRecorderListener;
import com.iceteck.silicompressorr.SiliCompressor;
import com.indev.jubicare_assistants.R;
import com.indev.jubicare_assistants.SharedPrefHelper;
import com.indev.jubicare_assistants.model.IvrCallingMasking;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallState;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CallScreenActivity extends BaseActivity implements HBRecorderListener {

    static final String TAG = CallScreenActivity.class.getSimpleName();
    static final String ADDED_LISTENER = "addedListener";
    static final String VIEWS_TOGGLED = "viewsToggled";

    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;

    //Permissions
    private static final int SCREEN_RECORD_REQUEST_CODE = 777;
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = PERMISSION_REQ_ID_RECORD_AUDIO + 1;
    private boolean hasPermissions = false;

    //Declare HBRecorder
    private HBRecorder hbRecorder;

    //Start/Stop Button
    private ImageView startbtn;
    boolean wasHDSelected = false;
    boolean isAudioEnabled = true;


    private String mCallId;
    private boolean mAddedListener = false;
    private boolean mLocalVideoViewAdded = false;
    private boolean mRemoteVideoViewAdded = false;
    private BroadcastReceiver MyReceiver = null;
    private TextView mCallDuration;
    private TextView mCallState;
    private TextView mCallerName;
    boolean mToggleVideoViewPositions = false;
    private boolean mic=true;
    SharedPrefHelper sharedPrefHelper;
    String patient_name;
    String startTime;
    String masking_id;
    String call_status="Not Answered";
    IvrCallingMasking ivrCallingMasking;
    SqliteHelper sqliteHelper;
    ArrayList<IvrCallingMasking> masking = new ArrayList<>();
     String filePath = "";


    @Override
    public void HBRecorderOnStart() {

    }

    @Override
    public void HBRecorderOnComplete() {

    }

    @Override
    public void HBRecorderOnError(int errorCode, String reason) {

    }

    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            CallScreenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(ADDED_LISTENER, mAddedListener);
        savedInstanceState.putBoolean(VIEWS_TOGGLED, mToggleVideoViewPositions);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mAddedListener = savedInstanceState.getBoolean(ADDED_LISTENER);
        mToggleVideoViewPositions = savedInstanceState.getBoolean(VIEWS_TOGGLED);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callscreen);
        getSupportActionBar().hide();
        mAudioPlayer = new AudioPlayer(this);
        mCallDuration = findViewById(R.id.callDuration);
        mCallerName = findViewById(R.id.remoteUser);
        mCallState = findViewById(R.id.callState);
        startbtn = findViewById(R.id.start);
        Button endCallButton = findViewById(R.id.hangupButton);
        sharedPrefHelper =new SharedPrefHelper(this);
        ivrCallingMasking=new IvrCallingMasking();
        sqliteHelper=new SqliteHelper(this);
        MyReceiver=new MyReceiver();
        Bundle bundle= getIntent().getExtras();
        if(bundle!=null){
            patient_name=bundle.getString("patient_name","");
            startTime=bundle.getString("startTime","");
            masking_id=bundle.getString("masking_id","");
        }
        endCallButton.setOnClickListener(v ->
                endCall()

        );
        broadcastIntent();

        hbRecorder=new HBRecorder(this,this);
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);

        startbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //first check if permissions was granted
                    if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO) && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE)) {
                        hasPermissions = true;
                    }
                    if (hasPermissions) {
                        //check if recording is in progress
                        //and stop it if it is
                        if (hbRecorder.isBusyRecording()) {
                            hbRecorder.stopScreenRecording();
                            startbtn.setColorFilter(ContextCompat.getColor(CallScreenActivity.this, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);

                        }
                        //else start recording
                        else {
                            startRecordingScreen();
                        }
                    }
                } else {
                    showLongToast("This library requires API 21>");
                }
            }
        });
    }
    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }
    //Handle permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_ID_RECORD_AUDIO:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
                } else {
                    hasPermissions = false;
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                }
                break;
            case PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasPermissions = true;
                    //Permissions was provided
                    //Start screen recording
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startRecordingScreen();
                    }
                } else {
                    hasPermissions = false;
                    showLongToast("No permission for " + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            default:
                break;
        }
    }
    private void showLongToast(final String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startRecordingScreen() {
        quickSettings();
        MediaProjectionManager mediaProjectionManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        }
        Intent permissionIntent = mediaProjectionManager != null ? mediaProjectionManager.createScreenCaptureIntent() : null;
        startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    //Set file path or Uri depending on SDK version
                    setOutputPath();
                    //Start screen recording
                    hbRecorder.startScreenRecording(data, resultCode, this);

                }
            }
        }
    }
    ContentResolver resolver;
    ContentValues contentValues;
    Uri mUri;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setOutputPath() {
        String filename = generateFileName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            resolver = getContentResolver();
            contentValues = new ContentValues();
            contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/" + "HBRecorder");
            contentValues.put(MediaStore.Video.Media.TITLE, filename);
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
            mUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
            //FILE NAME SHOULD BE THE SAME
            hbRecorder.setFileName(filename);
            hbRecorder.setOutputUri(mUri);
            sharedPrefHelper.setString("Uri",mUri.toString());
        }else{
            createFolder();
            hbRecorder.setOutputPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) +"/HBRecorder");

        }
    }
    private void createFolder() {
        File f1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "HBRecorder");
        if (!f1.exists()) {
            if (f1.mkdirs()) {
                sharedPrefHelper.setString("FileName",f1.getPath());
                //Log.i("Folder ", "created");
            }
        }
    }

    //Generate a timestamp to be used as a file name
    private String generateFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate).replace(" ", "");
    }


    //drawable to byte[]
    private byte[] drawable2ByteArray(@DrawableRes int drawableId) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), drawableId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(MyReceiver);
//    }

    public void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    private void quickSettings() {
        hbRecorder.setAudioBitrate(128000);
        hbRecorder.setAudioSamplingRate(44100);
        hbRecorder.recordHDVideo(wasHDSelected);
        hbRecorder.enableCustomSettings();
        hbRecorder.isAudioEnabled(isAudioEnabled);
        hbRecorder.setVideoEncoder("DEFAULT");
        hbRecorder.setAudioSource("DEFAULT");
        hbRecorder.setVideoBitrate(450000);
        hbRecorder.setOutputFormat("DEFAULT");
        hbRecorder.setVideoFrameRate(24);
        hbRecorder.setScreenDimensions(426, 240);
        //Customise Notification
        hbRecorder.setNotificationSmallIcon((drawable2ByteArray(R.drawable.icon)));
        hbRecorder.setNotificationTitle("Recording your screen");
        hbRecorder.setNotificationDescription("Drag down to stop the recording");

    }


    @Override
    public void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            if (!mAddedListener) {
                call.addCallListener(new SinchCallListener());
                mAddedListener = true;
            }
        } else {
            //Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }

        updateUI();
    }

    private void updateUI() {
        if (getSinchServiceInterface() == null) {
            return; // early
        }

        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            String userID=call.getRemoteUserId();
            String[] is=userID.split("~");
            mCallerName.setText(is[0]);
            mCallState.setText(call.getState().toString());
            if (call.getDetails().isVideoOffered()) {
                if (call.getState() == CallState.ESTABLISHED) {
                    setVideoViewsVisibility(true, true);
                } else {
                    setVideoViewsVisibility(true, false);
                }
            }
        } else {
            setVideoViewsVisibility(false, false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mDurationTask.cancel();
        mTimer.cancel();
        removeVideoViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
        updateUI();
    }

    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
    }

    private void endCall() {
        mAudioPlayer.stopProgressTone();
        sharedPrefHelper.setString("FileName",hbRecorder.getFilePath());
        //Log.e("FileName",hbRecorder.getFilePath());
        ivrCallingMasking.setIvr_call_masking_id(masking_id);
        ivrCallingMasking.setPatient_name(patient_name);
        ivrCallingMasking.setUid(hbRecorder.getFilePath());
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        ivrCallingMasking.setEnd_time(timeStamp);
        ivrCallingMasking.setStart_time(startTime);
        ivrCallingMasking.setTime_duration(mCallDuration.getText().toString());
        ivrCallingMasking.setCall_status(call_status);
        //sqliteHelper.updateVideoData(ivrCallingMasking,masking_id);

        if (hbRecorder.isBusyRecording()) {
            hbRecorder.stopScreenRecording();

        }


        Call call = getSinchServiceInterface().getCall(mCallId);

        if (call != null) {
            call.hangup();
          //  compressAndSyncVideo();

        }
        //start open to telemedicine application
        if (sharedPrefHelper.getString("assistance_application_login_by_doctor", "").equals("assistance_application_login_by_doctor")) {
            /*Intent callScreen = new Intent(Intent.ACTION_MAIN);
            callScreen.setClassName("com.indev.telemedicine", "com.indev.telemedicine.acitivities.doctor.DoctorHome");
            callScreen.putExtra("ivr_call_masking_id", masking_id);
            callScreen.putExtra("patient_name", patient_name);
            callScreen.putExtra("uid", hbRecorder.getFilePath());
            callScreen.putExtra("end_time", timeStamp);
            callScreen.putExtra("start_time", startTime);
            callScreen.putExtra("time_duration", mCallDuration.getText().toString());
            callScreen.putExtra("call_status", call_status);

            callScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(callScreen);*/
            sharedPrefHelper.setString("ivr_call_masking_id", masking_id);
            sharedPrefHelper.setString("patient_name", patient_name);
            sharedPrefHelper.setString("uid", hbRecorder.getFilePath());
            sharedPrefHelper.setString("end_time", timeStamp);
            sharedPrefHelper.setString("start_time", startTime);
            sharedPrefHelper.setString("time_duration", mCallDuration.getText().toString());
            sharedPrefHelper.setString("call_status", call_status);
        }
        finish();
    }

    private void compressAndSyncVideo() {
        masking = sqliteHelper.getVideoDataForSync();
        if (masking.size() > 0) {

            new AsyncTask<String, String, String>() {

                @Override
                protected String doInBackground(String... strings) {

                    try {
                        for (int i = 0; i < masking.size(); i++) {
                            if (filePath!=null) {
                                filePath = SiliCompressor.with(getApplicationContext()).compressVideo(masking.get(i).getVideo_file(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/HBRecorder", 240, 426, 450000);
                                if (filePath != null) {
                                    File file = new File(masking.get(i).getVideo_file());
                                    file.delete();
                                    sqliteHelper.updateVideoPathInTable("ivr_calling_masking", "id", Integer.parseInt(masking.get(i).getId()), filePath);

                                } else {
                                    Toast.makeText(CallScreenActivity.this, "somthing went wrong with compression", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return filePath;


                }
            }.execute();
        }else {

        }
    }


    private String formatTimespan(int totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    private void updateCallDuration() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            mCallDuration.setText(formatTimespan(call.getDetails().getDuration()));
        }
    }

    private ViewGroup getVideoView(boolean localView) {
        if (mToggleVideoViewPositions) {
            localView = !localView;
        }
        return localView ? findViewById(R.id.localVideo) : findViewById(R.id.remoteVideo);
    }

    private void addLocalView() {
        if (mLocalVideoViewAdded || getSinchServiceInterface() == null) {
            return; //early
        }
        final VideoController vc = getSinchServiceInterface().getVideoController();
        if (vc != null) {
            runOnUiThread(() -> {
                ViewGroup localView = getVideoView(true);
                localView.addView(vc.getLocalView());
                localView.setOnClickListener(v -> vc.toggleCaptureDevicePosition());
                mLocalVideoViewAdded = true;
                vc.setLocalVideoZOrder(!mToggleVideoViewPositions);
            });
        }
    }
    private void addRemoteView() {
        if (mRemoteVideoViewAdded || getSinchServiceInterface() == null) {
            return; //early
        }
        final VideoController vc = getSinchServiceInterface().getVideoController();
        if (vc != null) {
            runOnUiThread(() -> {
                ViewGroup remoteView = getVideoView(false);
                remoteView.addView(vc.getRemoteView());
                remoteView.setOnClickListener((View v) -> {

                    removeVideoViews();
                    mToggleVideoViewPositions = !mToggleVideoViewPositions;
                    addRemoteView();
                    addLocalView();
                });
                mRemoteVideoViewAdded = true;
                vc.setLocalVideoZOrder(!mToggleVideoViewPositions);
            });
        }
    }


    private void removeVideoViews() {
        if (getSinchServiceInterface() == null) {
            return; // early
        }

        VideoController vc = getSinchServiceInterface().getVideoController();
        if (vc != null) {
            runOnUiThread(() -> {
                ((ViewGroup)(vc.getRemoteView().getParent())).removeView(vc.getRemoteView());
                ((ViewGroup)(vc.getLocalView().getParent())).removeView(vc.getLocalView());
                mLocalVideoViewAdded = false;
                mRemoteVideoViewAdded = false;
            });
        }
    }

    private void setVideoViewsVisibility(final boolean localVideoVisibile, final boolean remoteVideoVisible) {
        if (getSinchServiceInterface() == null)
            return;
        if (mRemoteVideoViewAdded == false) {
            addRemoteView();
        }
        if (mLocalVideoViewAdded == false) {
            addLocalView();
        }
        final VideoController vc = getSinchServiceInterface().getVideoController();
        if (vc != null) {
            ImageView iv_switch_camera=findViewById(R.id.iv_switch_camera);
            iv_switch_camera.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    vc.toggleCaptureDevicePosition();
                }
            });
            runOnUiThread(() -> {
                vc.getLocalView().setVisibility(localVideoVisibile ? View.VISIBLE : View.GONE);
                vc.getRemoteView().setVisibility(remoteVideoVisible ? View.VISIBLE : View.GONE);
            });
        }
    }

    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String endMsg = "Call ended: " + call.getDetails().toString();
            Log.d(TAG, "Call ended. Reason: " + call.getDetails().toString());
            if (!cause.toString().equals("HUNG_UP")){
                // Toast.makeText(CallScreenActivity.this, "Make sure you are trying with valid id and good internet connectivity", Toast.LENGTH_LONG).show();
                Toast.makeText(CallScreenActivity.this, "Please try again.."+cause, Toast.LENGTH_LONG).show();

            }

            endCall();
        }


        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            call_status="Answered";
            mAudioPlayer.stopProgressTone();
            mCallState.setText(call.getState().toString());
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            ImageView iv_mute_unmute=findViewById(R.id.iv_mute_unmute);
            AudioController audioController = getSinchServiceInterface().getAudioController();
            //audioController.enableSpeaker();
            audioController.enableAutomaticAudioRouting(true, AudioController.UseSpeakerphone.SPEAKERPHONE_AUTO);            if (call.getDetails().isVideoOffered()) {
                setVideoViewsVisibility(true, true);
            }
            iv_mute_unmute.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mic){
                        iv_mute_unmute.setImageResource(R.drawable.ic_mic_off_black_24dp);
                        audioController.mute();
                        mic=false;
                    }else {
                        iv_mute_unmute.setImageResource(R.drawable.ic_mic_black_24dp);
                        audioController.unmute();
                        mic=true;
                    }

                }
            });

            Log.d(TAG, "Call offered video: " + call.getDetails().isVideoOffered());
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
            mAudioPlayer.playProgressTone();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }

        @Override
        public void onVideoTrackAdded(Call call) {
            addLocalView();
            addRemoteView();
        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }
    }

    //when you got that error =>are you missing a call to unregisterReceiver()
    //pls unregisterReceiver() your receiver
    @Override
    public void onDestroy() {
        try{
            if(MyReceiver!=null)
                unregisterReceiver(MyReceiver);
        }catch(Exception e){}

        super.onDestroy();
    }
}
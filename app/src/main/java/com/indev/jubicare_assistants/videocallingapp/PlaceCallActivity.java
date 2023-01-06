package com.indev.jubicare_assistants.videocallingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.indev.jubicare_assistants.R;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.UserController;
import com.sinch.android.rtc.calling.Call;


public class PlaceCallActivity extends BaseActivity {

    private Button mCallButton;
    private EditText mCallName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCallName = (EditText) findViewById(R.id.callName);
        mCallButton = (Button) findViewById(R.id.callButton);
        mCallButton.setEnabled(false);
        mCallButton.setOnClickListener(buttonClickListener);


    }

    @Override
    protected void onServiceConnected() {
        TextView userName = (TextView) findViewById(R.id.loggedInName);
        userName.setText(getSinchServiceInterface().getUsername());
        mCallButton.setEnabled(true);
    }

    private void logoutButtonClicked() {
        if (getSinchServiceInterface() != null) {
            UserController uc = Sinch.getUserControllerBuilder()
                    .context(getApplicationContext())
                    .applicationKey(SinchService.APP_KEY)
                    .userId(getSinchServiceInterface().getUsername())
                    .environmentHost(SinchService.ENVIRONMENT)
                    .build();
            uc.unregisterPushToken();
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    private void callButtonClicked() {
        String userName = mCallName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUser(userName);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }

    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.callButton:
                    callButtonClicked();
                    break;


            }
        }
    };
}

package com.indev.jubicare_assistants.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;


/**
 * Created by ARUN on 7/08/2021.
 */

public class MyJobService extends JobIntentService {
    public static final int JOB_ID = 101;

    private Context context = this;
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MyJobService.class, JOB_ID, work);
    }
    public MyJobService() {
        super();

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.e("start","started");


    }
}
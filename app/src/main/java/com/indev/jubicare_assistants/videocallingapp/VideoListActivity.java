package com.indev.jubicare_assistants.videocallingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.R;
import com.indev.jubicare_assistants.adapter.VideoListAdapter;
import com.indev.jubicare_assistants.model.IvrCallingMasking;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoListActivity extends AppCompatActivity {

    @BindView(R.id.tvNoDataFound)
    TextView tvNoDataFound;
    @BindView(R.id.btn_go_home)
    TextView btn_go_home;
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.tv_syncAll)
    TextView tv_syncAll;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.rv_video_list)
    RecyclerView rv_video_list;
    VideoListAdapter videoListAdapter;
    SqliteHelper sqliteHelper;
    ArrayList<IvrCallingMasking> masking=new ArrayList<>();
    ProgressDialog mProgressDialog;
    Context context=this;
    String filePath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);
        setTitle("Video List");
        sqliteHelper=new SqliteHelper(this);
        mProgressDialog=new ProgressDialog(this);
        masking = sqliteHelper.getVideoDataForSync();
        if (masking.size()>0){
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            videoListAdapter = new VideoListAdapter(masking,this);
            rv_video_list.setLayoutManager(mLayoutManager);
            rv_video_list.setAdapter(videoListAdapter);
            videoListAdapter.notifyDataSetChanged();
        }else{
            tvNoDataFound.setVisibility(View.VISIBLE);
            btn_go_home.setVisibility(View.VISIBLE);
        }

        btn_go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intentDoctor=new Intent(VideoListActivity.this, DoctorHome.class);
                intentDoctor.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentDoctor);
                finish();*/

            }
        });

        tv_syncAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compressAndSyncVideo();
            }
        });



    }
    private void compressAndSyncVideo() {
        masking = sqliteHelper.getVideoDataForSync();
        if (masking.size() > 0) {
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);
            progress.setMax(100);
            new AsyncTask<String, String, String>() {

                @Override
                protected String doInBackground(String... strings) {

                    filePath=updloadVideo();

                    return filePath;


                }
            }.execute();
        }else {
            Toast.makeText(context, "No Videos For Sync", Toast.LENGTH_SHORT).show();

        }
    }

    private String updloadVideo() {
        try {
            progress.setProgress(5);
            progress.setProgress(10);
            for (int i = 0; i < masking.size(); i++) {
              //  filePath = SiliCompressor.with(context).compressVideo(masking.get(i).getVideo_file(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/HBRecorder");
               // if (filePath != null) {
                    Uri imageUri = Uri.parse(masking.get(i).getVideo_file());
                    MultipartBody.Part part = null;
                    File file = new File(imageUri.getPath());
                    RequestBody fileReqBody = RequestBody.create(MediaType.parse("Image/*"), file);
                    part = MultipartBody.Part.createFormData("video_file", file.getName(), fileReqBody);
                    progress.setProgress(40);

                    int finalI = i;
                    /*APIClient.getClient().create(TELEMEDICINE_API.class).video_callingFiles(masking.get(i).getIvr_call_masking_id(), masking.get(i).getPatient_name(), masking.get(i).getCall_status(), masking.get(i).getStart_time(), masking.get(i).getEnd_time(), masking.get(i).getTime_duration(), part).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                Log.e("upload files sxx", "onResponse- " + jsonObject.toString());
                                String message = jsonObject.optString("success");
                                if (message.equalsIgnoreCase("1")) {
                                    progress.setProgress(70);
                                    progress.setProgress(80);
                                    sqliteHelper.updateFlagInTable("ivr_calling_masking", "id", Integer.parseInt(masking.get(finalI).getId()), 1);
                                    if (masking.size() == finalI + 1) {
                                        File file = new File(masking.get(finalI).getVideo_file());
                                        file.delete();
                                      //  File file1 = new File(filePath);
                                       // file1.delete();
                                        progress.setProgress(100);
                                        progress.setVisibility(View.GONE);
                                        tv_progress.setVisibility(View.GONE);
                                        Intent intent=new Intent(VideoListActivity.this,DoctorHome.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(context, "Videos sync successfully", Toast.LENGTH_SHORT).show();
                                     //   mProgressDialog.dismiss();
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.GONE);
                            tv_progress.setVisibility(View.GONE);
                           // mProgressDialog.dismiss();
                        }
                    });*/

              //  } else {
                   // mProgressDialog.show();
              //  }

            }

        //} catch (URISyntaxException e) {
          //  e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }


}

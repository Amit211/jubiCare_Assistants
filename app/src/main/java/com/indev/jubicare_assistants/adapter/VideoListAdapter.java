package com.indev.jubicare_assistants.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.R;
import com.indev.jubicare_assistants.model.IvrCallingMasking;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    ArrayList<IvrCallingMasking> masking;
    int a=0;
    SqliteHelper sqliteHelper;
    String filePath="";
    public VideoListAdapter(ArrayList<IvrCallingMasking> masking, Context context) {
        this.masking = masking;
        this.context = context;
        sqliteHelper=new SqliteHelper(context);
    }

    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_list_custom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name=masking.get(position).getVideo_file();
        holder.tv_video_name.setText(name);

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compressAndSyncVideo(holder,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return masking.size();
    }

    private void compressAndSyncVideo(ViewHolder holder, int position) {

        holder.sync.setVisibility(View.GONE);
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.progressBar.setProgress(0);
        holder.progressBar.setMax(100);
        new AsyncTask<String, String, String>() {


            @Override
                protected String doInBackground(String... strings) {
                filePath= uploadVideo(holder,position);

                 return filePath;

                }
            }.execute();

    }

    private String uploadVideo(ViewHolder holder, int position) {

        try {
            holder.progressBar.setProgress(5);
            //filePath = SiliCompressor.with(context).compressVideo(masking.get(position).getVideo_file(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/HBRecorder");
           // if (filePath != null) {
                Uri imageUri = Uri.parse(masking.get(position).getVideo_file());
                MultipartBody.Part part = null;
                File file = new File(imageUri.getPath());
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("Image/*"), file);
                part = MultipartBody.Part.createFormData("video_file", file.getName(), fileReqBody);
                holder.progressBar.setProgress(23);
                holder.progressBar.setProgress(45);
                /*APIClient.getClient().create(TELEMEDICINE_API.class).video_callingFiles(masking.get(position).getIvr_call_masking_id(), masking.get(position).getPatient_name(), masking.get(position).getCall_status(), masking.get(position).getStart_time(), masking.get(position).getEnd_time(), masking.get(position).getTime_duration(), part).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            Log.e("upload files sxx", "onResponse- " + jsonObject.toString());
                            String message = jsonObject.optString("success");
                            if (message.equalsIgnoreCase("1")) {
                                holder.progressBar.setProgress(68);
                                holder.progressBar.setProgress(89);
                                sqliteHelper.updateFlagInTable("ivr_calling_masking", "id", Integer.parseInt(masking.get(position).getId()), 1);
                                File file = new File(masking.get(position).getVideo_file());
                                file.delete();
                             //   File file1 = new File(filePath);
                               // file1.delete();
                                Toast.makeText(context, "Videos sync successfully", Toast.LENGTH_SHORT).show();
                                holder.progressBar.setVisibility(View.GONE);
                                holder.sync.setVisibility(View.VISIBLE);
                                holder.sync.setImageResource(R.drawable.ic_check_circle_black_24dp);
                                holder.ll_main.setClickable(false);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        holder.progressBar.setVisibility(View.GONE);
                    }
                });*/

           // } else {
            //}



       // } catch (URISyntaxException e) {
         ///   e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_video_name;
        CircleImageView sync;
        LinearLayout ll_main;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_video_name= itemView.findViewById(R.id.tv_video_name);
            sync= itemView.findViewById(R.id.sync);
            progressBar= itemView.findViewById(R.id.progress);
            ll_main= itemView.findViewById(R.id.ll_main);
        }
    }
}

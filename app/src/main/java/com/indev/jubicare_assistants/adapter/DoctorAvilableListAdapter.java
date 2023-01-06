package com.indev.jubicare_assistants.adapter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indev.jubicare_assistants.R;
import com.indev.jubicare_assistants.SharedPrefHelper;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorAvilableListAdapter extends RecyclerView.Adapter<DoctorAvilableListAdapter.ViewHolder> {
    List<ContentValues> listModels;
    private Context context;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    String video_image = "";
    android.app.Dialog popUp;
    TextView tv_description_name, tv_module_name, btn_module;
    ImageView iv_img;
    String nextVideo = "";
    int currentVideoPlay = 0;
    String languageid;
    String language_id;

    public DoctorAvilableListAdapter(List<ContentValues> listModels, Context context) {
        //this.courseList = courseList;
        popUp = new Dialog(context);

        this.listModels = listModels;
        this.context = context;
        sqliteHelper = new SqliteHelper(context);
        sharedPrefHelper = new SharedPrefHelper(context);
    }

    @NonNull
    @Override
    public DoctorAvilableListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_doctor_list, parent, false);
        DoctorAvilableListAdapter.ViewHolder courseViewHolder = new DoctorAvilableListAdapter.ViewHolder(view);
        return courseViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAvilableListAdapter.ViewHolder holder, int position) {
        holder.tv_counsellor_name.setText(new StringBuilder().append(listModels.get(position).get("full_name").toString()));
      //  holder.tv_counsellor_number.setText(new StringBuilder().append(listModels.get(position).get("contact_no").toString()));
        holder.tv_district.setText(new StringBuilder().append(listModels.get(position).get("district_name").toString()));

    }



    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_counsellor_name)
        TextView tv_counsellor_name;
        @BindView(R.id.tv_counsellor_number)

        TextView tv_district;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
package com.indev.jubicare_assistants.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.indev.jubicare_assistants.AppointmentInput;
import com.indev.jubicare_assistants.PatientPojo;
import com.indev.jubicare_assistants.R;
import com.indev.jubicare_assistants.SharedPrefHelper;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.ViewHolder> {

    private Context context;
    private List<AppointmentInput> listModels;
    ClickListener clickListener;

    SharedPrefHelper sharedPrefHelper;
    SqliteHelper sqliteHelper;
    String district_name = "";
    String block_name = "";
    String village_name = "";
    String stste_name = "";
    String flag = "";
    public AppointmentListAdapter(Context context, List<AppointmentInput> listModels) {
        this.context = context;
        this.listModels = listModels;
        sharedPrefHelper = new SharedPrefHelper(context);
        sqliteHelper=new SqliteHelper(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_dateAppointment.setText(listModels.get(position).getAssigned_doctor_on());
    //    holder.tv_dateAppointment.setText(listModels.get(position).getAssigned_doctor_on());

//        stste_name = sqliteHelper.getstateName(listModels.get(position).getState_id(), "state");
//        district_name = sqliteHelper.getdistrictName(listModels.get(position).getDistrict_id(), "district");
//        block_name = sqliteHelper.getblockName(listModels.get(position).getBlock_id(), "block");
//        village_name = sqliteHelper.getvillageName(listModels.get(position).getVillage_id(), "village");
//




        holder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onListItemClick(position);
            }
        });

    }

    //convert time into days, month and years
    // to get the difference between two dates



    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_dateAppointment)
        TextView tv_dateAppointment;

        @BindView(R.id.cv_main)
          CardView cv_main;

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
}



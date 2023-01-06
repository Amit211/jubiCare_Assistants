package com.indev.jubicare_assistants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.indev.jubicare_assistants.adapter.AppointmentListAdapter;
import com.indev.jubicare_assistants.adapter.PatientListAdapter;
import com.indev.jubicare_assistants.model.PharmacyPatientModel;
import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointmentView extends AppCompatActivity {
    @BindView(R.id.tv_patient_symptoms)
    TextView tv_patient_symptoms;
    @BindView(R.id.tv_disease_patient)
    TextView tv_disease_patient;
    @BindView(R.id.tv_bplowerP)
    TextView tv_bplowerP;
    @BindView(R.id.tv_bpupperP)
    TextView tv_bpupperP;
    @BindView(R.id.tv_blood_oxygen_levelP)
    TextView tv_blood_oxygen_levelP;
    @BindView(R.id.tv_sugarP)
    TextView tv_sugarP;
    @BindView(R.id.tv_PulseP)
    TextView tv_PulseP;
    @BindView(R.id.tv_tempratureP)
    TextView tv_tempratureP;
    @BindView(R.id.tv_assigned_doctor1)
    TextView tv_assigned_doctor1;
    @BindView(R.id.tv_date_assigned_doctor)
    TextView tv_date_assigned_doctor;

    @BindView(R.id.tv_any_emergency)
    TextView tv_any_emergency;
    @BindView(R.id.tv_pharmacists_assigned)
    TextView tv_pharmacists_assigned;
    @BindView(R.id.tv_date_pharmacists_assigned)
    TextView tv_date_pharmacists_assigned;
    @BindView(R.id.tv_date_taking_any_prescription)
    TextView tv_date_taking_any_prescription;
    @BindView(R.id.tv_taking_any_prescription)
    TextView tv_taking_any_prescription;
    @BindView(R.id.tv_pharmacists_is_done_on)
    TextView tv_pharmacists_is_done_on;
    @BindView(R.id.tv_pharmacists_is_done)
    TextView tv_pharmacists_is_done;
    @BindView(R.id.tv_is_on_doctor_done)
    TextView tv_is_on_doctor_done;
    @BindView(R.id.tv_doctor_is_done)
    TextView tv_doctor_is_done;
    @BindView(R.id.tv_by_remarks_doctor)
    TextView tv_by_remarks_doctor;
    @BindView(R.id.tv_verified_is)
    TextView tv_verified_is;
SqliteHelper sqliteHelper;
SharedPrefHelper sharedPrefHelper;
AppointmentInput appointmentInput;
String profile_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);
        setTitle("View Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sqliteHelper=new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
        appointmentInput=new AppointmentInput();
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            profile_id = bundle.getString("profile_patient_id", "");
        }
//        appointmentInput=sqliteHelper.syncAppointmentOFlinData2(profile_id);
//        tv_patient_symptoms.setText(appointmentInput.getSymptom_name());
//        tv_disease_patient.setText(appointmentInput.getDisease_name());
//        tv_bplowerP.setText(appointmentInput.getBp_lower());
//        tv_bpupperP.setText(appointmentInput.getBp_upper());
//        tv_sugarP.setText(appointmentInput.getSugar());
//        tv_PulseP.setText(appointmentInput.getPulse());
//        tv_tempratureP.setText(appointmentInput.getTemperature());
//        tv_pharmacists_is_done_on.setText(appointmentInput.getIs_pharmacists_done());
//        tv_pharmacists_is_done.setText(appointmentInput.getIs_pharmacists_done_on());
//        tv_doctor_is_done.setText(appointmentInput.getIs_doctor_done());
//        tv_is_on_doctor_done.setText(appointmentInput.getIs_doctor_done());
//        tv_verified_is.setText(appointmentInput.getIs_verified());
//        tv_blood_oxygen_levelP.setText(appointmentInput.getBlood_oxygen_level());
//        tv_any_emergency.setText(appointmentInput.getIs_emergency());
//        tv_pharmacists_assigned.setText(appointmentInput.getAssigned_pharmacists());
//        tv_date_pharmacists_assigned.setText(appointmentInput.getAssigned_pharmacists_on());
//        tv_taking_any_prescription .setText(appointmentInput.getPrescribed_medicine());
//        tv_date_taking_any_prescription .setText(appointmentInput.getPrescribed_medicine_date());
//        tv_by_remarks_doctor .setText(appointmentInput.getRemarkrs_by_doctor());
//        tv_date_assigned_doctor.setText(appointmentInput.getAssigned_doctor_on());
//        try{
//            tv_assigned_doctor1.setText(appointmentInput.getAssigned_doctor());
//        }catch (RuntimeException e){
//            e.printStackTrace();
//        }
    }
}
package com.indev.jubicare_assistants.model;

public class IvrCallingMasking {


    public String getIvr_call_masking_id() {
        return ivr_call_masking_id;
    }

    public void setIvr_call_masking_id(String ivr_call_masking_id) {
        this.ivr_call_masking_id = ivr_call_masking_id;
    }

    private String ivr_call_masking_id;
    private String id;
    private String uid;
    private String caller_id;
    private String reciver_id;
    private String patient_appointment_id;

    public String getVideo_file() {
        return video_file;
    }

    public void setVideo_file(String video_file) {
        this.video_file = video_file;
    }

    private String video_file;

    public String getProfile_patient_id() {
        return profile_patient_id;
    }

    public void setProfile_patient_id(String profile_patient_id) {
        this.profile_patient_id = profile_patient_id;
    }

    private String profile_patient_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCaller_id() {
        return caller_id;
    }

    public void setCaller_id(String caller_id) {
        this.caller_id = caller_id;
    }

    public String getReciver_id() {
        return reciver_id;
    }

    public void setReciver_id(String reciver_id) {
        this.reciver_id = reciver_id;
    }

    public String getPatient_appointment_id() {
        return patient_appointment_id;
    }

    public void setPatient_appointment_id(String patient_appointment_id) {
        this.patient_appointment_id = patient_appointment_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getCall_status() {
        return call_status;
    }

    public void setCall_status(String call_status) {
        this.call_status = call_status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getTime_duration() {
        return time_duration;
    }

    public void setTime_duration(String time_duration) {
        this.time_duration = time_duration;
    }

    public String getCalling_type() {
        return calling_type;
    }

    public void setCalling_type(String calling_type) {
        this.calling_type = calling_type;
    }

    public String getCalling_screen() {
        return calling_screen;
    }

    public void setCalling_screen(String calling_screen) {
        this.calling_screen = calling_screen;
    }

    private String role_id;
    private String call_status;
    private String start_time;
    private String end_time;
    private String time_duration;
    private String calling_type;
    private String calling_screen;

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    private String patient_name;


    public static final String TABLE_NAME = "ivr_calling_masking";
    public static final String COLUMN_ivr_calling_masking_id = "ivr_calling_masking_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_uid = "uid";
    public static final String COLUMN_caller_id = "caller_id";
    public static final String COLUMN_patient_name = "patient_name";
    public static final String COLUMN_reciver_id = "reciver_id";
    public static final String COLUMN_patient_appointment_id = "patient_appointment_id";
    public static final String COLUMN_role_id = "role_id";
    public static final String COLUMN_call_status = "call_status";
    public static final String COLUMN_start_time = "start_time";
    public static final String COLUMN_end_time = "end_time";
    public static final String COLUMN_time_duration = "time_duration";
    public static final String COLUMN_calling_type = "calling_type";
    public static final String COLUMN_calling_screen = "calling_screen";
    public static final String COLUMN_created_at = "created_at";
    public static final String COLUMN_del_action = "del_action";
    public static final String COLUMN_FLAG = "flag";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ivr_calling_masking_id + " INTEGER ,"
                    + COLUMN_uid + " TEXT ,"
                    + COLUMN_caller_id + " TEXT ,"
                    + COLUMN_patient_name + " TEXT ,"
                    + COLUMN_reciver_id + " TEXT ,"
                    + COLUMN_patient_appointment_id + " TEXT ,"
                    + COLUMN_role_id + " TEXT ,"
                    + COLUMN_call_status + " TEXT ,"
                    + COLUMN_start_time + " TEXT ,"
                    + COLUMN_end_time + " TEXT ,"
                    + COLUMN_time_duration + " TEXT ,"
                    + COLUMN_calling_type + " TEXT ,"
                    + COLUMN_calling_screen + " TEXT ,"
                    + COLUMN_created_at + " TEXT ,"
                    + COLUMN_del_action + " TEXT ,"
                    + COLUMN_FLAG + "  INTEGER   "
                    + ")";


}

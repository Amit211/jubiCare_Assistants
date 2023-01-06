package com.indev.jubicare_assistants;

public class DoctorAvailableDatePojo {
    private String doctorNotAvailable;

    public String getDoctorNotAvailable() {
        return doctorNotAvailable;
    }

    public void setDoctorNotAvailable(String doctorNotAvailable) {
        this.doctorNotAvailable = doctorNotAvailable;
    }

    private String date_from;
    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }
    private String date_to;
    private String remark;

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    private String local_id;
    private String doctor_id="";

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public static final String TABLE_NAME = "doctor_available";
    public static final String COLUMN_LOCAL_ID = "local_id";
    public static final String COLUMN_DOCTOR_ID = "doctor_id";
    public static final String COLUMN_DATE_FROM  = "date_from";
    public static final String COLUMN_DATE_TO  = "date_to";
    public static final String COLUMN_REMARK = "remark";
    public static final String COLUMN_created_at = "created_at";
    public static final String COLUMN_del_action = "del_action";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FLAG = "flag";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ID + " INTEGER,"
                    + COLUMN_DOCTOR_ID + " INTEGER,"
                    + COLUMN_DATE_FROM + " TEXT ,"
                    + COLUMN_DATE_TO + " TEXT ,"
                    + COLUMN_REMARK + " TEXT ,"
                    + COLUMN_created_at + " TEXT ,"
                    + COLUMN_del_action + " TEXT ,"
                    + COLUMN_FLAG + "  INTEGER   "
                    + ")";



}


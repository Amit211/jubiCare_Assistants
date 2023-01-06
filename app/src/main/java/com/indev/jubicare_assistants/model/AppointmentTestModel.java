package com.indev.jubicare_assistants.model;

public class AppointmentTestModel {


    private  int id;
    private  int patient_appointment_id;
    private String created_on;
    private  int test_id;
    private  int test_value;
    private String is_verified;
    private  int sub_test_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatient_appointment_id() {
        return patient_appointment_id;
    }

    public void setPatient_appointment_id(int patient_appointment_id) {
        this.patient_appointment_id = patient_appointment_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
    }

    public int getTest_value() {
        return test_value;
    }

    public void setTest_value(int test_value) {
        this.test_value = test_value;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public int getSub_test_id() {
        return sub_test_id;
    }

    public void setSub_test_id(int sub_test_id) {
        this.sub_test_id = sub_test_id;
    }

    public static final String TABLE_NAME = "appointment_test";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PATIENT_APPOINTMENT_ID  = "patient_appointment_id";
    public static final String COLUMN_TEXT_ID = "test_id";
    public static final String COLUMN_SUB_TEST_ID = "sub_test_id";
    public static final String COLUMN_IS_VERIFIED = "is_verified";
    public static final String COLUMN_TEST_VALUE = "test_value";
    public static final String COLUMN_suggest_by = "suggest_by";
    public static final String COLUMN_CREATED_ON = "created_on";
    public static final String COLUMN_FLAG = "flag";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +COLUMN_PATIENT_APPOINTMENT_ID + " TEXT ,"
                    + COLUMN_TEXT_ID  + " INTEGER ,"
                    + COLUMN_SUB_TEST_ID + " INTEGER ,"
                    + COLUMN_IS_VERIFIED + " TEXT ,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_TEST_VALUE + " INTEGER ,"
                    + COLUMN_suggest_by + " INTEGER ,"
                    + COLUMN_FLAG + "  INTEGER   "
                    + ")";



}

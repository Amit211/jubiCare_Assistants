package com.indev.jubicare_assistants.model;

public class DiseaseModel {
    private  int id;
    private String disease_name;
    private String created_on;
    private String symptom_id;
    private String modified_on;
    private String is_verified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisease_name() {
        return disease_name;
    }

    public void setDisease_name(String disease_name) {
        this.disease_name = disease_name;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getSymptom_id() {
        return symptom_id;
    }

    public void setSymptom_id(String symptom_id) {
        this.symptom_id = symptom_id;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public static final String TABLE_NAME = "disease";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DISEASE_NAME  = "disease_name";
    public static final String COLUMN_SYMPTOM_ID = "symptom_id";
    public static final String COLUMN_MODIFIED_ON = "modified_on";
    public static final String COLUMN_IS_VERIFIED = "is_verified";
    public static final String COLUMN_CREATED_ON = "created_on";
    public static final String COLUMN_DEL_ACTION = "del_action";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DISEASE_NAME + " TEXT ,"
                    + COLUMN_SYMPTOM_ID + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_IS_VERIFIED + " TEXT ,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_DEL_ACTION + "  INTEGER "
                    + ")";

}

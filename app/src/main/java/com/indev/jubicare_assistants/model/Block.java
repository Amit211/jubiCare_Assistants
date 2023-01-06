package com.indev.jubicare_assistants.model;

public class Block {

    private  int id;
    private String name;
    private String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDel_action() {
        return del_action;
    }

    public void setDel_action(String del_action) {
        this.del_action = del_action;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    private String del_action;
    private String district_id;


    public static final String TABLE_NAME = "block";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME  = "name";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_DISTRICT_ID = "district_id";
    public static final String COLUMN_is_visible_patient = "is_visual_patient";
    public static final String COLUMN_CREATED_DATE = "created_at";
    public static final String COLUMN_FLAG = "flag";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_DISTRICT_ID + " TEXT ,"
                    + COLUMN_CREATED_DATE + " TEXT ,"
                    + COLUMN_is_visible_patient + " TEXT ,"
                    + COLUMN_FLAG + "  INTEGER   "
                    + ")";



}

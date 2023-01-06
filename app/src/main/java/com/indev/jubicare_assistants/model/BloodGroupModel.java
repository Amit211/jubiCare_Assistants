package com.indev.jubicare_assistants.model;

public class BloodGroupModel {


    private String id;
    private String name;
    private String del_action;


    public static final String TABLE_NAME = "blood_group";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_FLAG = "flag";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +COLUMN_NAME + " TEXT ,"
                    +COLUMN_DEL_ACTION + " TEXT ,"
                    +COLUMN_CREATED_AT + " TEXT ,"
                    + COLUMN_FLAG + "  INTEGER   "
                    + ")";



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDel_action() {
        return del_action;
    }

    public void setDel_action(String del_action) {
        this.del_action = del_action;
    }
}

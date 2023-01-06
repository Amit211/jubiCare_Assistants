package com.indev.jubicare_assistants.model;

public class Pharmacy {
    public static final String TABLE_NAME = "profile_pharmacists";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_CONTACT_NO = "contact_no";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_created_at = "created_at";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_ID  + " INTEGER,"
                    + COLUMN_FULL_NAME  + " TEXT,"
                    + COLUMN_CONTACT_NO  + " INTEGER,"
                    + COLUMN_created_at  + " INTEGER,"
                    + COLUMN_DEL_ACTION  + " TEXT "
                    + ")";

}

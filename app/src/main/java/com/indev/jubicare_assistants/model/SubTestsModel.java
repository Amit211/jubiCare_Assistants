package com.indev.jubicare_assistants.model;

public class SubTestsModel {
    private  int id;
    private String test_name;
    private String created_on;
    private String user_id;
    private String test_id;
    private String modified_on;
    private String is_verified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
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

    public static final String TABLE_NAME = "sub_tests";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TEST_NAME  = "test_name";
    public static final String COLUMN_TEST_ID  = "test_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_MODIFIED_ON = "modified_on";
    public static final String COLUMN_IS_VERIFIED = "is_verified";
    public static final String COLUMN_CREATED_ON = "created_on";
    public static final String COLUMN_DEL_ACTION = "del_action";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TEST_NAME + " TEXT ,"
                    + COLUMN_USER_ID  + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_IS_VERIFIED + " TEXT ,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_TEST_ID  + " INTEGER ,"
                    + COLUMN_DEL_ACTION + " INTEGER "
                    + ")";




}

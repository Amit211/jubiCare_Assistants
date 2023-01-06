package com.indev.jubicare_assistants.model;

public class Test {
    public static final String TABLE_NAME = "test";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TEST_NAME  = "test_name";
    public static final String COLUMN_IS_VERIFIED = "is_verified";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_MODIFIED_ON = "modified_on";
    public static final String COLUMN_CREATED_ON = "created_on";
    public static final String COLUMN_DEL_ACTION = "del_action";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TEST_NAME + " TEXT ,"
                    + COLUMN_IS_VERIFIED  + " TEXT ,"
                    + COLUMN_USER_ID + " INTEGER ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT "
                    + ")";

    /*setter and getter*/
    private int id;
    private String test_name;
    private String is_verified;
    private int user_id;
    private String modified_on;
    private String created_on;
    private String del_action;

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

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getDel_action() {
        return del_action;
    }

    public void setDel_action(String del_action) {
        this.del_action = del_action;
    }
}

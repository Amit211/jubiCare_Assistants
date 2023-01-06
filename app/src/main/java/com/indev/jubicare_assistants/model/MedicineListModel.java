package com.indev.jubicare_assistants.model;

public class MedicineListModel {

    private  int id;
    private String medicine_name;
    private String created_on;
    private String user_id;
    private String modified_on;
    private String is_verified;
    private String is_stock;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
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

    public String getIs_stock() {
        return is_stock;
    }

    public void setIs_stock(String is_stock) {
        this.is_stock = is_stock;
    }

    public static final String TABLE_NAME = "medicine_list";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MEDICINE_NAME  = "medicine_name";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_MODIFIED_ON = "modified_on";
    public static final String COLUMN_IS_VERIFIED = "is_verified";
    public static final String COLUMN_IS_STOCK = "is_stock";
    public static final String COLUMN_CREATED_ON = "created_on";
    public static final String COLUMN_duplicate_data = "duplicate_data";
    public static final String COLUMN_DEL_ACTION = "del_action";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_MEDICINE_NAME + " TEXT ,"
                    + COLUMN_USER_ID  + " TEXT ,"
                    + COLUMN_MODIFIED_ON + " TEXT ,"
                    + COLUMN_IS_VERIFIED + " TEXT ,"
                    + COLUMN_CREATED_ON + " TEXT ,"
                    + COLUMN_IS_STOCK + " TEXT ,"
                    + COLUMN_duplicate_data + " TEXT ,"
                    + COLUMN_DEL_ACTION + "  INTEGER   "
                    + ")";


}

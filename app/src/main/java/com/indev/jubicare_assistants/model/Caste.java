package com.indev.jubicare_assistants.model;

public class Caste {

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

    private  int id;
    private String name;
    private String created_at;

    public static final String TABLE_NAME = "caste";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME  = "name";
    public static final String COLUMN_DEL_ACTION = "del_action";
    public static final String COLUMN_CREATED_DATE = "created_at";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_CREATED_DATE + " TEXT "
                    + ")";


}

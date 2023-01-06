package com.indev.jubicare_assistants.model;

public class PrescriptionEatingSchedule {

    public static final String TABLE_NAME = "prescription_eating_schedule";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DISEASE_NAME  = "name";
    public static final String COLUMN_CREATED_ON = "created_at";
    public static final String COLUMN_DEL_ACTION = "del_action";




    private String id;

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

    private String name;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DISEASE_NAME + " TEXT ,"
                    + COLUMN_DEL_ACTION + " TEXT ,"
                    + COLUMN_CREATED_ON + " TEXT "
                    + ")";
}

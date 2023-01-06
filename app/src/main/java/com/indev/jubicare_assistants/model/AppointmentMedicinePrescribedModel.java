package com.indev.jubicare_assistants.model;

public class AppointmentMedicinePrescribedModel {
    private  int id;
    private  String patient_appointment_id="",table_name="";

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    private  int medicine_id;
    private  int prescription_days_id;
    private  int prescription_interval_id;
    private  int prescription_eating_schedule_id;
    private  String prescription_sos;

    private  int quantity_by_doctor;
    private  int quantity_by_chemist;
    private  String doctor_updated_on;
    private  String chemist_updated_on;
    private  int created_by;
    private  int pharmacist_id;

    public static final String COLUMN_LOCAL_ID = "local_id";
    public static final String COLUMN_ID = "id";
    public static final String TABLE_NAME = "appointment_medicine_prescribed";
    public static final String COLUMN_PATIENT_APPOINTMENT_ID  = "patient_appointment_id";
    public static final String COLUMN_MEDICINE_ID = "medicine_id";
    public static final String COLUMN_PRESCRIPTION_DAYS_ID = "prescription_days_id";
    public static final String COLUMN_PRESCRIPTION_INTERVAL_ID = "prescription_interval_id";
    public static final String COLUMN_PRESCRIPTION_EATING_SCHEDULE_ID = "prescription_eating_schedule_id";
    public static final String COLUMN_PRESCRIPTION_SOS = "prescription_sos";
    public static final String COLUMN_QUANTITY_BY_DOCTOR = "quantity_by_doctor";
    public static final String COLUMN_QUANTITY_BY_CHEMIST = "quantity_by_chemist";
    public static final String COLUMN_DOCTOR_UPDATED_ON = "doctor_updated_on";
    public static final String COLUMN_CHEMIST_UPDATED_ON = "chemist_updated_on";
    public static final String COLUMN_CREATED_BY = "created_by";
    public static final String COLUMN_PHARMACIST_ID = "pharmacist_id";
    public static final String COLUMN_FLAG = "flag";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatient_appointment_id() {
        return patient_appointment_id;
    }

    public void setPatient_appointment_id(String patient_appointment_id) {
        this.patient_appointment_id = patient_appointment_id;
    }

    public int getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(int medicine_id) {
        this.medicine_id = medicine_id;
    }

    public int getPrescription_days_id() {
        return prescription_days_id;
    }

    public void setPrescription_days_id(int prescription_days_id) {
        this.prescription_days_id = prescription_days_id;
    }

    public int getPrescription_interval_id() {
        return prescription_interval_id;
    }

    public void setPrescription_interval_id(int prescription_interval_id) {
        this.prescription_interval_id = prescription_interval_id;
    }

    public int getPrescription_eating_schedule_id() {
        return prescription_eating_schedule_id;
    }

    public void setPrescription_eating_schedule_id(int prescription_eating_schedule_id) {
        this.prescription_eating_schedule_id = prescription_eating_schedule_id;
    }

    public String getPrescription_sos() {
        return prescription_sos;
    }

    public void setPrescription_sos(String prescription_sos) {
        this.prescription_sos = prescription_sos;
    }

    public int getQuantity_by_doctor() {
        return quantity_by_doctor;
    }

    public void setQuantity_by_doctor(int quantity_by_doctor) {
        this.quantity_by_doctor = quantity_by_doctor;
    }

    public int getQuantity_by_chemist() {
        return quantity_by_chemist;
    }

    public void setQuantity_by_chemist(int quantity_by_chemist) {
        this.quantity_by_chemist = quantity_by_chemist;
    }

    public String getDoctor_updated_on() {
        return doctor_updated_on;
    }

    public void setDoctor_updated_on(String doctor_updated_on) {
        this.doctor_updated_on = doctor_updated_on;
    }

    public String getChemist_updated_on() {
        return chemist_updated_on;
    }

    public void setChemist_updated_on(String chemist_updated_on) {
        this.chemist_updated_on = chemist_updated_on;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public int getPharmacist_id() {
        return pharmacist_id;
    }

    public void setPharmacist_id(int pharmacist_id) {
        this.pharmacist_id = pharmacist_id;
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ID + " TEXT,"
                    + COLUMN_PATIENT_APPOINTMENT_ID + " TEXT ,"
                    + COLUMN_MEDICINE_ID  + " TEXT ,"
                    + COLUMN_PRESCRIPTION_DAYS_ID  + " TEXT ,"
                    + COLUMN_PRESCRIPTION_INTERVAL_ID  + " TEXT ,"
                    + COLUMN_PRESCRIPTION_EATING_SCHEDULE_ID  + " TEXT ,"
                    + COLUMN_PRESCRIPTION_SOS  + " TEXT ,"
                    + COLUMN_QUANTITY_BY_DOCTOR + " TEXT ,"
                    + COLUMN_QUANTITY_BY_CHEMIST + " TEXT ,"
                    + COLUMN_DOCTOR_UPDATED_ON + " TEXT ,"
                    + COLUMN_CHEMIST_UPDATED_ON + " TEXT ,"
                    + COLUMN_CREATED_BY + " TEXT ,"
                    + COLUMN_PHARMACIST_ID + " INTEGER ,"
                    + COLUMN_FLAG + "  INTEGER   "
                    + ")";
}

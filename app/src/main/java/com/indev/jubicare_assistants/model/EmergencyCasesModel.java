package com.indev.jubicare_assistants.model;

public class EmergencyCasesModel {
    private String patient_name;
    private String contact;
    private String block_name;
    private String district_name;
    private String state_name;
    private String village_name;
    private String full_name;
    private String gender;
    private String age;
    private String table_name;
    private String emergency_patient;
    private String is_emergency;
    private String role_id;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String mobile;

    public String getIs_emergency() {
        return is_emergency;
    }

    public void setIs_emergency(String is_emergency) {
        this.is_emergency = is_emergency;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getEmergency_patient() {
        return emergency_patient;
    }

    public void setEmergency_patient(String emergency_patient) {
        this.emergency_patient = emergency_patient;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String user_id;

    /*public PharmacyPatientModel(int image, String patient_name, String contact) {
        this.image = image;
        this.patient_name = patient_name;
        this.contact = contact;
    }
*/
  /*  public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
*/
    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }
}

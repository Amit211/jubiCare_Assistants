package com.indev.jubicare_assistants.model;

public class PendingCasesModel {
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
    private String is_doctor_done;
    private String role_id;
    private String type;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String mobile;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getIs_doctor_done() {
        return is_doctor_done;
    }

    public void setIs_doctor_done(String is_doctor_done) {
        this.is_doctor_done = is_doctor_done;
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

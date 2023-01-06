package com.indev.jubicare_assistants.model;

public class PharmacyPatientModel {
    //private int image;
    private String patient_name;
    private String user_id;
    private String appointment_id;
    private String role_id;
    private String contact;
    private String block_name;
    private String district_name;
    private String state_name;
    private String village_name;
    private String full_name;
    private String gender;
    private String age;
    private String table_name;

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getAvailability_date() {
        return availability_date;
    }

    public void setAvailability_date(String availability_date) {
        this.availability_date = availability_date;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    private String adhar;
    private String mobile;
    private String is_test_done;
    private String doctor_id;
    private String availability_date;
    private String availability;

    public String getSearch_by_name()
    {
        return search_by_name;
    }

    public void setSearch_by_name(String search_by_name) {
        this.search_by_name = search_by_name;
    }

    private String search_by_name;

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    private String contact_no;

    public String getIs_test_done() {
        return is_test_done;
    }

    public void setIs_test_done(String is_test_done) {
        this.is_test_done = is_test_done;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getIs_pharmacists_done() {
        return is_pharmacists_done;
    }

    public void setIs_pharmacists_done(String is_pharmacists_done) {
        this.is_pharmacists_done = is_pharmacists_done;
    }

    private String is_pharmacists_done;

    public String getIs_doctor_done() {
        return is_doctor_done;
    }

    public void setIs_doctor_done(String is_doctor_done) {
        this.is_doctor_done = is_doctor_done;
    }

    private String is_doctor_done;

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAppointment_ID() {
        return appointment_id;
    }

    public void setAppointment_ID(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getAdhar() {
        return adhar;
    }

    public void setAdhar(String adhar) {
        this.adhar = adhar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

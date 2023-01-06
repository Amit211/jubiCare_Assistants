package com.indev.jubicare_assistants;

public class PatientFilledDataModel {
    private String user_id;
    private String patient_contact_no;
    private String reciver_no;

    public String getCalling_type() {
        return calling_type;
    }

    public void setCalling_type(String calling_type) {
        this.calling_type = calling_type;
    }

    private String calling_type;

    public String getCalling_screen() {
        return calling_screen;
    }

    public void setCalling_screen(String calling_screen) {
        this.calling_screen = calling_screen;
    }

    private String calling_screen;


    public String getReciver_no() {
        return reciver_no;
    }

    public void setReciver_no(String reciver_no) {
        this.reciver_no = reciver_no;
    }

    public String getPatient_appointment_id() {
        return patient_appointment_id;
    }

    public void setPatient_appointment_id(String patient_appointment_id) {
        this.patient_appointment_id = patient_appointment_id;
    }

    private String patient_appointment_id;


    public String getProfile_patient_id() {
        return profile_patient_id;
    }

    public void setProfile_patient_id(String profile_patient_id) {
        this.profile_patient_id = profile_patient_id;
    }

    private String profile_patient_id;
    private String role_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getPatient_contact_no() {
        return patient_contact_no;
    }

    public void setPatient_contact_no(String patient_contact_no) {
        this.patient_contact_no = patient_contact_no;
    }
}

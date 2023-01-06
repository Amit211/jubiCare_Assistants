package com.indev.jubicare_assistants.model;

public class User
{
    private int user_id,role_id,status,patient_id,doctor_id,counsellor_id,pharmacist_id;
  private String success;
   private String user_name,user_password;

    public int getPharmacist_id() {
        return pharmacist_id;
    }

    public void setPharmacist_id(int pharmacist_id) {
        this.pharmacist_id = pharmacist_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getCounsellor_id() {
        return counsellor_id;
    }

    public void setCounsellor_id(int counsellor_id) {
        this.counsellor_id = counsellor_id;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}

package com.indev.jubicare_assistants;

public class CountInput {
    private String user_id;
    private String role_id;
    private String type;
    private String Doctor_addressed_appointment;
    private String Pending_appointment_for_doctor;
    private String medicine_Delivery;
    private String Pending_medicine_Delivery;
    private String Medicine_to_be_delivered;

    public String getMobile_token() {
        return mobile_token;
    }

    public void setMobile_token(String mobile_token) {
        this.mobile_token = mobile_token;
    }

    private String mobile_token;


    public String getMedicine_to_be_delivered() {
        return Medicine_to_be_delivered;
    }

    public void setMedicine_to_be_delivered(String medicine_to_be_delivered) {
        Medicine_to_be_delivered = medicine_to_be_delivered;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDoctor_addressed_appointment() {
        return Doctor_addressed_appointment;
    }

    public void setDoctor_addressed_appointment(String doctor_addressed_appointment) {
        Doctor_addressed_appointment = doctor_addressed_appointment;
    }

    public String getPending_appointment_for_doctor() {
        return Pending_appointment_for_doctor;
    }

    public void setPending_appointment_for_doctor(String pending_appointment_for_doctor) {
        Pending_appointment_for_doctor = pending_appointment_for_doctor;
    }

    public String getMedicine_Delivery() {
        return medicine_Delivery;
    }

    public void setMedicine_Delivery(String medicine_Delivery) {
        this.medicine_Delivery = medicine_Delivery;
    }

    public String getPending_medicine_Delivery() {
        return Pending_medicine_Delivery;
    }

    public void setPending_medicine_Delivery(String pending_medicine_Delivery) {
        Pending_medicine_Delivery = pending_medicine_Delivery;
    }
}

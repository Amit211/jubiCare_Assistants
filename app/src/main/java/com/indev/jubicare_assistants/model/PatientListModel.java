package com.indev.jubicare_assistants.model;

public class PatientListModel {
    private int image;
    private String patient_name;
    private String registration_date;

    public PatientListModel(int image, String patient_name, String registration_date) {
        this.image = image;
        this.patient_name = patient_name;
        this.registration_date = registration_date;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }
}

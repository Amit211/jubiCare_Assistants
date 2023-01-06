package com.indev.jubicare_assistants;

public class DoctorAssignmentInput {

    private String profile_patient_id;
    private String patient_appointment;
    private String assigned_doctor;
    private String assigned_by;
    private String symptom_id;
    private String call_id;
    private String type;
    private String is_emergency;

    public String getIs_emergency() {
        return is_emergency;
    }

    public void setIs_emergency(String is_emergency) {
        this.is_emergency = is_emergency;
    }

    public String getCounsellor_remarks() {
        return counsellor_remarks;
    }

    public void setCounsellor_remarks(String counsellor_remarks) {
        this.counsellor_remarks = counsellor_remarks;
    }

    private String counsellor_remarks;


    public String getCall_id() {
        return call_id;
    }

    public void setCall_id(String call_id) {
        this.call_id = call_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfile_patient_id() {
        return profile_patient_id;
    }

    public void setProfile_patient_id(String profile_patient_id) {
        this.profile_patient_id = profile_patient_id;
    }

    public String getAssigned_by() {
        return assigned_by;
    }

    public void setAssigned_by(String assigned_by) {
        this.assigned_by = assigned_by;
    }

    public String getPatient_appointment() {
        return patient_appointment;
    }

    public void setPatient_appointment(String patient_appointment) {
        this.patient_appointment = patient_appointment;
    }

    public String getAssigned_doctor() {
        return assigned_doctor;
    }

    public void setAssigned_doctor(String assigned_doctor) {
        this.assigned_doctor = assigned_doctor;
    }

    public String getSymptom_id() {
        return symptom_id;
    }

    public void setSymptom_id(String symptom_id) {
        this.symptom_id = symptom_id;
    }

    public String getDisease_id() {
        return disease_id;
    }

    public void setDisease_id(String disease_id) {
        this.disease_id = disease_id;
    }

    private String disease_id;
}

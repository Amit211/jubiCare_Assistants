package com.indev.jubicare_assistants;

public class AppointmentInput {
    private String profile_patient_id;
    private String prescribed_medicine;
    private String user_id;
    private String prescribed_medicine_date;
    private String is_emergency;
    private String height;
    private String weight;
    private String appointment_file;
    private String blood_group_id;

    public String getHemoglobin() {
        return hemoglobin;
    }

    public void setHemoglobin(String hemoglobin) {
        this.hemoglobin = hemoglobin;
    }

    private String remarks;
    private String bp_high;
    private String bp_low;
    private String hemoglobin;

    public String getSymptom_name() {
        return symptom_name;
    }

    public void setSymptom_name(String symptom_name) {
        this.symptom_name = symptom_name;
    }

    public String getDisease_name() {
        return disease_name;
    }

    public void setDisease_name(String disease_name) {
        this.disease_name = disease_name;
    }

    private String bp_lower;
    private String bp_upper;
    private String sugar;
    private String temperature;
    private String symptom_name;
    private String disease_name;
    private String local_id;
    private String blood_oxygen_level;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private String pulse;
    private String counsellor_remarks;
    private String appointment_type;
    private String flag;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTest_suggested() {
        return test_suggested;
    }

    public void setTest_suggested(String test_suggested) {
        this.test_suggested = test_suggested;
    }

    public String getIs_test_done() {
        return is_test_done;
    }

    public void setIs_test_done(String is_test_done) {
        this.is_test_done = is_test_done;
    }

    public String getTest_verified_by_doctor() {
        return test_verified_by_doctor;
    }

    public void setTest_verified_by_doctor(String test_verified_by_doctor) {
        this.test_verified_by_doctor = test_verified_by_doctor;
    }

    public String getIs_test_on() {
        return is_test_on;
    }

    public void setIs_test_on(String is_test_on) {
        this.is_test_on = is_test_on;
    }

    public String getTreatment_doc() {
        return treatment_doc;
    }

    public void setTreatment_doc(String treatment_doc) {
        this.treatment_doc = treatment_doc;
    }

    public int getAssigned_doctor() {
        return assigned_doctor;
    }

    public void setAssigned_doctor(int assigned_doctor) {
        this.assigned_doctor = assigned_doctor;
    }

    public String getAssigned_doctor_on() {
        return assigned_doctor_on;
    }

    public void setAssigned_doctor_on(String assigned_doctor_on) {
        this.assigned_doctor_on = assigned_doctor_on;
    }

    public String getAssigned_pharmacists() {
        return assigned_pharmacists;
    }

    public void setAssigned_pharmacists(String assigned_pharmacists) {
        this.assigned_pharmacists = assigned_pharmacists;
    }

    public String getAssigned_pharmacists_on() {
        return assigned_pharmacists_on;
    }

    public void setAssigned_pharmacists_on(String assigned_pharmacists_on) {
        this.assigned_pharmacists_on = assigned_pharmacists_on;
    }

    public String getMedicine_delivered_on() {
        return medicine_delivered_on;
    }

    public void setMedicine_delivered_on(String medicine_delivered_on) {
        this.medicine_delivered_on = medicine_delivered_on;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getAssigned_by() {
        return assigned_by;
    }

    public void setAssigned_by(String assigned_by) {
        this.assigned_by = assigned_by;
    }

    public String getRemarkrs_by_doctor() {
        return remarkrs_by_doctor;
    }

    public void setRemarkrs_by_doctor(String remarkrs_by_doctor) {
        this.remarkrs_by_doctor = remarkrs_by_doctor;
    }

    public String getIs_doctor_done() {
        return is_doctor_done;
    }

    public void setIs_doctor_done(String is_doctor_done) {
        this.is_doctor_done = is_doctor_done;
    }

    public String getIs_doctor_done_on() {
        return is_doctor_done_on;
    }

    public void setIs_doctor_done_on(String is_doctor_done_on) {
        this.is_doctor_done_on = is_doctor_done_on;
    }

    public String getIs_pharmacists_done() {
        return is_pharmacists_done;
    }

    public void setIs_pharmacists_done(String is_pharmacists_done) {
        this.is_pharmacists_done = is_pharmacists_done;
    }

    public String getIs_pharmacists_done_on() {
        return is_pharmacists_done_on;
    }

    public void setIs_pharmacists_done_on(String is_pharmacists_done_on) {
        this.is_pharmacists_done_on = is_pharmacists_done_on;
    }

    public String getDel_action() {
        return del_action;
    }

    public void setDel_action(String del_action) {
        this.del_action = del_action;
    }

    public String getMedicine_delivery_status() {
        return medicine_delivery_status;
    }

    public void setMedicine_delivery_status(String medicine_delivery_status) {
        this.medicine_delivery_status = medicine_delivery_status;
    }

    public String getOtp_generated() {
        return otp_generated;
    }

    public void setOtp_generated(String otp_generated) {
        this.otp_generated = otp_generated;
    }

    public String getMedicine_status() {
        return medicine_status;
    }

    public void setMedicine_status(String medicine_status) {
        this.medicine_status = medicine_status;
    }

    public String getMedicine_charge() {
        return medicine_charge;
    }

    public void setMedicine_charge(String medicine_charge) {
        this.medicine_charge = medicine_charge;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }



    private String disease_id;
    private String symptom_id;
    private String patient_appointment_id;

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    private String reason;

    private String    id;
    private String   created_at;
    private String   test_suggested;
    private String   is_test_done;
    private String   test_verified_by_doctor;
    private String   is_test_on;
    private String   treatment_doc;
    private int     assigned_doctor;
    private String   assigned_doctor_on;
    private String    assigned_pharmacists;
    private String    assigned_pharmacists_on;
    private String     medicine_delivered_on;
    private String     is_verified;
    private String    assigned_by;
    private String    remarkrs_by_doctor;
    private String    is_doctor_done;
    private String     is_doctor_done_on;
    private String    is_pharmacists_done;
    private String    is_pharmacists_done_on;
    private String    del_action;
    private String     medicine_delivery_status;
    private String    otp_generated;
    private String  medicine_status;
    private String   medicine_charge;
    private String   age;
    private String   app_version;



    public static final String TABLE_NAME = "patient_appointments";
    public static final String COLUMN_prescribed_medicine = "prescribed_medicine";
    public static final String COLUMN_APPOINTMENT_MEDICINE_PRESCRIBED = "appointment_medicine_prescribed";
    public static final String COLUMN_PRESCRIPTION_URL = "prescription_url";
    public static final String COLUMN_del_action = "del_action";
    public static final String COLUMN_medicine_delivery_status = "medicine_delivery_status";
    public static final String COLUMN_otp_generated = "otp_generated";
    public static final String COLUMN_age = "age";
    public static final String COLUMN_medicine_charge = "medicine_charge";
    public static final String COLUMN_medicine_status = "medicine_status";
    public static final String COLUMN_id = "id";
    public static final String COLUMN_is_pharmacists_done_on = "is_pharmacists_done_on";
    public static final String COLUMN_is_pharmacists_done = "is_pharmacists_done";
    public static final String COLUMN_is_doctor_done_on = "is_doctor_done_on";
    public static final String COLUMN_is_doctor_done = "is_doctor_done";
    public static final String COLUMN_remarkrs_by_doctor = "remarkrs_by_doctor";
    public static final String COLUMN_assigned_by = "assigned_by";
    public static final String COLUMN_is_verified = "is_verified";
    public static final String COLUMN_medicine_delivered_on = "medicine_delivered_on";
    public static final String COLUMN_assigned_pharmacists_on = "assigned_pharmacists_on";
    public static final String COLUMN_assigned_pharmacists = "assigned_pharmacists";
    public static final String COLUMN_assigned_doctor_on = "assigned_doctor_on";
    public static final String COLUMN_assigned_doctor = "assigned_doctor";
    public static final String COLUMN_created_at = "created_at";
    public static final String COLUMN_test_suggested = "test_suggested";
    public static final String COLUMN_is_test_done = "is_test_done";
    public static final String COLUMN_test_verified_by_doctor = "test_verified_by_doctor";
    public static final String COLUMN_is_test_on = "is_test_on";
    public static final String COLUMN_treatment_doc = "treatment_doc";
    public static final String COLUMN_local_id = "local_id";
    public static final String COLUMN_is_emergency = "is_emergency";
    public static final String COLUMN_height = "height";
    public static final String COLUMN_weight = "weight";
    public static final String COLUMN_appointment_age = "appointment_age";
    public static final String COLUMN_appointment_file = "appointment_file";
    public static final String COLUMN_blood_group_id = "blood_group_id";
    public static final String COLUMN_remarks = "remarks";
    public static final String COLUMN_bp_high = "bp_high";
    public static final String COLUMN_bp_low = "bp_low";
    public static final String COLUMN_bp_upper = "bp_upper";
    public static final String COLUMN_bp_lower = "bp_lower";
    public static final String COLUMN_prescribed_medicine_date = "prescribed_medicine_date";
    public static final String COLUMN_sugar = "sugar";
    public static final String COLUMN_disease_name = "disease_name";
    public static final String COLUMN_prescription_days_id = "prescription_days_id";
    public static final String COLUMN_prescription_interval_id = "prescription_interval_id";
    public static final String COLUMN_prescription_eating_schedule_id = "prescription_eating_schedule_id";
    public static final String COLUMN_prescription_sos  = "prescription_sos ";
    public static final String COLUMN_pharmacist_id  = "pharmacist_id ";
    public static final String COLUMN_symptom_name = "symptom_name";
    public static final String COLUMN_temperature = "temperature";
    public static final String COLUMN_hemoglobin = "hemoglobin";
    public static final String COLUMN_blood_oxygen_level = "blood_oxygen_level";
    public static final String COLUMN_pulse = "pulse";
    public static final String COLUMN_counsellor_remarks = "counsellor_remarks";
    public static final String COLUMN_appointment_type= "appointment_type";
    public static final String COLUMN_disease_id = "disease_id";
    public static final String COLUMN_symptom_id = "symptom_id";
    public static final String COLUMN_app_version = "app_version";
    public static final String COLUMN_profile_patient_id = "profile_patient_id";
    public static final String COLUMN_patient_appointment_id = "patient_appointment_id";
    public static final String COLUMN_reason = "reason";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_flag = "flag";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_local_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_ID  + " INTEGER,"
                    + COLUMN_id  + " INTEGER,"
                    + COLUMN_profile_patient_id  + " INTEGER,"
                    + COLUMN_height  + " TEXT,"
                    + COLUMN_weight  + " TEXT,"
                    + COLUMN_is_emergency  + " TEXT,"
                    + COLUMN_appointment_file  + " TEXT,"
                    + COLUMN_blood_group_id  + " TEXT,"
                    + COLUMN_remarks  + " INTEGER,"
                    + COLUMN_bp_high  + " TEXT,"
                    + COLUMN_bp_low  + " TEXT,"
                    + COLUMN_bp_upper  + " INTEGER,"
                    + COLUMN_bp_lower  + " INTEGER,"
                    + COLUMN_pharmacist_id  + " INTEGER,"
                    + COLUMN_prescribed_medicine_date  + " TEXT,"
                    + COLUMN_prescription_interval_id  + " TEXT,"
                    + COLUMN_prescription_eating_schedule_id  + " TEXT,"
                    + COLUMN_assigned_by  + " TEXT,"
                    + COLUMN_prescription_sos  + " TEXT,"
                    + COLUMN_sugar  + " TEXT,"
                    + COLUMN_prescribed_medicine  + " TEXT,"
                    + COLUMN_APPOINTMENT_MEDICINE_PRESCRIBED  + " TEXT,"
                    + COLUMN_PRESCRIPTION_URL  + " TEXT,"
                    + COLUMN_disease_name  + " TEXT,"
                    + COLUMN_symptom_name  + " TEXT,"
                    + COLUMN_temperature  + " TEXT,"
                    + COLUMN_hemoglobin  + " TEXT,"
                    + COLUMN_prescription_days_id  + " TEXT,"
                    + COLUMN_blood_oxygen_level  + " TEXT,"
                    + COLUMN_appointment_age  + " TEXT,"
                    + COLUMN_pulse  + " TEXT,"
                    + COLUMN_counsellor_remarks  + " TEXT,"
                    + COLUMN_appointment_type  + " TEXT,"
                    + COLUMN_treatment_doc  + " TEXT,"
                    + COLUMN_disease_id  + " TEXT,"
                    + COLUMN_symptom_id  + " TEXT,"
                    + COLUMN_app_version  + " TEXT,"
                    + COLUMN_patient_appointment_id  + " INTEGER,"
                    + COLUMN_medicine_delivered_on  + " INTEGER,"
                    + COLUMN_is_test_on  + " TEXT,"
                    + COLUMN_remarkrs_by_doctor  + " TEXT,"
                    + COLUMN_assigned_pharmacists_on  + " TEXT,"
                    + COLUMN_test_verified_by_doctor  + " TEXT,"
                    + COLUMN_assigned_pharmacists  + " TEXT,"
                    + COLUMN_assigned_doctor  + " TEXT,"
                    + COLUMN_assigned_doctor_on  + " TEXT,"
                    + COLUMN_is_verified  + " TEXT,"
                    + COLUMN_is_pharmacists_done  + " TEXT,"
                    + COLUMN_is_doctor_done_on  + " TEXT,"
                    + COLUMN_test_suggested  + " TEXT,"
                    + COLUMN_is_doctor_done  + " TEXT,"
                    + COLUMN_is_pharmacists_done_on  + " TEXT,"
                    + COLUMN_medicine_charge  + " TEXT,"
                    + COLUMN_medicine_status  + " TEXT,"
                    + COLUMN_age  + " INTEGER,"
                    + COLUMN_reason  + " TEXT,"
                    + COLUMN_is_test_done  + " TEXT,"
                    + COLUMN_medicine_delivery_status  + " TEXT,"
                    + COLUMN_otp_generated  + " INTEGER,"
                    + COLUMN_created_at  + " INTEGER,"
                    + COLUMN_del_action  + " INTEGER,"
                    + COLUMN_flag  + " TEXT"
                    + ")";


    public String getBp_high() {
        return bp_high;
    }

    public void setBp_high(String bp_high) {
        this.bp_high = bp_high;
    }

    public String getBp_low() {
        return bp_low;
    }

    public void setBp_low(String bp_low) {
        this.bp_low = bp_low;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

//    private String sugar;
public String getBp_lower() {
    return bp_lower;
}

    public void setBp_lower(String bp_lower) {
        this.bp_lower = bp_lower;
    }

    public String getBp_upper() {
        return bp_upper;
    }

    public void setBp_upper(String bp_upper) {
        this.bp_upper = bp_upper;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBlood_oxygen_level() {
        return blood_oxygen_level;
    }

    public void setBlood_oxygen_level(String blood_oxygen_level) {
        this.blood_oxygen_level = blood_oxygen_level;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }


    public String getAppointment_age() {
        return appointment_age;
    }

    public void setAppointment_age(String appointment_age) {
        this.appointment_age = appointment_age;
    }

    private String appointment_age;

    public String getCounsellor_remarks() {
        return counsellor_remarks;
    }

    public void setCounsellor_remarks(String counsellor_remarks) {
        this.counsellor_remarks = counsellor_remarks;
    }



    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPatient_appointment_id() {
        return patient_appointment_id;
    }

    public void setPatient_appointment_id(String patient_appointment_id) {
        this.patient_appointment_id = patient_appointment_id;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getProfile_patient_id() {
        return profile_patient_id;
    }

    public void setProfile_patient_id(String profile_patient_id) {
        this.profile_patient_id = profile_patient_id;
    }

    public String getAppointment_file() {
        return appointment_file;
    }

    public void setAppointment_file(String appointment_file) {
        this.appointment_file = appointment_file;
    }

    public String getBlood_group_id() {
        return blood_group_id;
    }

    public void setBlood_group_id(String blood_group_id) {
        this.blood_group_id = blood_group_id;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAppointment_type() {
        return appointment_type;
    }

    public void setAppointment_type(String appointment_type) {
        this.appointment_type = appointment_type;
    }

    public String getPrescribed_medicine() {
        return prescribed_medicine;
    }

    public void setPrescribed_medicine(String prescribed_medicine) {
        this.prescribed_medicine = prescribed_medicine;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPrescribed_medicine_date() {
        return prescribed_medicine_date;
    }

    public void setPrescribed_medicine_date(String prescribed_medicine_date) {
        this.prescribed_medicine_date = prescribed_medicine_date;
    }

    public String getIs_emergency() {
        return is_emergency;
    }

    public void setIs_emergency(String is_emergency) {
        this.is_emergency = is_emergency;
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
}

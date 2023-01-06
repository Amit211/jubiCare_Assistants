package com.indev.jubicare_assistants;

public class SignUpModel {
    private String full_name;
    private String contact_no;
    private String dob;
    private String aadhar_no;
    private String state_id;
    private int local_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String district_id;
    private String block_id;

    public String getAge_month() {
        return age_month;
    }

    public void setAge_month(String age_month) {
        this.age_month = age_month;
    }

    private String post_office_id;
    private String village_id;
    private String address;
    private String date_of_birth;
    private String emergency_contact_no;
    private String profile_pic;
    private String gender;
    private String success;
    private String age_month;
    private String age;
    private String mobile_token;
    private String pin_code;
    private String EditProfile;

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getPost_office_name() {
        return post_office_name;
    }

    public void setPost_office_name(String post_office_name) {
        this.post_office_name = post_office_name;
    }

    private String height;
    private String covered_area;
    private String role_id;
    private String user_id;
    private String profile_type;
    private String state_name;
    private String district_name;
    private String block_name;
    private String village_name;
    private String post_office_name;
    //private String patient_profile_id;
    private String profile_patient_id;
    private String app_version;
    private String bp_high;
    private String bp_low;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    private String created_at;

    public String getProfile_patient_id() {
        return profile_patient_id;
    }

    public void setProfile_patient_id(String profile_patient_id) {
        this.profile_patient_id = profile_patient_id;
    }

    private String bp;

    public String getPatient_profile_id() {
        return patient_profile_id;
    }

    public void setPatient_profile_id(String patient_profile_id) {
        this.patient_profile_id = patient_profile_id;
    }

    private String bp_lower;
    private String patient_profile_id;

    public String getProfile_patients_created_at() {
        return profile_patients_created_at;
    }

    public void setProfile_patients_created_at(String profile_patients_created_at) {
        this.profile_patients_created_at = profile_patients_created_at;
    }

    private String bp_upper;
    private String profile_patients_created_at;

    public static final String TABLE_NAME = "profile_patients";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_local_id = "local_id";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_CONTACT_NO = "contact_no";
    public static final String COLUMN_amount = "amount";
    public static final String COLUMN_DOB = "dob";
    public static final String COLUMN_AADHAR_NO = "aadhar_no";
    public static final String COLUMN_STATE_ID = "state_id";
    public static final String COLUMN_DISTRICT_ID = "district_id";
    public static final String COLUMN_BLOCK_ID = "block_id";
    public static final String COLUMN_POST_OFFICE_ID = "post_office_id";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    public static final String COLUMN_EMERGENCY_CONTACT_NO = "emergency_contact_no";
    public static final String COLUMN_PROFILE_PIC = "profile_pic";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_SUCCESS = "success";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_age_month = "age_month";
    public static final String COLUMN_MOBILE_TOKEN = "mobile_token";
    public static final String COLUMN_PIN_CODE = "pin_code";
    public static final String COLUMN_EDITPROFILE = "EditProfile";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_COVERED_AREA = "covered_area";
    public static final String COLUMN_ROLE_ID = "role_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PROFILE_TYPE = "profile_type";
    public static final String COLUMN_profile_patient_id = "profile_patient_id";
    public static final String COLUMN_APP_VERSION = "app_version";
//    public static final String COLUMN_BP_HIGH = "bp_high";
//    public static final String COLUMN_BP_LOW = "bp_low";
//    public static final String COLUMN_BP = "bp";
//    public static final String COLUMN_BP_LOWER = "bp_lower";
//    public static final String COLUMN_BP_UPPER = "bp_upper";
//    public static final String COLUMN_TEMPERATURE = "temperature";
//    public static final String COLUMN_BLOOD_OXYGEN_LEVEL = "blood_oxygen_level";
//    public static final String COLUMN_PULSE = "pulse";
//    public static final String COLUMN_SUGAR = "sugar";
    public static final String COLUMN_CASTE_ID= "caste_id";
    public static final String COLUMN_DISABILITY= "disability";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_BLOOD_GROUP_ID = "blood_group_id";
    public static final String COLUMN_appointment_counting = "appointment_counting";
    public static final String COLUMN_profile_patients_created_at = "profile_patients_created_at";
    public static final String COLUMN_profile_patients_profile_pic = "profile_patients_profile_pic";
   // public static final String COLUMN_profile_patient_id = "profile_patient_id";
    public static final String COLUMN_state_name = "state_name";
    public static final String COLUMN_district_name = "district_name";
    public static final String COLUMN_block_name = "block_name";
    public static final String COLUMN_village_id = "village_id";
    public static final String COLUMN_village_name = "village_name";
    public static final String COLUMN_payment_status = "payment_status";
    public static final String COLUMN_id = "id";
    public static final String COLUMN_org_id = "org_id";
    public static final String COLUMN_partner_id = "partner_id";
    public static final String COLUMN_added_by = "added_by";
    public static final String COLUMN_self_added = "self_added";
    public static final String COLUMN_del_action = "del_action";
    public static final String COLUMN_created_at = "created_at";
    public static final String COLUMN_updated_at = "updated_at";
    public static final String COLUMN_flag= "flag";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_local_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_id + " INTEGER,"
                    + COLUMN_org_id + " INTEGER,"
                    + COLUMN_USER_ID  + " INTEGER,"
                    + COLUMN_profile_patient_id  + " TEXT,"
                    + COLUMN_partner_id  + " TEXT,"
                    + COLUMN_FULL_NAME  + " TEXT,"
                    + COLUMN_CONTACT_NO  + " INTEGER,"
                    + COLUMN_DOB  + " TEXT,"
                    + COLUMN_AADHAR_NO  + " INTEGER,"
                    + COLUMN_STATE_ID  + " INTEGER,"
                    + COLUMN_DISTRICT_ID  + " INTEGER,"
                    + COLUMN_BLOCK_ID  + " INTEGER,"
                    + COLUMN_POST_OFFICE_ID  + " INTEGER,"
                    + COLUMN_ADDRESS  + " TEXT,"
                    + COLUMN_DATE_OF_BIRTH  + " TEXT,"
                    + COLUMN_district_name  + " TEXT,"
                    + COLUMN_EMERGENCY_CONTACT_NO  + " INTEGER,"
                    + COLUMN_PROFILE_PIC  + " TEXT,"
                    + COLUMN_state_name  + " TEXT,"
                    + COLUMN_block_name  + " TEXT,"
                    + COLUMN_GENDER  + " TEXT,"
                    + COLUMN_SUCCESS  + " TEXT,"
                    + COLUMN_AGE  + " TEXT,"
                    + COLUMN_age_month  + " TEXT,"
                    + COLUMN_MOBILE_TOKEN  + " TEXT,"
                    + COLUMN_PIN_CODE  + " INTEGER,"
                    + COLUMN_EDITPROFILE  + " TEXT,"
                    + COLUMN_COVERED_AREA  + " TEXT,"
                    + COLUMN_village_name  + " TEXT,"
                    + COLUMN_ROLE_ID  + " INTEGER,"
                    + COLUMN_village_id  + " INTEGER,"
                    + COLUMN_PROFILE_TYPE  + " TEXT,"
                    + COLUMN_APP_VERSION  + " TEXT,"
                    + COLUMN_appointment_counting  + " INTEGER,"
                    + COLUMN_profile_patients_created_at  + " TEXT,"
                    + COLUMN_profile_patients_profile_pic  + " TEXT,"
                    + COLUMN_amount  + " TEXT,"
                    + COLUMN_payment_status  + " TEXT,"
//                    + COLUMN_BP_HIGH  + " TEXT,"
//                    + COLUMN_BP_LOW  + " TEXT,"
//                    + COLUMN_BP  + " TEXT,"
//                    + COLUMN_BP_LOWER  + " TEXT,"
//                    + COLUMN_BP_UPPER  + " TEXT,"
//                    + COLUMN_TEMPERATURE  + " TEXT,"
//                    + COLUMN_BLOOD_OXYGEN_LEVEL  + " TEXT,"
//                    + COLUMN_PULSE  + " TEXT,"
//                    + COLUMN_SUGAR  + " TEXT,"
                    + COLUMN_CASTE_ID  + " INTEGER,"
                    + COLUMN_DISABILITY  + " TEXT,"
                    + COLUMN_HEIGHT  + " TEXT,"
                    + COLUMN_WEIGHT  + " TEXT,"
                    + COLUMN_BLOOD_GROUP_ID  + " TEXT,"
                    + COLUMN_added_by  + " TEXT,"
                    + COLUMN_self_added  + " TEXT,"
                    + COLUMN_del_action  + " TEXT,"
                    + COLUMN_created_at  + " TEXT,"
                    + COLUMN_updated_at  + " TEXT,"
                    + COLUMN_flag  + " INTEGER DEFAULT '1'"
                    + ")";
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

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
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

    private String temperature;
    private String blood_oxygen_level;
    private String pulse;
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

    private String sugar;

    public String getCaste_id() {
        return caste_id;
    }

    public void setCaste_id(String caste_id) {
        this.caste_id = caste_id;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    private String caste_id;
    private String disability;

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getCovered_area() {
        return covered_area;
    }

    public void setCovered_area(String covered_area) {
        this.covered_area = covered_area;
    }

    public String getBlood_group_id() {
        return blood_group_id;
    }

    public void setBlood_group_id(String blood_group_id) {
        this.blood_group_id = blood_group_id;
    }

    private String weight;
    private String blood_group_id;

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



    public String getEditProfile() {
        return EditProfile;
    }

    public void setEditProfile(String editProfile) {
        EditProfile = editProfile;
    }

    public String getProfile_type() {
        return profile_type;
    }

    public void setProfile_type(String profile_type) {
        this.profile_type = profile_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMobile_token() {
        return mobile_token;
    }

    public void setMobile_token(String mobile_token) {
        this.mobile_token = mobile_token;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAadhar_no() {
        return aadhar_no;
    }

    public void setAadhar_no(String aadhar_no) {
        this.aadhar_no = aadhar_no;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergency_contact_no() {
        return emergency_contact_no;
    }

    public void setEmergency_contact_no(String emergency_contact_no) {
        this.emergency_contact_no = emergency_contact_no;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
    public int getLocal_id() {
        return local_id;
    }

    public void setLocal_id(int local_id) {
        this.local_id = local_id;
    }
    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getPost_office_id() {
        return post_office_id;
    }

    public void setPost_office_id(String post_office_id) {
        this.post_office_id = post_office_id;
    }
}

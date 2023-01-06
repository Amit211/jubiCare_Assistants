package com.indev.jubicare_assistants;

public class PatientPojo {
    public String appointment_counting;
    public String profile_patients_profile_pic;
    public String profile_patients_created_at;
    public String user_id;
    public int id;
    public String flag;
    public String local_id;
    public String profile_patient_id;
    public String block_name;
    public String district_name;
    public String state_name;
    public String village_name;
    public String created_at;
    public String full_name;
    public String gender;
    public String age;
    public String dob;
    public String aadhar_no;
    public String contact_no;
    public String state_id;
    public String district_id;
    public String block_id;
    public String village_id;
    public String address;
    public String emergency_contact_no;
    public String profile_pic;


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLocal_id() {
        return local_id;
    }

    public int getId() {
        return id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getAppointment_counting() {
        return appointment_counting;
    }

    public void setAppointment_counting(String appointment_counting) {
        this.appointment_counting = appointment_counting;
    }

    public String getProfile_patients_profile_pic() {
        return profile_patients_profile_pic;
    }

    public void setProfile_patients_profile_pic(String profile_patients_profile_pic) {
        this.profile_patients_profile_pic = profile_patients_profile_pic;
    }

    public String getProfile_patients_created_at() {
        return profile_patients_created_at;
    }

    public void setProfile_patients_created_at(String profile_patients_created_at) {
        this.profile_patients_created_at = profile_patients_created_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProfile_patient_id() {
        return profile_patient_id;
    }

    public void setProfile_patient_id(String profile_patient_id) {
        this.profile_patient_id = profile_patient_id;
    }

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
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


    public static final String TABLE_NAME = "profile_doctors";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_CONTACT_NO = "contact_no";
    public static final String COLUMN_name_with_degree = "name_with_degree";
    public static final String COLUMN_AADHAR_NO = "aadhar_no";
    public static final String COLUMN_otp = "otp";
//    public static final String COLUMN_profile_patient_id = "profile_patient_id";
    public static final String COLUMN_STATE_ID = "state_id";
    public static final String COLUMN_state_name = "state_name";
    public static final String COLUMN_DISTRICT_ID = "district_id";
    public static final String COLUMN_district_name = "district_name";
    public static final String COLUMN_BLOCK_ID = "block_id";
    public static final String COLUMN_block_name = "block_name";
    public static final String COLUMN_village_id = "village_id";
    public static final String COLUMN_village_name = "village_name";
    public static final String COLUMN_description = "description";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_flag = "flag";
    public static final String COLUMN_created_at = "created_at";
   // public static final String COLUMN_appointment_counting = "appointment_counting";
    //public static final String COLUMN_profile_patients_created_at = "profile_patients_created_at";
   // public static final String COLUMN_EMERGENCY_CONTACT_NO = "emergency_contact_no";
   // public static final String COLUMN_PROFILE_PIC = "profile_pic";
    //public static final String COLUMN_profile_patients_profile_pic = "profile_patients_profile_pic";
   // public static final String COLUMN_GENDER = "gender";
   // public static final String COLUMN_AGE = "age";




    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FULL_NAME  + " TEXT,"
                    + COLUMN_CONTACT_NO  + " INTEGER,"
                    + COLUMN_description  + " TEXT,"
                    + COLUMN_ADDRESS  + " TEXT,"
                    + COLUMN_AADHAR_NO  + " INTEGER,"
                    + COLUMN_otp  + " INTEGER,"
                    + COLUMN_STATE_ID  + " INTEGER,"
                    + COLUMN_DISTRICT_ID  + " INTEGER,"
                    + COLUMN_BLOCK_ID  + " INTEGER,"
                    + COLUMN_name_with_degree  + " TEXT,"
                    + COLUMN_village_id  + " INTEGER,"
                    + COLUMN_state_name  + " TEXT,"
                    + COLUMN_district_name  + " TEXT,"
                    + COLUMN_block_name  + " TEXT,"
                    + COLUMN_village_name  + " TEXT,"
                    + COLUMN_created_at  + " TEXT,"
                    + COLUMN_flag  + " TEXT"
                    + ")";
}

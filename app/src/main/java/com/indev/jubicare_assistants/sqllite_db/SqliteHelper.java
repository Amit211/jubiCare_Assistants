package com.indev.jubicare_assistants.sqllite_db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import com.indev.jubicare_assistants.AppointmentInput;
import com.indev.jubicare_assistants.DiseaseInput;
import com.indev.jubicare_assistants.DoctorAvailableDatePojo;
import com.indev.jubicare_assistants.PatientPojo;
import com.indev.jubicare_assistants.SharedPrefHelper;
import com.indev.jubicare_assistants.SignUpModel;
import com.indev.jubicare_assistants.State;
import com.indev.jubicare_assistants.model.AppointmentMedicinePrescribedModel;
import com.indev.jubicare_assistants.model.AppointmentTestModel;
import com.indev.jubicare_assistants.model.Block;
import com.indev.jubicare_assistants.model.BloodGroupModel;
import com.indev.jubicare_assistants.model.Caste;
import com.indev.jubicare_assistants.model.DiseaseModel;
import com.indev.jubicare_assistants.model.District;
import com.indev.jubicare_assistants.model.IvrCallingMasking;
import com.indev.jubicare_assistants.model.MedicineListModel;
import com.indev.jubicare_assistants.model.Pharmacy;
import com.indev.jubicare_assistants.model.PharmacyPatientModel;
import com.indev.jubicare_assistants.model.Postoffice;
import com.indev.jubicare_assistants.model.PrescriptionDays;
import com.indev.jubicare_assistants.model.PrescriptionEatingSchedule;
import com.indev.jubicare_assistants.model.PrescriptionInterval;
import com.indev.jubicare_assistants.model.SubTestsModel;
import com.indev.jubicare_assistants.model.Symptom;
import com.indev.jubicare_assistants.model.SymptomModel;
import com.indev.jubicare_assistants.model.Test;
import com.indev.jubicare_assistants.model.Village;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SqliteHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "jubicar_assitant_db";
    static final int DATABASE_VERSION = 1;
    String DB_PATH_SUFFIX = "/databases/";
    int version;
    Context ctx;
    SharedPrefHelper sharedPrefHelper;
    String uuid;
    public SqliteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
        sharedPrefHelper = new SharedPrefHelper(ctx);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(District.CREATE_TABLE);
        db.execSQL(State.CREATE_TABLE);
        db.execSQL(Pharmacy.CREATE_TABLE);
        db.execSQL(DoctorAvailableDatePojo.CREATE_TABLE);
        db.execSQL(Block.CREATE_TABLE);
        db.execSQL(AppointmentInput.CREATE_TABLE);
        db.execSQL(DiseaseModel.CREATE_TABLE);
        db.execSQL(MedicineListModel.CREATE_TABLE);
        db.execSQL(Village.CREATE_TABLE);
        db.execSQL(SymptomModel.CREATE_TABLE);
        db.execSQL(SubTestsModel.CREATE_TABLE);
        db.execSQL(AppointmentMedicinePrescribedModel.CREATE_TABLE);
        db.execSQL(AppointmentTestModel.CREATE_TABLE);
        db.execSQL(Test.CREATE_TABLE);
        db.execSQL(PrescriptionDays.CREATE_TABLE);
        db.execSQL(PrescriptionEatingSchedule.CREATE_TABLE);
        db.execSQL(PrescriptionInterval.CREATE_TABLE);
        db.execSQL(Postoffice.CREATE_TABLE);
        db.execSQL(BloodGroupModel.CREATE_TABLE);
        db.execSQL(SignUpModel.CREATE_TABLE);
        db.execSQL(IvrCallingMasking.CREATE_TABLE);
        db.execSQL(PatientPojo.CREATE_TABLE);
        db.execSQL(Caste.CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public SQLiteDatabase openDataBase() throws SQLException {

        Log.e("version", "outside" + version);

        File dbFile = ctx.getDatabasePath(DATABASE_NAME);
        //  checkDbVersion(dbFile);
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public boolean checkExist(String tableName, String colName, String whrCol) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean status = false;
        Cursor cursor = null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select " + colName + " from " + tableName + " where " + colName + " ='" + whrCol+"'";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    status=true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
            
        }

        return status;
    }
    public void updateMasterTable(ContentValues contentValues, String tablename, String whrCol, String whrColValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        long idsds =db.update(tablename, contentValues, whrCol+" = '" + whrColValue + "'", null);
        Log.d("LOG", idsds + " id");
        db.close();
    }

    public void saveMasterTable(ContentValues contentValues, String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        long idsds = db.insert(tablename, null, contentValues);
       // Log.d("LOG", idsds + " id");
        db.close();
    }

    public void dropTable(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM'" + tablename + "'");
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }
    public void dropTableWhere(String tablename,String whereColumn,String whereValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM'" + tablename + "' WHERE "+whereColumn+"='"+whereValue+"'");
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public ArrayList<AppointmentInput> AppointmrntList(String id) {
        ArrayList<AppointmentInput> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select profile_patient_id ,assigned_doctor_on, local_id from patient_appointments  Where profile_patient_id = '" + id + "'  ";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        AppointmentInput appointmentInput = new AppointmentInput();
                        appointmentInput.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("profile_patient_id")));
                        appointmentInput.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        appointmentInput.setAssigned_doctor_on(cursor.getString(cursor.getColumnIndex("assigned_doctor_on")));


                        arrayList.add(appointmentInput);
                        cursor.moveToNext();
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return arrayList;
    }

    public ArrayList<PatientPojo> getpatientlist(int index, int limit)
    {
        ArrayList<PatientPojo> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select contact_no, full_name, flag, local_id, profile_pic, state_id, gender, id, district_id, block_id, age, village_id, created_at, appointment_counting from profile_patients order by created_at desc LIMIT "+index+", "+limit+"";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        PatientPojo patientPojo = new PatientPojo();
                        patientPojo.setContact_no(cursor.getString(cursor.getColumnIndex("contact_no")));
                        patientPojo.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("id")));
                        patientPojo.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                        patientPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        patientPojo.setProfile_pic(cursor.getString(cursor.getColumnIndex("profile_pic")));
                        patientPojo.setState_id(cursor.getString(cursor.getColumnIndex("state_id")));
                        patientPojo.setDistrict_id(cursor.getString(cursor.getColumnIndex("district_id")));
                        patientPojo.setBlock_id(cursor.getString(cursor.getColumnIndex("block_id")));
                        patientPojo.setAge(cursor.getString(cursor.getColumnIndex("age")));
                        patientPojo.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                        patientPojo.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
                        patientPojo.setVillage_id(cursor.getString(cursor.getColumnIndex("village_id")));
                        patientPojo.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                        patientPojo.setAppointment_counting(cursor.getString(cursor.getColumnIndex("appointment_counting")));
                        arrayList.add(patientPojo);
                        cursor.moveToNext();
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return arrayList;
    }
    public ArrayList<PatientPojo> getpatientlistT(int index, int limit)
    {
        ArrayList<PatientPojo> patientArrayList=new ArrayList<>();
        for (int i = index; i < limit; i++) {
            PatientPojo user = new PatientPojo();
            user.setFull_name("Name " + i);
            user.setAge("25");
            user.setGender("M");
            user.setFlag("1");
            user.setState_id("1");
            user.setDistrict_id("15");
            user.setBlock_id("2");
            user.setVillage_id("3");
            user.setContact_no("999" + i + "380292");
            user.setProfile_patient_id("1" + i + "1");
            patientArrayList.add(user);
        }
        return  patientArrayList;
    }
    public String getPharmecyName(String id,String table) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            cursor = db.rawQuery("select full_name from " + table + " where user_id = '" + id + "' ", null);
            if (cursor.moveToFirst())
                sum = cursor.getString(cursor.getColumnIndex("full_name"));
        }catch(Exception ex){
            Log.d("getPharmacyName",ex.getMessage());   
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return sum;
    }

    public ArrayList<PatientPojo> getpatientlist1(String searchdata) {
        ArrayList<PatientPojo> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select  contact_no, full_name, flag,local_id,profile_pic, state_id,gender,id, district_id, block_id ,age, village_id, created_at, appointment_counting  from profile_patients where contact_no  like " + "'" + searchdata + "%' or full_name  like " + "'" + searchdata + "%' ";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        PatientPojo patientPojo = new PatientPojo();
                        patientPojo.setContact_no(cursor.getString(cursor.getColumnIndex("contact_no")));
                        patientPojo.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("id")));
                        patientPojo.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                        patientPojo.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        patientPojo.setProfile_pic(cursor.getString(cursor.getColumnIndex("profile_pic")));
                        patientPojo.setState_id(cursor.getString(cursor.getColumnIndex("state_id")));
                        patientPojo.setDistrict_id(cursor.getString(cursor.getColumnIndex("district_id")));
                        patientPojo.setBlock_id(cursor.getString(cursor.getColumnIndex("block_id")));
                        patientPojo.setAge(cursor.getString(cursor.getColumnIndex("age")));
                        patientPojo.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                        patientPojo.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
                        patientPojo.setVillage_id(cursor.getString(cursor.getColumnIndex("village_id")));
                        patientPojo.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                        patientPojo.setAppointment_counting(cursor.getString(cursor.getColumnIndex("appointment_counting")));
                        arrayList.add(patientPojo);
                        cursor.moveToNext();
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return arrayList;
    }


    public String getsymptom(String id, String table) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            cursor = db.rawQuery("select symptom from " + table + " where id = '" + id + "' ", null);
            if (cursor.moveToFirst())
                sum = cursor.getString(cursor.getColumnIndex("symptom"));
        }catch(Exception ex){
          //
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return sum;
    }


    public String getstateName(String id, String table) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            cursor = db.rawQuery("select name from " + table + " where id = '" + id + "' ", null);
            if (cursor.moveToFirst())
                sum = cursor.getString(cursor.getColumnIndex("name"));
        }catch(Exception ex){}
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return sum;
    }
    public String getdistrictName(String id, String table) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            cursor = db.rawQuery("select name from " + table + " where id = '" + id + "' ", null);
            if (cursor.moveToFirst())
                sum = cursor.getString(cursor.getColumnIndex("name"));
        }catch(Exception ex){}
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return sum;
    }
    public String getblockName(String id, String table) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            cursor = db.rawQuery("select name from " + table + " where id = '" + id + "' ", null);
            if (cursor.moveToFirst())
                sum = cursor.getString(cursor.getColumnIndex("name"));
        }catch (Exception ex){}
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return sum;
    }
    public String getvillageName(String id, String table) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try{
        cursor = db.rawQuery("select name from " + table + " where id = '" + id + "' ", null);
            if (cursor.moveToFirst())
                sum = cursor.getString(cursor.getColumnIndex("name"));
        }catch(Exception ex){ }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return sum;
    }

    public String getvillageName1(String id) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select flag from patient_appointments where id = '" + id + "' ", null);
        if (cursor.moveToFirst())
            sum = cursor.getString(cursor.getColumnIndex("flag"));
        return sum;
    }
    public int getAppointmentCount(int id) {
        int sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            cursor = db.rawQuery("SELECT count(profile_patient_id) as total from patient_appointments where profile_patient_id = '" + id + "'", null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("total"));
        }catch(Exception ex){}
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return sum;
    }

    public int getdoctorCount() {
        int sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(full_name) as total from profile_doctors ",null);
        if (cursor.moveToFirst())
            sum = cursor.getInt(cursor.getColumnIndex("total"));
        return sum;
    }
    public Cursor getMasterDataFromLocal(String type, String where) {
        int n = where.length();
        where = where.substring(0, n - 1);
        String query = "Select * from  " + type + " where category_id  in (" + where + ")";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor curs = db.rawQuery(query, null);
        return curs;
    }

    public Cursor getMasterDataFromLocal(String tablename) {
        String query = "Select * from  " + tablename;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor curs = db.rawQuery(query, null);
        return curs;
    }

    public long updateFlagInTable(String tableName, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {

                ContentValues value = new ContentValues();
                value.put("status", flag); // Name
                inserted_id = db.update(tableName, value, null, null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }


    public long updatePatientIdInTable(String table, String whr, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("id", flag);
                inserted_id = db.update(table, values, whr + " = '" + local_id + "'", null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }
    public void saveSignupData(SignUpModel signUpModel, String patient_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("aadhar_no", signUpModel.getAadhar_no());
                values.put("full_name", signUpModel.getFull_name());
                values.put("gender", signUpModel.getGender());
               values.put("app_version", signUpModel.getApp_version());
                values.put("user_id", signUpModel.getUser_id());
                values.put("dob", signUpModel.getDob());
                values.put("created_at", signUpModel.getCreated_at());
                values.put("address", signUpModel.getAddress());
                values.put("emergency_contact_no", signUpModel.getEmergency_contact_no());
                values.put("contact_no", signUpModel.getContact_no());
                values.put("profile_type", signUpModel.getProfile_type());
                values.put("disability", signUpModel.getDisability());
                values.put("weight", signUpModel.getWeight());
                values.put("height", signUpModel.getHeight());
                values.put("pin_code", signUpModel.getPin_code());
                values.put("state_id", signUpModel.getState_id());
                values.put("caste_id", signUpModel.getCaste_id());
                values.put("district_id", signUpModel.getDistrict_id());
                values.put("block_id", signUpModel.getBlock_id());
                values.put("post_office_id", signUpModel.getPost_office_id());
                values.put("village_id", signUpModel.getVillage_id());
                values.put("covered_area", signUpModel.getCovered_area());
                values.put("blood_group_id", signUpModel.getBlood_group_id());
                values.put("role_id", signUpModel.getRole_id());
                values.put("profile_pic", signUpModel.getProfile_pic());
                values.put("mobile_token", signUpModel.getMobile_token());
                values.put("age", signUpModel.getAge());
                values.put("age_month", signUpModel.getAge_month());
                values.put("id",signUpModel.getProfile_patient_id());
                values.put("flag", 0);

                if (patient_id == null || patient_id.equals("0")) {
                    db.insert("profile_patients", null, values);
                    db.close();
                } else {
                    db.update("profile_patients", values, "id = '" + patient_id + "'", null);
                    db.close(); // Closing database connection
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public void saveAppointmentData(AppointmentInput user,String patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("app_version", user.getApp_version());
                values.put("appointment_age", user.getAppointment_age());
                values.put("appointment_file", user.getAppointment_file());
                values.put("user_id", user.getUser_id());
                values.put("blood_group_id", user.getBlood_group_id());
                values.put("blood_oxygen_level", user.getBlood_oxygen_level());
                values.put("bp_lower", user.getBp_lower());
                values.put("bp_upper", user.getBp_upper());
                values.put("assigned_doctor", user.getAssigned_doctor());
                values.put("assigned_doctor_on", user.getAssigned_doctor_on());
                values.put("appointment_type", user.getAppointment_type());
                values.put("weight", user.getWeight());
                values.put("height", user.getHeight());
               // values.put("counsellor_remarks", user.getCounsellor_remarks());
                values.put("prescribed_medicine_date", user.getPrescribed_medicine_date());
               // values.put("reason", user.getReason());
                values.put("prescribed_medicine", user.getPrescribed_medicine());
                values.put("is_emergency", user.getIs_emergency());
                values.put("pulse", user.getPulse());
                values.put("sugar", user.getSugar());
                values.put("remarks", user.getRemarks());
                values.put("flag", 0);
                //values.put("blood_group_id", user.getBlood_group_id());
                values.put("symptom_id", user.getSymptom_id());
                //values.put("disease_id", user.getDisease_id());
                values.put("temperature", user.getTemperature());
                values.put("hemoglobin", user.getHemoglobin());
//                if (!uuid.equals("")){
//                    values.put("profile_patient_id", uuid);
//                }else {
                    values.put("profile_patient_id", user.getProfile_patient_id());
                    values.put("created_at", user.getCreated_at());
                    values.put("symptom_name", getSymptomName(user.getSymptom_id()));
                    values.put("disease_name", "");
                    values.put("pharmacist_id", 0);
                    values.put("is_pharmacists_done", "N");
                    values.put("is_pharmacists_done_on", "");
                    values.put("is_doctor_done", "N");
                    values.put("is_doctor_done_on", "");
                    values.put("assigned_pharmacists", 0);
                    values.put("assigned_pharmacists_on", "");
                    values.put("remarkrs_by_doctor", "");

                //}
                    db.insert("patient_appointments", null, values);
                    db.close();
                }


        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }
    public AppointmentInput syncAppointmentData() {
        AppointmentInput appointmentInput = new AppointmentInput();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select profile_patient_id ,app_version,appointment_file,user_id,assigned_doctor_on,assigned_doctor,blood_oxygen_level,appointment_age,is_emergency,prescribed_medicine_date,prescribed_medicine,sugar,pulse,temperature,hemoglobin,bp_lower,symptom_id,bp_upper,weight,height,appointment_type from patient_appointments Where flag = 0  ";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        appointmentInput = new AppointmentInput();

                        appointmentInput.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("profile_patient_id")));
                        appointmentInput.setApp_version(cursor.getString(cursor.getColumnIndex("app_version")));
                        appointmentInput.setAppointment_file(cursor.getString(cursor.getColumnIndex("appointment_file")));
                        appointmentInput.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        appointmentInput.setBlood_oxygen_level(cursor.getString(cursor.getColumnIndex("blood_oxygen_level")));
                        appointmentInput.setBp_lower(cursor.getString(cursor.getColumnIndex("bp_lower")));
                        appointmentInput.setBp_upper(cursor.getString(cursor.getColumnIndex("bp_upper")));
                        appointmentInput.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                        appointmentInput.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                        appointmentInput.setAssigned_doctor_on(cursor.getString(cursor.getColumnIndex("assigned_doctor_on")));
                        appointmentInput.setAssigned_doctor(cursor.getInt(cursor.getColumnIndex("assigned_doctor")));
                        appointmentInput.setAppointment_type((cursor.getString(cursor.getColumnIndex("appointment_type"))));
                        //appointmentInput.setCounsellor_remarks((cursor.getString(cursor.getColumnIndex("counsellor_remarks"))));
                        //appointmentInput.setReason((cursor.getString(cursor.getColumnIndex("reason"))));
                       // appointmentInput.setDisease_id((cursor.getString(cursor.getColumnIndex("disease_id"))));
                        appointmentInput.setSymptom_id((cursor.getString(cursor.getColumnIndex("symptom_id"))));
                        appointmentInput.setTemperature((cursor.getString(cursor.getColumnIndex("temperature"))));
                        appointmentInput.setHemoglobin((cursor.getString(cursor.getColumnIndex("hemoglobin"))));
                        appointmentInput.setSugar((cursor.getString(cursor.getColumnIndex("sugar"))));
                        appointmentInput.setPulse((cursor.getString(cursor.getColumnIndex("pulse"))));
                        appointmentInput.setIs_emergency((cursor.getString(cursor.getColumnIndex("is_emergency"))));
                        appointmentInput.setPrescribed_medicine((cursor.getString(cursor.getColumnIndex("prescribed_medicine"))));
                        appointmentInput.setPrescribed_medicine_date((cursor.getString(cursor.getColumnIndex("prescribed_medicine_date"))));
                       // appointmentInput.setBlood_group_id((cursor.getString(cursor.getColumnIndex("blood_group_id"))));
                        appointmentInput.setAppointment_age((cursor.getString(cursor.getColumnIndex("appointment_age"))));

                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return appointmentInput;
    }



    public AppointmentInput syncAppointmentOFlinData(String profile_id) {
        AppointmentInput appointmentInput = new AppointmentInput();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select profile_patient_id ,app_version,appointment_file,user_id,blood_oxygen_level,assigned_doctor_on,assigned_doctor,appointment_age,is_emergency,prescribed_medicine_date,prescribed_medicine,sugar,pulse,temperature,hemoglobin,bp_lower,symptom_id,bp_upper,weight,height,appointment_type from patient_appointments Where profile_patient_id = '" + profile_id + "' and flag =0 ";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        appointmentInput = new AppointmentInput();

                        appointmentInput.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("profile_patient_id")));
                        appointmentInput.setApp_version(cursor.getString(cursor.getColumnIndex("app_version")));
                        appointmentInput.setAppointment_file(cursor.getString(cursor.getColumnIndex("appointment_file")));
                        appointmentInput.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        appointmentInput.setBlood_oxygen_level(cursor.getString(cursor.getColumnIndex("blood_oxygen_level")));
                        appointmentInput.setBp_lower(cursor.getString(cursor.getColumnIndex("bp_lower")));
                        appointmentInput.setBp_upper(cursor.getString(cursor.getColumnIndex("bp_upper")));
                        appointmentInput.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                        appointmentInput.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                        appointmentInput.setAppointment_type((cursor.getString(cursor.getColumnIndex("appointment_type"))));
                        //appointmentInput.setCounsellor_remarks((cursor.getString(cursor.getColumnIndex("counsellor_remarks"))));
                        //appointmentInput.setReason((cursor.getString(cursor.getColumnIndex("reason"))));
                        // appointmentInput.setDisease_id((cursor.getString(cursor.getColumnIndex("disease_id"))));
                        appointmentInput.setSymptom_id((cursor.getString(cursor.getColumnIndex("symptom_id"))));
                        appointmentInput.setTemperature((cursor.getString(cursor.getColumnIndex("temperature"))));
                        appointmentInput.setHemoglobin((cursor.getString(cursor.getColumnIndex("hemoglobin"))));
                        appointmentInput.setSugar((cursor.getString(cursor.getColumnIndex("sugar"))));
                        appointmentInput.setAssigned_doctor_on(cursor.getString(cursor.getColumnIndex("assigned_doctor_on")));
                        appointmentInput.setAssigned_doctor(cursor.getInt(cursor.getColumnIndex("assigned_doctor")));
                        appointmentInput.setPulse((cursor.getString(cursor.getColumnIndex("pulse"))));
                        appointmentInput.setIs_emergency((cursor.getString(cursor.getColumnIndex("is_emergency"))));
                        appointmentInput.setPrescribed_medicine((cursor.getString(cursor.getColumnIndex("prescribed_medicine"))));
                        appointmentInput.setPrescribed_medicine_date((cursor.getString(cursor.getColumnIndex("prescribed_medicine_date"))));
                        // appointmentInput.setBlood_group_id((cursor.getString(cursor.getColumnIndex("blood_group_id"))));
                        appointmentInput.setAppointment_age((cursor.getString(cursor.getColumnIndex("appointment_age"))));

                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return appointmentInput;
    }



    public ArrayList<AppointmentInput> syCAppointmentOFLineData1(String id) {
        ArrayList<AppointmentInput> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select profile_patient_id ,app_version,local_id,appointment_file,assigned_doctor_on,assigned_doctor,flag,user_id,blood_oxygen_level,appointment_age,is_emergency,prescribed_medicine_date,prescribed_medicine,sugar,pulse,temperature,hemoglobin,bp_lower,symptom_id,bp_upper,weight,height,appointment_type from patient_appointments Where profile_patient_id = '" + id + "' and flag =0 ";
                 cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        AppointmentInput appointmentInput = new AppointmentInput();
                        appointmentInput.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        appointmentInput.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("profile_patient_id")));
                        appointmentInput.setApp_version(cursor.getString(cursor.getColumnIndex("app_version")));
                        appointmentInput.setAppointment_file(cursor.getString(cursor.getColumnIndex("appointment_file")));
                        appointmentInput.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        appointmentInput.setBlood_oxygen_level(cursor.getString(cursor.getColumnIndex("blood_oxygen_level")));
                        appointmentInput.setBp_lower(cursor.getString(cursor.getColumnIndex("bp_lower")));
                        appointmentInput.setBp_upper(cursor.getString(cursor.getColumnIndex("bp_upper")));
                        appointmentInput.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                        appointmentInput.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                        appointmentInput.setAppointment_type((cursor.getString(cursor.getColumnIndex("appointment_type"))));
                        //appointmentInput.setCounsellor_remarks((cursor.getString(cursor.getColumnIndex("counsellor_remarks"))));
                        //appointmentInput.setReason((cursor.getString(cursor.getColumnIndex("reason"))));
                        // appointmentInput.setDisease_id((cursor.getString(cursor.getColumnIndex("disease_id"))));
                        appointmentInput.setSymptom_id((cursor.getString(cursor.getColumnIndex("symptom_id"))));
                        appointmentInput.setTemperature((cursor.getString(cursor.getColumnIndex("temperature"))));
                        appointmentInput.setHemoglobin((cursor.getString(cursor.getColumnIndex("hemoglobin"))));
                        appointmentInput.setSugar((cursor.getString(cursor.getColumnIndex("sugar"))));
                        appointmentInput.setPulse((cursor.getString(cursor.getColumnIndex("pulse"))));
                        appointmentInput.setFlag((cursor.getString(cursor.getColumnIndex("flag"))));
//                        appointmentInput.setAssigned_pharmacists((cursor.getString(cursor.getColumnIndex("assigned_pharmacists"))));
//                        appointmentInput.setAssigned_pharmacists_on((cursor.getString(cursor.getColumnIndex("assigned_pharmacists_on"))));
                        appointmentInput.setAssigned_doctor_on(cursor.getString(cursor.getColumnIndex("assigned_doctor_on")));
                        appointmentInput.setAssigned_doctor(cursor.getInt(cursor.getColumnIndex("assigned_doctor")));
                        appointmentInput.setIs_emergency((cursor.getString(cursor.getColumnIndex("is_emergency"))));
                        appointmentInput.setPrescribed_medicine((cursor.getString(cursor.getColumnIndex("prescribed_medicine"))));
                        appointmentInput.setPrescribed_medicine_date((cursor.getString(cursor.getColumnIndex("prescribed_medicine_date"))));
                        // appointmentInput.setBlood_group_id((cursor.getString(cursor.getColumnIndex("blood_group_id"))));
                        appointmentInput.setAppointment_age((cursor.getString(cursor.getColumnIndex("appointment_age"))));

                        arrayList.add(appointmentInput);
                        cursor.moveToNext();
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return arrayList;
    }



    public AppointmentInput syncAppointmentOFlinData1( String id ) {
        AppointmentInput appointmentInput = new AppointmentInput();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select profile_patient_id ,app_version,appointment_file,assigned_doctor_on,assigned_doctor,flag,user_id,blood_oxygen_level,appointment_age,is_emergency,prescribed_medicine_date,prescribed_medicine,sugar,pulse,temperature,hemoglobin,bp_lower,symptom_id,bp_upper,weight,height,appointment_type from patient_appointments Where profile_patient_id = '" + id + "' and flag =0 ";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        appointmentInput = new AppointmentInput();

                        appointmentInput.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("profile_patient_id")));
                        appointmentInput.setApp_version(cursor.getString(cursor.getColumnIndex("app_version")));
                        appointmentInput.setAppointment_file(cursor.getString(cursor.getColumnIndex("appointment_file")));
                        appointmentInput.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        appointmentInput.setBlood_oxygen_level(cursor.getString(cursor.getColumnIndex("blood_oxygen_level")));
                        appointmentInput.setBp_lower(cursor.getString(cursor.getColumnIndex("bp_lower")));
                        appointmentInput.setBp_upper(cursor.getString(cursor.getColumnIndex("bp_upper")));
                        appointmentInput.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                        appointmentInput.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                        appointmentInput.setAppointment_type((cursor.getString(cursor.getColumnIndex("appointment_type"))));
                        //appointmentInput.setCounsellor_remarks((cursor.getString(cursor.getColumnIndex("counsellor_remarks"))));
                        //appointmentInput.setReason((cursor.getString(cursor.getColumnIndex("reason"))));
                        // appointmentInput.setDisease_id((cursor.getString(cursor.getColumnIndex("disease_id"))));
                        appointmentInput.setSymptom_id((cursor.getString(cursor.getColumnIndex("symptom_id"))));
                        appointmentInput.setTemperature((cursor.getString(cursor.getColumnIndex("temperature"))));
                        appointmentInput.setHemoglobin((cursor.getString(cursor.getColumnIndex("hemoglobin"))));
                        appointmentInput.setSugar((cursor.getString(cursor.getColumnIndex("sugar"))));
                        appointmentInput.setPulse((cursor.getString(cursor.getColumnIndex("pulse"))));
                        appointmentInput.setFlag((cursor.getString(cursor.getColumnIndex("flag"))));
                        appointmentInput.setAssigned_doctor_on(cursor.getString(cursor.getColumnIndex("assigned_doctor_on")));
                        appointmentInput.setAssigned_doctor(cursor.getInt(cursor.getColumnIndex("assigned_doctor")));
                        appointmentInput.setIs_emergency((cursor.getString(cursor.getColumnIndex("is_emergency"))));
                        appointmentInput.setPrescribed_medicine((cursor.getString(cursor.getColumnIndex("prescribed_medicine"))));
                        appointmentInput.setPrescribed_medicine_date((cursor.getString(cursor.getColumnIndex("prescribed_medicine_date"))));
                        // appointmentInput.setBlood_group_id((cursor.getString(cursor.getColumnIndex("blood_group_id"))));
                        appointmentInput.setAppointment_age((cursor.getString(cursor.getColumnIndex("appointment_age"))));

                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return appointmentInput;
    }

    public AppointmentInput syncAppointmentOFlinData2(String id) {
        AppointmentInput appointmentInput = new AppointmentInput();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select profile_patient_id ,app_version,is_pharmacists_done,is_doctor_done,is_verified,remarkrs_by_doctor,is_doctor_done_on,is_pharmacists_done_on,appointment_file,symptom_name,local_id,assigned_pharmacists_on,assigned_pharmacists,disease_name,appointment_file,assigned_doctor_on,assigned_doctor,user_id,blood_oxygen_level,appointment_age,is_emergency,prescribed_medicine_date,prescribed_medicine,sugar,pulse,temperature,hemoglobin,bp_lower,bp_upper,weight,height,appointment_type from patient_appointments Where local_id = '" + id + "'  ";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        appointmentInput = new AppointmentInput();

                        appointmentInput.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("profile_patient_id")));
                        appointmentInput.setIs_verified(cursor.getString(cursor.getColumnIndex("is_verified")));
                       // appointmentInput.setD(cursor.getString(cursor.getColumnIndex("is_verified")));
                        appointmentInput.setIs_pharmacists_done(cursor.getString(cursor.getColumnIndex("is_pharmacists_done")));
                        appointmentInput.setIs_pharmacists_done_on(cursor.getString(cursor.getColumnIndex("is_pharmacists_done_on")));
                        appointmentInput.setIs_doctor_done(cursor.getString(cursor.getColumnIndex("is_doctor_done")));
                        appointmentInput.setIs_doctor_done_on(cursor.getString(cursor.getColumnIndex("is_doctor_done_on")));
                        appointmentInput.setRemarkrs_by_doctor(cursor.getString(cursor.getColumnIndex("remarkrs_by_doctor")));
                        appointmentInput.setLocal_id(cursor.getString(cursor.getColumnIndex("local_id")));
                        appointmentInput.setApp_version(cursor.getString(cursor.getColumnIndex("app_version")));
                        appointmentInput.setAppointment_file(cursor.getString(cursor.getColumnIndex("appointment_file")));
                     //  appointmentInput.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                        appointmentInput.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        appointmentInput.setBlood_oxygen_level(cursor.getString(cursor.getColumnIndex("blood_oxygen_level")));
                        appointmentInput.setBp_lower(cursor.getString(cursor.getColumnIndex("bp_lower")));
                        appointmentInput.setBp_upper(cursor.getString(cursor.getColumnIndex("bp_upper")));
                        appointmentInput.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                        appointmentInput.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                        appointmentInput.setAppointment_type((cursor.getString(cursor.getColumnIndex("appointment_type"))));
                         appointmentInput.setDisease_name((cursor.getString(cursor.getColumnIndex("disease_name"))));
                        appointmentInput.setSymptom_name((cursor.getString(cursor.getColumnIndex("symptom_name"))));
                        appointmentInput.setTemperature((cursor.getString(cursor.getColumnIndex("temperature"))));
                        appointmentInput.setHemoglobin((cursor.getString(cursor.getColumnIndex("hemoglobin"))));
                        appointmentInput.setSugar((cursor.getString(cursor.getColumnIndex("sugar"))));
                        appointmentInput.setPulse((cursor.getString(cursor.getColumnIndex("pulse"))));
                        appointmentInput.setAssigned_doctor_on(cursor.getString(cursor.getColumnIndex("assigned_doctor_on")));
                        appointmentInput.setAssigned_pharmacists_on(cursor.getString(cursor.getColumnIndex("assigned_pharmacists_on")));
                        appointmentInput.setAssigned_pharmacists(cursor.getString(cursor.getColumnIndex("assigned_pharmacists")));
                        appointmentInput.setAssigned_doctor(cursor.getInt(cursor.getColumnIndex("assigned_doctor")));
                        appointmentInput.setIs_emergency((cursor.getString(cursor.getColumnIndex("is_emergency"))));
                        appointmentInput.setPrescribed_medicine((cursor.getString(cursor.getColumnIndex("prescribed_medicine"))));
                        appointmentInput.setPrescribed_medicine_date((cursor.getString(cursor.getColumnIndex("prescribed_medicine_date"))));
                        appointmentInput.setAppointment_age((cursor.getString(cursor.getColumnIndex("appointment_age"))));

                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return appointmentInput;
    }

    public ArrayList<AppointmentInput> getDataForShowAppointmentOfPatient(String profile_id) {
        ArrayList<AppointmentInput> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select id,app_version,is_pharmacists_done,is_doctor_done,is_verified,remarkrs_by_doctor,is_doctor_done_on,is_pharmacists_done_on,appointment_file,symptom_name,assigned_pharmacists_on,assigned_pharmacists,disease_name,appointment_file,assigned_doctor_on,assigned_doctor,user_id,blood_oxygen_level,appointment_age,is_emergency,prescribed_medicine_date,prescribed_medicine,sugar,pulse,temperature,hemoglobin,bp_lower,bp_upper,weight,height,appointment_type,created_at from patient_appointments Where profile_patient_id='"+profile_id+"'";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        AppointmentInput appointmentInput = new AppointmentInput();
                        appointmentInput.setId(cursor.getString(cursor.getColumnIndex("id")));
                        appointmentInput.setIs_verified(cursor.getString(cursor.getColumnIndex("is_verified")));
                        appointmentInput.setIs_pharmacists_done(cursor.getString(cursor.getColumnIndex("is_pharmacists_done")));
                        appointmentInput.setIs_pharmacists_done_on(cursor.getString(cursor.getColumnIndex("is_pharmacists_done_on")));
                        appointmentInput.setIs_doctor_done(cursor.getString(cursor.getColumnIndex("is_doctor_done")));
                        appointmentInput.setIs_doctor_done_on(cursor.getString(cursor.getColumnIndex("is_doctor_done_on")));
                        appointmentInput.setRemarkrs_by_doctor(cursor.getString(cursor.getColumnIndex("remarkrs_by_doctor")));
                        appointmentInput.setApp_version(cursor.getString(cursor.getColumnIndex("app_version")));
                        appointmentInput.setAppointment_file(cursor.getString(cursor.getColumnIndex("appointment_file")));
                        appointmentInput.setUser_id(cursor.getString(cursor.getColumnIndex("user_id")));
                        appointmentInput.setBlood_oxygen_level(cursor.getString(cursor.getColumnIndex("blood_oxygen_level")));
                        appointmentInput.setBp_lower(cursor.getString(cursor.getColumnIndex("bp_lower")));
                        appointmentInput.setBp_upper(cursor.getString(cursor.getColumnIndex("bp_upper")));
                        appointmentInput.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                        appointmentInput.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                        appointmentInput.setAppointment_type((cursor.getString(cursor.getColumnIndex("appointment_type"))));
                        appointmentInput.setDisease_name((cursor.getString(cursor.getColumnIndex("disease_name"))));
                        appointmentInput.setSymptom_name((cursor.getString(cursor.getColumnIndex("symptom_name"))));
                        appointmentInput.setTemperature((cursor.getString(cursor.getColumnIndex("temperature"))));
                        appointmentInput.setHemoglobin((cursor.getString(cursor.getColumnIndex("hemoglobin"))));
                        appointmentInput.setSugar((cursor.getString(cursor.getColumnIndex("sugar"))));
                        appointmentInput.setPulse((cursor.getString(cursor.getColumnIndex("pulse"))));
                        appointmentInput.setAssigned_doctor_on(cursor.getString(cursor.getColumnIndex("assigned_doctor_on")));
                        appointmentInput.setAssigned_pharmacists_on(cursor.getString(cursor.getColumnIndex("assigned_pharmacists_on")));
                        appointmentInput.setAssigned_pharmacists(cursor.getString(cursor.getColumnIndex("assigned_pharmacists")));
                        appointmentInput.setAssigned_doctor(cursor.getInt(cursor.getColumnIndex("assigned_doctor")));
                        appointmentInput.setIs_emergency((cursor.getString(cursor.getColumnIndex("is_emergency"))));
                        appointmentInput.setPrescribed_medicine((cursor.getString(cursor.getColumnIndex("prescribed_medicine"))));
                        appointmentInput.setPrescribed_medicine_date((cursor.getString(cursor.getColumnIndex("prescribed_medicine_date"))));
                        appointmentInput.setAppointment_age((cursor.getString(cursor.getColumnIndex("appointment_age"))));
                        appointmentInput.setCreated_at((cursor.getString(cursor.getColumnIndex("created_at"))));

                        arrayList.add(appointmentInput);
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return arrayList;
    }

    public SignUpModel getPatientDetail(String id) {
        SignUpModel signUpModel = new SignUpModel();
        SQLiteDatabase db = this.getReadableDatabase();
Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select id,full_name ,gender,dob,contact_no,role_id,age_month,mobile_token,age,profile_pic,blood_group_id,covered_area,local_id,village_id,block_id,post_office_id,caste_id,district_id,aadhar_no,state_id,disability,weight,height,pin_code,created_at,address,emergency_contact_no from profile_patients where id = '" + id + "'";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        signUpModel = new SignUpModel();
                        signUpModel.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("id")));
                        signUpModel.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                        signUpModel.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                        signUpModel.setDob(cursor.getString(cursor.getColumnIndex("dob")));
                        signUpModel.setAge_month(cursor.getString(cursor.getColumnIndex("age_month")));
                        signUpModel.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                        signUpModel.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        signUpModel.setEmergency_contact_no(cursor.getString(cursor.getColumnIndex("emergency_contact_no")));
                        signUpModel.setContact_no(cursor.getString(cursor.getColumnIndex("contact_no")));
                        signUpModel.setDisability(cursor.getString(cursor.getColumnIndex("disability")));
                        signUpModel.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                        signUpModel.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                        signUpModel.setPin_code((cursor.getString(cursor.getColumnIndex("pin_code"))));
                        signUpModel.setState_id((cursor.getString(cursor.getColumnIndex("state_id"))));
                        signUpModel.setLocal_id((cursor.getInt(cursor.getColumnIndex("local_id"))));
                        signUpModel.setAadhar_no((cursor.getString(cursor.getColumnIndex("aadhar_no"))));
                        signUpModel.setCaste_id((cursor.getString(cursor.getColumnIndex("caste_id"))));
                        signUpModel.setDistrict_id((cursor.getString(cursor.getColumnIndex("district_id"))));
                        signUpModel.setBlock_id((cursor.getString(cursor.getColumnIndex("block_id"))));
                        signUpModel.setPost_office_id((cursor.getString(cursor.getColumnIndex("post_office_id"))));
                        signUpModel.setVillage_id((cursor.getString(cursor.getColumnIndex("village_id"))));
                        signUpModel.setCovered_area((cursor.getString(cursor.getColumnIndex("covered_area"))));
                        signUpModel.setBlood_group_id((cursor.getString(cursor.getColumnIndex("blood_group_id"))));
                        signUpModel.setRole_id((cursor.getString(cursor.getColumnIndex("role_id"))));
                        signUpModel.setProfile_pic((cursor.getString(cursor.getColumnIndex("profile_pic"))));
                        signUpModel.setMobile_token((cursor.getString(cursor.getColumnIndex("mobile_token"))));
                        signUpModel.setAge((cursor.getString(cursor.getColumnIndex("age"))));
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return signUpModel;
    }



    public SignUpModel SyncOFlinePatientDetail(String id) {
        SignUpModel signUpModel = new SignUpModel();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select *   from profile_patients where id = '" + id + "'and length(id)>=10";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        signUpModel = new SignUpModel();
                        signUpModel.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("id")));
                        // signUpModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        signUpModel.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                        signUpModel.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                        signUpModel.setDob(cursor.getString(cursor.getColumnIndex("dob")));
                        signUpModel.setProfile_type(cursor.getString(cursor.getColumnIndex("profile_type")));
                        signUpModel.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                        signUpModel.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        signUpModel.setEmergency_contact_no(cursor.getString(cursor.getColumnIndex("emergency_contact_no")));
                        signUpModel.setContact_no(cursor.getString(cursor.getColumnIndex("contact_no")));
                        signUpModel.setDisability(cursor.getString(cursor.getColumnIndex("disability")));
                        signUpModel.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                        signUpModel.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                        signUpModel.setPin_code((cursor.getString(cursor.getColumnIndex("pin_code"))));
                        signUpModel.setState_id((cursor.getString(cursor.getColumnIndex("state_id"))));
                        signUpModel.setLocal_id((cursor.getInt(cursor.getColumnIndex("local_id"))));
                        signUpModel.setAadhar_no((cursor.getString(cursor.getColumnIndex("aadhar_no"))));
                        signUpModel.setCaste_id((cursor.getString(cursor.getColumnIndex("caste_id"))));
                        signUpModel.setDistrict_id((cursor.getString(cursor.getColumnIndex("district_id"))));
                        signUpModel.setBlock_id((cursor.getString(cursor.getColumnIndex("block_id"))));
                        signUpModel.setPost_office_id((cursor.getString(cursor.getColumnIndex("post_office_id"))));
                        signUpModel.setVillage_id((cursor.getString(cursor.getColumnIndex("village_id"))));
                        signUpModel.setCovered_area((cursor.getString(cursor.getColumnIndex("covered_area"))));
                        signUpModel.setBlood_group_id((cursor.getString(cursor.getColumnIndex("blood_group_id"))));
                        signUpModel.setRole_id((cursor.getString(cursor.getColumnIndex("role_id"))));
                        signUpModel.setProfile_pic((cursor.getString(cursor.getColumnIndex("profile_pic"))));
                        signUpModel.setMobile_token((cursor.getString(cursor.getColumnIndex("mobile_token"))));
                        signUpModel.setAge((cursor.getString(cursor.getColumnIndex("age"))));
                        signUpModel.setUser_id(((sharedPrefHelper.getString("user_id",""))));
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return signUpModel;
    }
    public AppointmentInput SyncOFlineAppoint(String id) {
        AppointmentInput appointmentInput = new AppointmentInput();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select flag,profile_patient_id from patient_appointments where profile_patient_id = '" + id + "' and flag =0 ";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        appointmentInput = new AppointmentInput();
                        appointmentInput.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("profile_patient_id")));
                        appointmentInput.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return appointmentInput;
    }
    public ArrayList<String> getspnidDistrictData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select name from district where id=state_id";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
//                        PhcPojo phcPojo = new PhcPojo();
                        String name = cursor.getString(cursor.getColumnIndex("name"));

                        cursor.moveToNext();
                        arrayList.add(name);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return arrayList;
    }
    /*multi select spinner for village*/
    public HashMap<String, Integer> getSymptoms() {
        HashMap<String, Integer> symptomsHM = new HashMap<>();
        Symptom symptom;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from symptom";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        symptom = new Symptom();
                        symptom.setSymptom_id(cursor.getInt(cursor.getColumnIndex("id")));
                        symptom.setSymptom(cursor.getString(cursor.getColumnIndex("symptom")));
                        symptom.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
                        cursor.moveToNext();
                        symptomsHM.put(symptom.getSymptom(), symptom.getSymptom_id());
                    }
                }
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return symptomsHM;
    }

    public ArrayList<String> getspnidStateData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select name from state ";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
//                        PhcPojo phcPojo = new PhcPojo();
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        cursor.moveToNext();
                        arrayList.add(name);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return arrayList;
    }

    public HashMap<String, Integer> getDisease() {
        HashMap<String, Integer> diseaseHM = new HashMap<>();
        DiseaseInput opdDisease;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from disease";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        opdDisease = new DiseaseInput();
                        opdDisease.setId(cursor.getString(cursor.getColumnIndex("id")));
                        opdDisease.setDisease_name(cursor.getString(cursor.getColumnIndex("disease_name")));
                        cursor.moveToNext();
                        diseaseHM.put(opdDisease.getDisease_name(), Integer.valueOf(opdDisease.getId()));
                    }
                }
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }

        return diseaseHM;
    }
    public ArrayList<String> getspnidVillageData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select name from state";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
//                        PhcPojo phcPojo = new PhcPojo();
                        String name = cursor.getString(cursor.getColumnIndex("name"));

                        cursor.moveToNext();
                        arrayList.add(name);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return arrayList;
    }

    public ArrayList<String> getspnBloodGroupData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select name from blood_group ";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));

                        cursor.moveToNext();
                        arrayList.add(name);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return arrayList;
    }

    public ArrayList<String> getspnCasteData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select name from caste ";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));

                        cursor.moveToNext();
                        arrayList.add(name);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return arrayList;
    }
    public ArrayList<String> getspnidBlockData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select name from state";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
//                        PhcPojo phcPojo = new PhcPojo();
                        String name = cursor.getString(cursor.getColumnIndex("name"));

                        cursor.moveToNext();
                        arrayList.add(name);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return arrayList;
    }

    public HashMap<String, Integer> getAllState() {
        HashMap<String, Integer> state = new HashMap<>();
        State state1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor=null;
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from state";
                cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        state1 = new State();
                        state1.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        state1.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        state.put(state1.getName(), state1.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return state;
    }

    public HashMap<String, Integer> getAllDistrict(int state_id) {
        HashMap<String, Integer> district = new HashMap<>();
        District district1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor=null;
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from district where state_id=" + state_id;
                cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        district1 = new District();
                        district1.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        district1.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        district.put(district1.getName().trim(), district1.getId());
                    }
                }
            }
            sqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return district;
    }

    public void saveVideoData(IvrCallingMasking user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("caller_id", user.getCaller_id());
                values.put("reciver_id", user.getReciver_id());
                values.put("patient_appointment_id", user.getPatient_appointment_id());
                values.put("role_id", user.getRole_id());
                values.put("calling_type", user.getCalling_type());
                values.put("calling_screen", user.getCalling_screen());
                values.put("flag", 0);
                // Inserting Row
                db.insert("ivr_calling_masking", null, values);

                db.close(); // Closing database connection
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public void updateVideoData(IvrCallingMasking user, String masking_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("patient_name", user.getPatient_name());
                values.put("uid", user.getUid());
                values.put("call_status", user.getCall_status());
                values.put("start_time", user.getStart_time());
                values.put("end_time", user.getEnd_time());
                values.put("time_duration", user.getTime_duration());
                values.put("flag", 0);
                // Inserting Row

                db.update("ivr_calling_masking", values, "ivr_calling_masking_id" + " = " + masking_Id + "", null);


                db.close(); // Closing database connection
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }


    public ArrayList<IvrCallingMasking> getVideoDataForSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<IvrCallingMasking> callingMaskings = new ArrayList<>();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select patient_name, uid, call_status, start_time, end_time, time_duration, id,ivr_calling_masking_id from ivr_calling_masking where flag = 0";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        IvrCallingMasking callingMasking = new IvrCallingMasking();
                        callingMasking.setIvr_call_masking_id(cursor.getString(cursor.getColumnIndex("ivr_calling_masking_id")));
                        callingMasking.setPatient_name(cursor.getString(cursor.getColumnIndex("patient_name")));
                        callingMasking.setId(cursor.getString(cursor.getColumnIndex("id")));
                        callingMasking.setVideo_file(cursor.getString(cursor.getColumnIndex("uid")));
                        callingMasking.setCall_status(cursor.getString(cursor.getColumnIndex("call_status")));
                        callingMasking.setStart_time(cursor.getString(cursor.getColumnIndex("start_time")));
                        callingMasking.setEnd_time(cursor.getString(cursor.getColumnIndex("end_time")));
                        callingMasking.setTime_duration(cursor.getString(cursor.getColumnIndex("time_duration")));

                        cursor.moveToNext();
                        callingMaskings.add(callingMasking);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return callingMaskings;
    }


    public IvrCallingMasking getVideoData() {
        SQLiteDatabase db = this.getReadableDatabase();
        IvrCallingMasking callingMasking = null;
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select caller_id, reciver_id, patient_appointment_id, role_id, calling_type, calling_screen, id from ivr_calling_masking where flag = 0";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        callingMasking = new IvrCallingMasking();
                        callingMasking.setUser_id(cursor.getString(cursor.getColumnIndex("caller_id")));
                        callingMasking.setId(cursor.getString(cursor.getColumnIndex("id")));
                        callingMasking.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("reciver_id")));
                        callingMasking.setPatient_appointment_id(cursor.getString(cursor.getColumnIndex("patient_appointment_id")));
                        callingMasking.setRole_id(cursor.getString(cursor.getColumnIndex("role_id")));
                        callingMasking.setCalling_type(cursor.getString(cursor.getColumnIndex("calling_type")));
                        callingMasking.setCalling_screen(cursor.getString(cursor.getColumnIndex("calling_screen")));

                        cursor.moveToNext();

                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return callingMasking;
    }
    public SignUpModel syncSignUpData() {
        SignUpModel signUpModel = new SignUpModel();
        SQLiteDatabase db = this.getReadableDatabase();
Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from profile_patients Where flag = 0 and length(id)>=10";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        signUpModel = new SignUpModel();

                       signUpModel.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("id")));
                        signUpModel.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                        signUpModel.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                        signUpModel.setDob(cursor.getString(cursor.getColumnIndex("dob")));
                        signUpModel.setProfile_type(cursor.getString(cursor.getColumnIndex("profile_type")));
                        signUpModel.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                        signUpModel.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        signUpModel.setEmergency_contact_no(cursor.getString(cursor.getColumnIndex("emergency_contact_no")));
                        signUpModel.setContact_no(cursor.getString(cursor.getColumnIndex("contact_no")));
                        signUpModel.setDisability(cursor.getString(cursor.getColumnIndex("disability")));
                        signUpModel.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                        signUpModel.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                        signUpModel.setPin_code((cursor.getString(cursor.getColumnIndex("pin_code"))));
                        signUpModel.setState_id((cursor.getString(cursor.getColumnIndex("state_id"))));
                        signUpModel.setLocal_id((cursor.getInt(cursor.getColumnIndex("local_id"))));
                        signUpModel.setAadhar_no((cursor.getString(cursor.getColumnIndex("aadhar_no"))));
                        signUpModel.setCaste_id((cursor.getString(cursor.getColumnIndex("caste_id"))));
                        signUpModel.setDistrict_id((cursor.getString(cursor.getColumnIndex("district_id"))));
                        signUpModel.setBlock_id((cursor.getString(cursor.getColumnIndex("block_id"))));
                        signUpModel.setPost_office_id((cursor.getString(cursor.getColumnIndex("post_office_id"))));
                        signUpModel.setVillage_id((cursor.getString(cursor.getColumnIndex("village_id"))));
                        signUpModel.setCovered_area((cursor.getString(cursor.getColumnIndex("covered_area"))));
                        signUpModel.setBlood_group_id((cursor.getString(cursor.getColumnIndex("blood_group_id"))));
                        signUpModel.setRole_id((cursor.getString(cursor.getColumnIndex("role_id"))));
                        signUpModel.setProfile_pic((cursor.getString(cursor.getColumnIndex("profile_pic"))));
                        signUpModel.setMobile_token((cursor.getString(cursor.getColumnIndex("mobile_token"))));
                        signUpModel.setAge((cursor.getString(cursor.getColumnIndex("age"))));
                        signUpModel.setUser_id(((sharedPrefHelper.getString("user_id",""))));
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return signUpModel;
    }




    public SignUpModel syncSignUpData1(String profile_id) {
        SignUpModel signUpModel = new SignUpModel();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from profile_patients WHERE id = '" + profile_id + "'";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        signUpModel = new SignUpModel();
                        signUpModel.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("id")));
                        signUpModel.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                        signUpModel.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                        signUpModel.setDob(cursor.getString(cursor.getColumnIndex("dob")));
                        signUpModel.setProfile_type(cursor.getString(cursor.getColumnIndex("profile_type")));
                        signUpModel.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                        signUpModel.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        signUpModel.setEmergency_contact_no(cursor.getString(cursor.getColumnIndex("emergency_contact_no")));
                        signUpModel.setContact_no(cursor.getString(cursor.getColumnIndex("contact_no")));
                        signUpModel.setDisability(cursor.getString(cursor.getColumnIndex("disability")));
                        signUpModel.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                        signUpModel.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                        signUpModel.setPin_code((cursor.getString(cursor.getColumnIndex("pin_code"))));
                        signUpModel.setState_id((cursor.getString(cursor.getColumnIndex("state_id"))));
                        signUpModel.setLocal_id((cursor.getInt(cursor.getColumnIndex("local_id"))));
                        signUpModel.setAadhar_no((cursor.getString(cursor.getColumnIndex("aadhar_no"))));
                        signUpModel.setCaste_id((cursor.getString(cursor.getColumnIndex("caste_id"))));
                        signUpModel.setDistrict_id((cursor.getString(cursor.getColumnIndex("district_id"))));
                        signUpModel.setBlock_id((cursor.getString(cursor.getColumnIndex("block_id"))));
                        signUpModel.setPost_office_id((cursor.getString(cursor.getColumnIndex("post_office_id"))));
                        signUpModel.setVillage_id((cursor.getString(cursor.getColumnIndex("village_id"))));
                        signUpModel.setCovered_area((cursor.getString(cursor.getColumnIndex("covered_area"))));
                        signUpModel.setBlood_group_id((cursor.getString(cursor.getColumnIndex("blood_group_id"))));
                        signUpModel.setRole_id((cursor.getString(cursor.getColumnIndex("role_id"))));
                        signUpModel.setProfile_pic((cursor.getString(cursor.getColumnIndex("profile_pic"))));
                        signUpModel.setMobile_token((cursor.getString(cursor.getColumnIndex("mobile_token"))));
                        signUpModel.setAge((cursor.getString(cursor.getColumnIndex("age"))));
                        signUpModel.setUser_id(((sharedPrefHelper.getString("user_id",""))));
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return signUpModel;
    }




    public SignUpModel syncUpdatedSignUpData() {
        SignUpModel signUpModel = new SignUpModel();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from profile_patients Where flag = 0 and length(id)<10";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        signUpModel = new SignUpModel();

                        signUpModel.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("id")));
                        // signUpModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        signUpModel.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                        signUpModel.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                        signUpModel.setDob(cursor.getString(cursor.getColumnIndex("dob")));
                        signUpModel.setProfile_type(cursor.getString(cursor.getColumnIndex("profile_type")));
                        signUpModel.setCreated_at(cursor.getString(cursor.getColumnIndex("created_at")));
                        signUpModel.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        signUpModel.setEmergency_contact_no(cursor.getString(cursor.getColumnIndex("emergency_contact_no")));
                        signUpModel.setContact_no(cursor.getString(cursor.getColumnIndex("contact_no")));
                        signUpModel.setDisability(cursor.getString(cursor.getColumnIndex("disability")));
                        signUpModel.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                        signUpModel.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                        signUpModel.setPin_code((cursor.getString(cursor.getColumnIndex("pin_code"))));
                        signUpModel.setState_id((cursor.getString(cursor.getColumnIndex("state_id"))));
                        signUpModel.setLocal_id((cursor.getInt(cursor.getColumnIndex("local_id"))));
                        signUpModel.setAadhar_no((cursor.getString(cursor.getColumnIndex("aadhar_no"))));
                        signUpModel.setCaste_id((cursor.getString(cursor.getColumnIndex("caste_id"))));
                        signUpModel.setDistrict_id((cursor.getString(cursor.getColumnIndex("district_id"))));
                        signUpModel.setBlock_id((cursor.getString(cursor.getColumnIndex("block_id"))));
                        signUpModel.setPost_office_id((cursor.getString(cursor.getColumnIndex("post_office_id"))));
                        signUpModel.setVillage_id((cursor.getString(cursor.getColumnIndex("village_id"))));
                        signUpModel.setCovered_area((cursor.getString(cursor.getColumnIndex("covered_area"))));
                        signUpModel.setBlood_group_id((cursor.getString(cursor.getColumnIndex("blood_group_id"))));
                        signUpModel.setRole_id((cursor.getString(cursor.getColumnIndex("role_id"))));
                        signUpModel.setProfile_pic((cursor.getString(cursor.getColumnIndex("profile_pic"))));
                        signUpModel.setMobile_token((cursor.getString(cursor.getColumnIndex("mobile_token"))));
                        signUpModel.setAge((cursor.getString(cursor.getColumnIndex("age"))));
                        signUpModel.setUser_id(((sharedPrefHelper.getString("user_id",""))));
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return signUpModel;
    }


    public long updateFlag(String table, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("flag", flag);
                inserted_id = db.update(table, values, "flag" + " = '" + local_id + "'", null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public long updateFlagInTable(String table, String whr, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("flag", flag);
                inserted_id = db.update(table, values, whr + " = '" + local_id + "'", null);
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }


//    public long updateFlagInTable(String table, String whr, int local_id, int flag) {
//        long inserted_id = 0;
//        SQLiteDatabase db = this.getWritableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                ContentValues values = new ContentValues();
//                values.put("flag", flag);
//                inserted_id = db.update(table, values, whr + " = " + local_id + "", null);
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return inserted_id;
//    }

    public long updateDependentTable(String table,String Column, String whr, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put(Column, flag);
                inserted_id = db.update(table, values, whr + " = '" + local_id + "'", null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }
    public long updateServerIdTable(String table, String whr, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("ivr_calling_masking_id", flag);
                inserted_id = db.update(table, values, whr + " = " + local_id + "", null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }


    public long updateVideoPathInTable(String table, String whr, int local_id, String flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("uid", flag);
                inserted_id = db.update(table, values, whr + " = " + local_id + "", null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }


    public HashMap<String, Integer> getAllBlock(int district_id) {
        HashMap<String, Integer> block1 = new HashMap<>();
        Block block;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor=null;
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "";
                //if (sharedPrefHelper.getString("role_id","").equals("5")){
                query = "Select id, name from block where district_id = " + district_id;
                /*}else {
                    query  = "Select id, name from block where district_id = " +district_id + " and is_visual_patient = '1'";
                }*/

               cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        /*if (!sharedPrefHelper.getString("role_id", "").equals("7")
                                && !cursor.getString(cursor.getColumnIndex("name")).equals("Other")) {*/
                        block = new Block();
                        block.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        block.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        block1.put(block.getName().trim(), block.getId());
                        //}
                    }
                    /*block = new Block();
                    block.setId(000);
                    block.setName("Other");
                    block1.put(block.getName().trim(), block.getId());*/
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return block1;
    }

    public HashMap<String, Integer> getAllCaste() {
        HashMap<String, Integer> caste = new HashMap<>();
        Caste state1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor=null;
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from caste";
                cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        state1 = new Caste();
                        state1.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        state1.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        caste.put(state1.getName(), state1.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return caste;
    }
    public HashMap<String, Integer> getAllDoctor(String doctorNotAvailable) {
        HashMap<String, Integer> doctor_list = new HashMap<>();
        PatientPojo  state1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor=null;
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id,full_name from profile_doctors where id NOT IN("+doctorNotAvailable+") AND flag='N'";
                cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        state1 = new PatientPojo();
                        state1.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        state1.setFull_name(cursor.getString(cursor.getColumnIndex("full_name")));
                        cursor.moveToNext();
                        doctor_list.put(state1.getFull_name(), state1.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return doctor_list;
    }
    public HashMap<String, Integer> getAllblood_group() {
        HashMap<String, Integer> blood_group = new HashMap<>();
        Caste state1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor=null;
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from blood_group";
                cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        state1 = new Caste();
                        state1.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        state1.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        blood_group.put(state1.getName(), state1.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return blood_group;
    }

    public HashMap<String, Integer> getAllPostOffice(int block_id) {
        HashMap<String, Integer> postoffice1 = new HashMap<>();
        Postoffice postoffice;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor=null;
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "";
                //if (sharedPrefHelper.getString("role_id","").equals("5")){
                query = "Select id, name from post_office where block_id = " + block_id;
                /*}else {
                    query = "Select id, name from post_office where block_id = " +block_id+ " and is_visual_patient = '1' ";
                }*/

                cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        postoffice = new Postoffice();
                        postoffice.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        postoffice.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        postoffice1.put(postoffice.getName(), postoffice.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }

        return postoffice1;
    }

    public HashMap<String, Integer> getAllVillage(int block_id) {
        HashMap<String, Integer> village1 = new HashMap<>();
        Village village;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor=null;
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "";
                //if (sharedPrefHelper.getString("role_id","").equals("5")){
                query = "Select id, name from village where block_id = " + block_id;
                /*}else {
                    query = "Select id, name from village where post_office_id = " + post_office_id +" and is_visual_patient = '1'  ";
                }*/
                cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        /*if (!sharedPrefHelper.getString("role_id", "").equals("7")
                                && !cursor.getString(cursor.getColumnIndex("name")).equals("Other")) {*/
                        village = new Village();
                        village.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        village.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        village1.put(village.getName().trim(), village.getId());
                        //}
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }

        return village1;
    }

//    public HashMap<String, Integer> getDisease() {
//        HashMap<String, Integer> diseaseHM = new HashMap<>();
//        DiseaseInput opdDisease;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from disease";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        opdDisease = new DiseaseInput();
//                        opdDisease.setId(cursor.getString(cursor.getColumnIndex("id")));
//                        opdDisease.setDisease_name(cursor.getString(cursor.getColumnIndex("disease_name")));
//                        cursor.moveToNext();
//                        diseaseHM.put(opdDisease.getDisease_name(), Integer.valueOf(opdDisease.getId()));
//                    }
//                }
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//
//        return diseaseHM;
//    }


    public String getdisease_idFromDisease(String data) {

        String id = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select id from disease where disease_name = '" + data + "'";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        id = cursor.getString(cursor.getColumnIndex("id"));
                        cursor.moveToNext();
                    }
                }

                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return id;
    }

    public ArrayList<String> getSymptomDataList() {
        ArrayList<String> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "SELECT symptom FROM symptom";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        dataList.add(cursor.getString(cursor.getColumnIndex("symptom")));
                        cursor.moveToNext();
                    }
                }
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return dataList;
    }

    /*multi select spinner for village*/
//    public HashMap<String, Integer> getSymptoms() {
//        HashMap<String, Integer> symptomsHM = new HashMap<>();
//        Symptom symptom;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from symptom";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        symptom = new Symptom();
//                        symptom.setSymptom_id(cursor.getInt(cursor.getColumnIndex("id")));
//                        symptom.setSymptom(cursor.getString(cursor.getColumnIndex("symptom")));
//                        symptom.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
//                        cursor.moveToNext();
//                        symptomsHM.put(symptom.getSymptom(), symptom.getSymptom_id());
//                    }
//                }
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return symptomsHM;
//    }
//
//    public HashMap<String, Integer> getPrescriptionDays() {
//        HashMap<String, Integer> symptomsHM = new HashMap<>();
//        PrescriptionDays symptom;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from prescription_days";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        symptom = new PrescriptionDays();
//                        symptom.setId(cursor.getString(cursor.getColumnIndex("id")));
//                        symptom.setName(cursor.getString(cursor.getColumnIndex("name")));
//                        cursor.moveToNext();
//                        symptomsHM.put(symptom.getName(), Integer.valueOf(symptom.getId()));
//                    }
//                }
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return symptomsHM;
//    }
//    public HashMap<String, Integer> getPrescriptionInterval() {
//        HashMap<String, Integer> symptomsHM = new HashMap<>();
//        PrescriptionDays symptom;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from prescription_interval";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        symptom = new PrescriptionDays();
//                        symptom.setId(cursor.getString(cursor.getColumnIndex("id")));
//                        symptom.setName(cursor.getString(cursor.getColumnIndex("name")));
//                        cursor.moveToNext();
//                        symptomsHM.put(symptom.getName(), Integer.valueOf(symptom.getId()));
//                    }
//                }
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return symptomsHM;
//    }
//    public HashMap<String, Integer> getPrescriptionEatingSchedule() {
//        HashMap<String, Integer> symptomsHM = new HashMap<>();
//        PrescriptionDays symptom;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from prescription_eating_schedule";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        symptom = new PrescriptionDays();
//                        symptom.setId(cursor.getString(cursor.getColumnIndex("id")));
//                        symptom.setName(cursor.getString(cursor.getColumnIndex("name")));
//                        cursor.moveToNext();
//                        symptomsHM.put(symptom.getName(), Integer.valueOf(symptom.getId()));
//                    }
//                }
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return symptomsHM;
//    }
//
//    public HashMap<String, Integer> getAllTest() {
//        HashMap<String, Integer> testHM = new HashMap<>();
//        Test testModel;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select id, test_name  from test where  del_action = 'N' ";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//
//
//                    while (!cursor.isAfterLast()) {
//                        testModel = new Test();
//                        testModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
//                        testModel.setTest_name(cursor.getString(cursor.getColumnIndex("test_name")));
//                        cursor.moveToNext();
//                        testHM.put(testModel.getTest_name().trim(), testModel.getId());
//                    }
//
//                }
//                db.close();
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return testHM;
//    }
//
//    public HashMap<String, Integer> getAllSubTest() {
//        HashMap<String, Integer> subTestHashMap = new HashMap<>();
//        SubTestsModel subTestsModel;
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        try {
//            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
//                String query = "Select id, test_name from sub_tests";
//                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        subTestsModel = new SubTestsModel();
//                        subTestsModel.setTest_id(cursor.getString(cursor.getColumnIndex("id")));
//                        subTestsModel.setTest_name(cursor.getString(cursor.getColumnIndex("test_name")));
//                        cursor.moveToNext();
//                        subTestHashMap.put(subTestsModel.getTest_name().trim(), Integer.valueOf(subTestsModel.getTest_id()));
//                    }
//                }
//            }
//            sqLiteDatabase.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return subTestHashMap;
//    }

    public int getTestCategoryId(String name) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur=null;
        try {
            String query = "select id from test where test_name = '" + name + "'  ";
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                cur = db.rawQuery(query, null);
                if (cur != null && cur.getCount() > 0) {
                    cur.move(0);
                    while (cur.moveToNext()) {
                        try {
                            id = cur.getInt(cur.getColumnIndex("id"));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch(Exception ex){    }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cur != null)
                cur.close();
        }
        return id;

    }

//    public HashMap<String, Integer> getAllSubTestByTestId(int id) {
//        HashMap<String, Integer> hashMap = new HashMap<>();
//        SubTestsModel testsModel;
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        try {
//            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
//                String query = "Select id, test_name from sub_tests where test_id=" + id;
//                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        testsModel = new SubTestsModel();
//                        testsModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
//                        testsModel.setTest_name(cursor.getString(cursor.getColumnIndex("test_name")));
//                        cursor.moveToNext();
//                        hashMap.put(testsModel.getTest_name().trim(), testsModel.getId());
//                    }
//                }
//            }
//            sqLiteDatabase.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return hashMap;
//    }
//
//    /*multi select spinner for village*/
//    public HashMap<String, Integer> getVillage() {
//        HashMap<String, Integer> villagesHM = new HashMap<>();
//        Village village;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from village";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        village = new Village();
//                        village.setId(cursor.getInt(cursor.getColumnIndex("id")));
//                        village.setName(cursor.getString(cursor.getColumnIndex("name")));
//                        //village.setBlock_id(cursor.getString(cursor.getColumnIndex("block_id")));
//                        cursor.moveToNext();
//                        villagesHM.put(village.getName(), village.getId());
//                    }
//                }
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return villagesHM;
//    }

    public String getSelectedItemId(String table, String bloodGroup) {
        String id = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur=null;
        try {
            String query = "select id from '" + table + "' where name = '" + bloodGroup + "'";
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                cur = db.rawQuery(query, null);
                if (cur != null && cur.getCount() > 0) {
                    cur.move(0);
                    while (cur.moveToNext()) {
                        try {
                            id = cur.getString(cur.getColumnIndex("id"));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch(Exception ex){}
        finally {
            // this gets called even if there is an exception somewhere above
            if(cur != null)
                cur.close();
        }
        return id;
    }

    public String getUpdatedDate(String table_name) {
        String date = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur=null;
        try {
            String query = "select created_at from '" + table_name + "' order by id desc limit 0,1 ";
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                cur = db.rawQuery(query, null);
                if (cur != null && cur.getCount() > 0) {
                    cur.move(0);
                    while (cur.moveToNext()) {
                        try {
                            date = cur.getString(cur.getColumnIndex("created_at"));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch(Exception ex){}
        finally {
            // this gets called even if there is an exception somewhere above
            if(cur != null)
                cur.close();
        }
        return date;

    }

    public String getUpdatedOn(String table_name) {
        String date = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur=null;
        try {
            String query = "select created_on from '" + table_name + "' order by id desc limit 0,1 ";
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                cur = db.rawQuery(query, null);
                if (cur != null && cur.getCount() > 0) {
                    cur.move(0);
                    while (cur.moveToNext()) {
                        try {
                            date = cur.getString(cur.getColumnIndex("created_on"));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch(Exception ex){}
        finally {
            // this gets called even if there is an exception somewhere above
            if(cur != null)
                cur.close();
        }
        return date;
    }
    public String getNameById(String tableName, String colName, String whrCol, int whrId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String name = "";
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select " + colName + " from " + tableName + " where " + whrCol + " =" + whrId;
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    name = cursor.getString(cursor.getColumnIndex(colName));
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return name;
    }

    public DoctorAvailableDatePojo getDoctorNotAvailable(String currentDate) {
        DoctorAvailableDatePojo doctorAvailable = new DoctorAvailableDatePojo();
        SQLiteDatabase db = this.getReadableDatabase();
Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "SELECT COUNT(DISTINCT(doctor_id)) AS doctorNotAvailable, GROUP_CONCAT(DISTINCT(doctor_id)) AS doctor_id FROM doctor_available WHERE date_from<='"+currentDate+"' AND '"+currentDate+"'<=date_to";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        doctorAvailable.setDoctorNotAvailable(cursor.getString(cursor.getColumnIndex("doctorNotAvailable")));
                        doctorAvailable.setDoctor_id(cursor.getString(cursor.getColumnIndex("doctor_id")));
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return doctorAvailable;
    }

    public int getDoctorAvailable(String doctorId) {
        int sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            cursor = db.rawQuery("SELECT count(id) AS total FROM profile_doctors WHERE id NOT IN(" + doctorId + ") AND flag='N'", null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("total"));
        }catch(Exception ex){}
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return sum;
    }

    public ArrayList<AppointmentMedicinePrescribedModel> getMedicinePrescribed(String Id) {
        ArrayList<AppointmentMedicinePrescribedModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select patient_appointment_id,medicine_id,prescription_days_id,prescription_interval_id,prescription_eating_schedule_id,prescription_sos,quantity_by_doctor,quantity_by_chemist,doctor_updated_on,chemist_updated_on,created_by,pharmacist_id from appointment_medicine_prescribed Where patient_appointment_id='"+Id+"'";
                cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        AppointmentMedicinePrescribedModel medicinePrescribedModel = new AppointmentMedicinePrescribedModel();
                        medicinePrescribedModel.setPatient_appointment_id(cursor.getString(cursor.getColumnIndex("patient_appointment_id")));
                        medicinePrescribedModel.setMedicine_id(cursor.getInt(cursor.getColumnIndex("medicine_id")));
                        medicinePrescribedModel.setPrescription_days_id(cursor.getInt(cursor.getColumnIndex("prescription_days_id")));
                        medicinePrescribedModel.setPrescription_interval_id(cursor.getInt(cursor.getColumnIndex("prescription_interval_id")));
                        medicinePrescribedModel.setPrescription_eating_schedule_id(cursor.getInt(cursor.getColumnIndex("prescription_eating_schedule_id")));
                        medicinePrescribedModel.setPrescription_sos(cursor.getString(cursor.getColumnIndex("prescription_sos")));
                        medicinePrescribedModel.setQuantity_by_doctor(cursor.getInt(cursor.getColumnIndex("quantity_by_doctor")));
                        medicinePrescribedModel.setQuantity_by_chemist(cursor.getInt(cursor.getColumnIndex("quantity_by_chemist")));
                        medicinePrescribedModel.setDoctor_updated_on(cursor.getString(cursor.getColumnIndex("doctor_updated_on")));
                        medicinePrescribedModel.setChemist_updated_on(cursor.getString(cursor.getColumnIndex("chemist_updated_on")));
                        medicinePrescribedModel.setCreated_by(cursor.getInt(cursor.getColumnIndex("created_by")));
                        medicinePrescribedModel.setPharmacist_id(cursor.getInt(cursor.getColumnIndex("pharmacist_id")));

                        arrayList.add(medicinePrescribedModel);
                        cursor.moveToNext();
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return arrayList;
    }

    public String getSymptomName(String id) {
        String sum = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            cursor = db.rawQuery("select symptom from symptom where id in (" + id + ") ", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int count = 0;
                while (!cursor.isAfterLast()) {
                    if (count == 0) {
                        sum = cursor.getString(cursor.getColumnIndex("symptom"));
                        count++;
                    } else {
                        sum = sum + "," + cursor.getString(cursor.getColumnIndex("symptom"));
                    }
                    cursor.moveToNext();
                }
            }
        }catch(Exception ex){}
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return sum;
    }

    public int getTotalListCount() {
        int sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        try {
            cursor = db.rawQuery("SELECT count(local_id) as total from profile_patients", null);
            if (cursor.moveToFirst())
                sum = cursor.getInt(cursor.getColumnIndex("total"));
        }catch(Exception ex){}
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }
        return sum;
    }
}

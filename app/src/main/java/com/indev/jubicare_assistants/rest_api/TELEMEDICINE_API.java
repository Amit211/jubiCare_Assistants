package com.indev.jubicare_assistants.rest_api;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface TELEMEDICINE_API {
    @POST("login.php")
    Call<JsonObject> login(@Body RequestBody body);
    @POST("new_login.php")
    Call<JsonObject> newLogin(@Body RequestBody body);

    @POST("download_prescription.php")
    Call<JsonObject> downloadPrescription(@Body RequestBody body);

    @POST("download_appointment_medicine_prescribed.php")
    Call<JsonObject> downloadAppointmentMedicinePrescription(@Body RequestBody body);
    @POST("signup.php")
    Call<JsonObject> sendSignupData(@Body RequestBody sign);
    @POST("download_general_new.php")
    Call<JsonObject> download_Data(@Body RequestBody sign);
    @POST("download_doctors.php")
    Call<JsonObject> doctor_Data(@Body RequestBody sign);
    @POST("download_doctor_available.php")
    Call<JsonObject> download_doctor_available(@Body RequestBody sign);
    @POST("profile_patient_list.php")
    Call<JsonObject> searchData(@Body RequestBody body);
    @POST("patient_appointments_new.php")
    Call<JsonObject> sendAppointdata(@Body RequestBody body);
    @POST("master_table_download.php")
    Call<JsonObject> getMasterTables(@Body RequestBody body);
    @POST("forget_password.php")
    Call<JsonObject> sendForgetPassword(@Body RequestBody body);
    @POST("change-password.php")
    Call<JsonObject> sendChangePassword(@Body RequestBody body);
    @POST("emergency_contact.php")
    Call<JsonObject> emergencyContactList(@Body RequestBody body);
    @POST("resend_otp.php")
    Call<JsonObject> resendOtp(@Body RequestBody body);
    @POST("profile.php")
    Call<JsonObject> getCommonProfile(@Body RequestBody body);
    @POST("doctor_calling.php")
    Call<JsonObject> getDoctorCall(@Body RequestBody body);
    @GET
    Call<JsonObject> getPinCode(@Url String PinCode);

    @POST("downloaded_notification.php")
    Call<JsonObject> getNotificationData(@Body RequestBody body);

    @POST("medicine-stock-list.php")
    Call<JsonObject> getMedicineStock(@Body RequestBody body);
    @POST("patient_search_new.php")
    Call<JsonObject> patient_search(@Body RequestBody sign);

    @POST("otp-not-generated-delivery-count.php")
    Call<JsonObject> getOtpNotGeneratedCount(@Body RequestBody body);


    @POST("logout.php")
    Call<JsonObject> callLogoutApi(@Body RequestBody body);


    @POST("edit_profile_patient.php")
    Call<JsonObject> sendEditProfileData(@Body RequestBody body);


    @POST("profile_patient_list.php")
    Call<JsonObject> patientListingApi(@Body RequestBody body);

    @POST("download_listing_doctors.php")
    Call<JsonObject> download_DoctortList(@Body RequestBody body);

    @POST("profile.php")
    Call<JsonObject> download_profile(@Body RequestBody body);


    @POST("delete_appointment.php")
    Call<JsonObject> deleteAppointmentApi(@Body RequestBody body);

    @POST("call_masking_rec.php")
    Call<JsonObject> callFromDoctorApi(@Body RequestBody body);

    @POST("get_ivrcdr_number.php")
    Call<JsonObject> callFromCounsellorFillFormApi(@Body RequestBody body);

    @POST("check_last_login.php")
    Call<JsonObject> check_last_login(@Body RequestBody body);

    @POST("patient_previous_history.php")
    Call<JsonObject> callPatientPreviousHistory(@Body RequestBody body);
    @POST("download_listing_patients.php")
    Call<JsonObject> download_patientList(@Body RequestBody body);
    @POST("doctor_assign.php")
    Call<JsonObject> sendDoctorAssignmentdata(@Body RequestBody body);
}
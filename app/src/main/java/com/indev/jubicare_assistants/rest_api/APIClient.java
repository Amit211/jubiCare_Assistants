package com.indev.jubicare_assistants.rest_api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    //testing
    /*public static final String BASE_URL = "http://telemedicine.nutrisoft.in/apiV1-17/";
    public static final String BASE_URL1 = "http://telemedicine.nutrisoft.in/jubicare/api/";
    public static final String IMAGE_URL = "http://telemedicine.nutrisoft.in/jubicare/images/patient/profile/";
    public static final String IMAGE_URL_DOC_APPO = "http://telemedicine.nutrisoft.in/admin/assets/images/appointment/";
    public static final String ADDRESS_URL = "http://www.postalpincode.in/api/pincode/";*/

    //original
    public static final String BASE_URL = "http://jubicare.nutrisoft.in/apiV1-17/";
    public static final String BASE_URL1 = "http://jubicare.nutrisoft.in/jubicare/api/";
    public static final String IMAGE_URL = "http://jubicare.nutrisoft.in/jubicare/images/patient/profile/";
    public static final String IMAGE_URL_DOC_APPO = "http://jubicare.nutrisoft.in/admin/assets/images/appointment/";
    public static final String ADDRESS_URL = "http://www.postalpincode.in/api/pincode/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .readTimeout(60000, TimeUnit.SECONDS)
                .addInterceptor(logging).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit getClientJubiCare() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .readTimeout(60000, TimeUnit.SECONDS)
                .addInterceptor(logging).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }
}

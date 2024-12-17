package com.mrahmed.todo.ApiClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * @Project: Todo
 * @Author: Md. Raju Ahmed
 * @Created at: 12/13/2024
 */
public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String baseUrl = "https://675aaf55099e3090dbe59783.mockapi.io/api/"; //For physical Device
//    private static final String baseUrl = "http://10.0.2.2:8080/"; // Android Emulator
//    private static final String baseUrl = "http://192.168.0.1:8080/"; //For physical Device

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}

package com.ex.assignment.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitControler {
    private static Retrofit retrofit;
    private static String BASE_URL = "https://api.unsplash.com/";
    public static Retrofit getCaller() {
        if(retrofit != null)
            return retrofit;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        return retrofit;

    }
}

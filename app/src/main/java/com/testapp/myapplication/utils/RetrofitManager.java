package com.testapp.myapplication.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static Retrofit retrofit = null;

    public static Retrofit getInstance() {

        if (retrofit == null) {
            synchronized (RetrofitManager.class) {
                retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com/users/JakeWharton/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }

        return retrofit;
    }

}

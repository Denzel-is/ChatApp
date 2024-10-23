package com.example.chatapp;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiManager {
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static WeatherApiManager instance;
    private WeatherApiService weatherApiService;

    private WeatherApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherApiService = retrofit.create(WeatherApiService.class);
    }

    public static synchronized WeatherApiManager getInstance() {
        if (instance == null) {
            instance = new WeatherApiManager();
        }
        return instance;
    }

    public WeatherApiService getWeatherApiService() {
        return weatherApiService;
    }
}


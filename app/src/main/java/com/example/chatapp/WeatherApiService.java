package com.example.chatapp;

import com.example.chatapp.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("weather")
    Call<WeatherResponse> getWeatherData(
            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}

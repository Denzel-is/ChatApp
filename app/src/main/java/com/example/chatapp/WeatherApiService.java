    package com.example.chatapp;

    import com.example.chatapp.WeatherResponse;
    import retrofit2.Call;
    import retrofit2.http.GET;
    import retrofit2.http.Query;

    public interface WeatherApiService {
        @GET("weather")
        Call<WeatherResponse> getWeatherDataByCity(
                @Query("q") String cityName,
                @Query("appid") String apiKey,
                @Query("units") String units
        );

        // Существующий метод для получения данных по координатам
        @GET("weather")
        Call<WeatherResponse> getWeatherData(
                @Query("lat") double lat,
                @Query("lon") double lon,
                @Query("appid") String apiKey,
                @Query("units") String units
        );
<<<<<<< HEAD
        @GET("forecast/daily")
        Call<ForecastResponse> get7DayForecast(
                @Query("lat") double lat,
                @Query("lon") double lon,
                @Query("cnt") int count,
                @Query("appid") String apiKey,
                @Query("units") String units
        );
=======

>>>>>>> origin/master
        // Call<WeatherResponse> getWeatherData(String cityName, String apiKey, String metric);
    }

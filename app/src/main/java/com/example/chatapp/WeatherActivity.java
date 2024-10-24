package com.example.chatapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    private TextView temperatureTextView, humidityTextView, weatherConditionTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String API_KEY = "f1982d317e4d16010219651fcda9c66f";
    private static final double LAT = 35.0;
    private static final double LON = 139.0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // Инициализация UI элементов
        temperatureTextView = findViewById(R.id.temperatureTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        weatherConditionTextView = findViewById(R.id.weatherConditionTextView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Загрузка данных о погоде при открытии страницы
        loadWeatherData();

        // Обновление данных при свайпе вниз
        swipeRefreshLayout.setOnRefreshListener(this::loadWeatherData);
    }

    private void loadWeatherData() {
        swipeRefreshLayout.setRefreshing(true);

        WeatherApiManager.getInstance().getWeatherApiService()
                .getWeatherData(LAT, LON, API_KEY, "metric")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateWeatherUI(response.body());
                        } else {
                            Log.e("WeatherActivity", "Response unsuccessful");
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        Log.e("WeatherActivity", "API request failed", t);
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(WeatherActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateWeatherUI(WeatherResponse weatherData) {
        double temp = weatherData.getMain().getTemp();
        String condition = weatherData.getWeather().get(0).getDescription();
        int humidity = weatherData.getMain().getHumidity();

        temperatureTextView.setText(String.format(Locale.getDefault(), "%.1f°C", temp));
        humidityTextView.setText(String.format(Locale.getDefault(), "Humidity: %d%%", humidity));
        weatherConditionTextView.setText(condition);
    }
}

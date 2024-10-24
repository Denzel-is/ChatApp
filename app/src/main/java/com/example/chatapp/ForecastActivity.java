package com.example.chatapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Locale;

public class ForecastActivity extends AppCompatActivity {

    private TextView forecastTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String API_KEY = "f1982d317e4d16010219651fcda9c66f";
    private static final double LAT = 51.1694; // Координаты Астаны
    private static final double LON = 71.4491;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        forecastTextView = findViewById(R.id.forecastTextView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Загрузка данных о прогнозе погоды при открытии страницы
        loadForecastData();

        // Обновление данных при свайпе вниз
        swipeRefreshLayout.setOnRefreshListener(this::loadForecastData);
    }

    private void loadForecastData() {
        swipeRefreshLayout.setRefreshing(true);

        WeatherApiManager.getInstance().getWeatherApiService()
                .get7DayForecast(LAT, LON, 7, API_KEY, "metric")
                .enqueue(new Callback<ForecastResponse>() {
                    @Override
                    public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateForecastUI(response.body());
                        } else {
                            Log.e("ForecastActivity", "Response unsuccessful");
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<ForecastResponse> call, Throwable t) {
                        Log.e("ForecastActivity", "API request failed", t);
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(ForecastActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateForecastUI(ForecastResponse forecastData) {
        StringBuilder forecastInfo = new StringBuilder();

        List<ForecastResponse.Daily> dailyForecasts = forecastData.getDaily();
        for (ForecastResponse.Daily day : dailyForecasts) {
            double tempDay = day.getTemp().getDay();
            String weatherDescription = day.getWeather().get(0).getDescription();
            forecastInfo.append(String.format(Locale.getDefault(), "%.1f°C - %s\n", tempDay, weatherDescription));
        }

        forecastTextView.setText(forecastInfo.toString());
    }
}

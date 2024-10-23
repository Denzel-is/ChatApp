package com.example.chatapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentWeatherFragment extends Fragment {
    private TextView temperatureText, humidityText, conditionText;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_weather, container, false);

        temperatureText = rootView.findViewById(R.id.temperatureText);
        humidityText = rootView.findViewById(R.id.humidityText);
        conditionText = rootView.findViewById(R.id.conditionText);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(this::fetchWeatherData);

        fetchWeatherData(); // Загружаем погоду при запуске

        return rootView;
    }

    private void fetchWeatherData() {
        double lat = 51.5074; // Пример широты (Лондон)
        double lon = -0.1278; // Пример долготы (Лондон)
        String apiKey = getString(R.string.weather_api_key);

        WeatherApiManager.getInstance().getWeatherApiService()
                .getWeatherData(lat, lon, apiKey, "metric")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            WeatherResponse weather = response.body();
                            temperatureText.setText("Температура: " + weather.getMain().getTemp() + "°C");
                            humidityText.setText("Влажность: " + weather.getMain().getHumidity() + "%");
                            conditionText.setText("Условия: " + weather.getWeather().get(0).getDescription());
                        } else {
                            Toast.makeText(getContext(), "Не удалось получить данные", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false); // Останавливаем refresh
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Ошибка сети", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false); // Останавливаем refresh
                    }
                });
    }
}

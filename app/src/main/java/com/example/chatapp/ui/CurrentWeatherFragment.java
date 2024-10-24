package com.example.chatapp.ui;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.chatapp.R;
import com.example.chatapp.WeatherApiService;
import com.example.chatapp.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Locale;

public class CurrentWeatherFragment extends Fragment {

    private static final String API_KEY = "f1982d317e4d16010219651fcda9c66f";
    private static final String CITY_NAME = "Astana";

    private TextView temperatureTextView, weatherConditionTextView, humidityTextView, pressureTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);

        // Инициализируем UI-компоненты
        temperatureTextView = view.findViewById(R.id.temperatureTextView);
        weatherConditionTextView = view.findViewById(R.id.weatherConditionTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        // Обновляем данные при свайпе вниз
        swipeRefreshLayout.setOnRefreshListener(() -> fetchWeatherData(CITY_NAME, API_KEY));

        // Загружаем погоду при создании фрагмента
        fetchWeatherData(CITY_NAME, API_KEY);

        return view;
    }

    private void fetchWeatherData(String cityName, String apiKey) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService weatherService = retrofit.create(WeatherApiService.class);

        Call<WeatherResponse> call = weatherService.getWeatherDataByCity(cityName, apiKey, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateWeatherUI(response.body());
                } else {
                    Log.e("CurrentWeatherFragment", "Response unsuccessful");
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("CurrentWeatherFragment", "API request failed", t);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateWeatherUI(WeatherResponse weatherData) {
        double temp = weatherData.getMain().getTemp();
        String condition = weatherData.getWeather().get(0).getDescription();
        int humidity = weatherData.getMain().getHumidity();
        int pressure = weatherData.getMain().getPressure();

        temperatureTextView.setText(String.format(Locale.getDefault(), "%.1f°C", temp));
        weatherConditionTextView.setText(condition);
        humidityTextView.setText(String.format(Locale.getDefault(), "Humidity: %d%%", humidity));
        pressureTextView.setText(String.format(Locale.getDefault(), "Pressure: %d hPa", pressure));
    }
}

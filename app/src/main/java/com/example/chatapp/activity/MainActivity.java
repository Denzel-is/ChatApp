package com.example.chatapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chatapp.ForecastFragment;
import com.example.chatapp.R;
import com.example.chatapp.WeatherApiManager;
import com.example.chatapp.WeatherResponse;
import com.example.chatapp.ui.CurrentWeatherFragment;
import com.example.chatapp.ui.SettingsFragment;
import com.example.chatapp.ui.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "f1982d317e4d16010219651fcda9c66f";
    private static final String CITY_NAME = "Astana";

    private TextView temperatureTextView, weatherConditionTextView, humidityTextView, pressureTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        // Проверяем авторизацию пользователя
        if (!isUserLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Инициализируем UI-компоненты
        temperatureTextView = findViewById(R.id.temperatureTextView);
        weatherConditionTextView = findViewById(R.id.weatherConditionTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        pressureTextView = findViewById(R.id.pressureTextView);

        // Обновляем погоду при запуске
        fetchWeatherData(CITY_NAME, API_KEY);


        swipeRefreshLayout.setOnRefreshListener(() -> fetchWeatherData(CITY_NAME, API_KEY));

        // Инициализация BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Загружаем основной фрагмент по умолчанию
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new HomeFragment())
                    .commit();

        }
    }


    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("ChatAppPrefs", MODE_PRIVATE);
        return prefs.getBoolean("isLoggedIn", false);
    }


    private void fetchWeatherData(String cityName, String apiKey) {
        WeatherApiManager.getInstance().getWeatherApiService()
                .getWeatherDataByCity(cityName, apiKey, "metric")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateWeatherUI(response.body());
                        } else {
                            Log.e("MainActivity", "Response unsuccessful");
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        Log.e("MainActivity", "API request failed", t);
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


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_weather) {
                selectedFragment = new CurrentWeatherFragment();
            } else if (itemId == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            }
            else if (itemId == R.id.nav_forecast) {
                selectedFragment = new ForecastFragment();
            }
            else {
                Log.e("MainActivity", "Неизвестный элемент меню с id: " + itemId);
                return false;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
                return true;
            } else {
                Log.e("MainActivity", "Ошибка инициализации фрагмента");
                return false;
            }
        }
    };
}

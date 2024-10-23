package com.example.chatapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chatapp.R;
import com.example.chatapp.WeatherActivity;
import com.example.chatapp.WeatherApiService;
import com.example.chatapp.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "f1982d317e4d16010219651fcda9c66f";
    private static final String CITY_NAME = "Astana";  // Здесь укажи нужный город

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);  // Убедись, что у тебя есть соответствующий layout

        // Проверяем, залогинен ли пользователь
        if (isUserLoggedIn()) {
            // Если залогинен — делаем запрос к API погоды
            fetchWeatherData(CITY_NAME, API_KEY);
        } else {
            // Если не залогинен — переходим в LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Завершаем MainActivity
        }
    }

    // Проверяем авторизацию с помощью SharedPreferences
    private boolean isUserLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("ChatAppPrefs", MODE_PRIVATE);
        return prefs.getBoolean("isLoggedIn", false);
    }

    // Выполняем запрос к OpenWeather API
    private void fetchWeatherData(String cityName, String apiKey) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService weatherService = retrofit.create(WeatherApiService.class);

        Call<WeatherResponse> call = weatherService.getWeatherData(cityName, apiKey, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherData = response.body();

                    if (weatherData != null) {
                        // Извлекаем данные из ответа
                        double temperature = weatherData.getMain().getTemp();
                        String weatherCondition = weatherData.getWeather().get(0).getDescription();
                        int humidity = weatherData.getMain().getHumidity();
                        int pressure = weatherData.getMain().getPressure();
                        double windSpeed = weatherData.getWind().getSpeed();

                        // Сохраняем данные погоды для дальнейшего использования
                        saveWeatherDataToPrefs(temperature, weatherCondition, humidity, pressure, windSpeed);

                        // Обновляем UI
                        updateWeatherUI(temperature, weatherCondition, humidity, pressure, windSpeed);

                        // Переходим на страницу с погодой
                        Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Log.e("MainActivity", "Response not successful");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("MainActivity", "Error fetching weather data", t);
            }
        });
    }

    // Обновляем UI данными погоды
    private void updateWeatherUI(double temperature, String condition, int humidity, int pressure, double windSpeed) {
        TextView temperatureTextView = findViewById(R.id.temperatureTextView);
        TextView weatherConditionTextView = findViewById(R.id.weatherConditionTextView);
        TextView humidityTextView = findViewById(R.id.humidityTextView);
        TextView pressureTextView = findViewById(R.id.pressureTextView);

        // Обновляем текстовые поля с данными погоды
        temperatureTextView.setText(String.format(Locale.getDefault(), "%.1f°C", temperature));
        weatherConditionTextView.setText(condition);
        humidityTextView.setText(String.format(Locale.getDefault(), "Humidity: %d%%", humidity));
        pressureTextView.setText(String.format(Locale.getDefault(), "Pressure: %d hPa", pressure));
    }

    // Сохраняем данные погоды в SharedPreferences
    private void saveWeatherDataToPrefs(double temperature, String condition, int humidity, int pressure, double windSpeed) {
        SharedPreferences prefs = getSharedPreferences("WeatherPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("temperature", (float) temperature);
        editor.putString("condition", condition);
        editor.putInt("humidity", humidity);
        editor.putInt("pressure", pressure);
        editor.putFloat("windSpeed", (float) windSpeed);
        editor.apply();
    }
}

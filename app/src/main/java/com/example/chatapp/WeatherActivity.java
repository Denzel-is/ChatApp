package com.example.chatapp;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.chatapp.R;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {

    private TextView temperatureTextView, humidityTextView, weatherConditionTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    // Вставьте свой API-ключ OpenWeatherMap
    private final String API_KEY = "f1982d317e4d16010219651fcda9c66f";
    private final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=" + API_KEY + "&units=metric";  // Пример запроса

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
        swipeRefreshLayout.setRefreshing(true);  // Включаем индикатор обновления

        new Thread(() -> {
            try {
                URL url = new URL(WEATHER_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);

                // Чтение ответа
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Парсинг ответа
                parseWeatherData(response.toString());

            } catch (Exception e) {
                runOnUiThread(() -> {
                    swipeRefreshLayout.setRefreshing(false);  // Отключаем индикатор обновления
                    Toast.makeText(WeatherActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                });
                e.printStackTrace();
            }
        }).start();
    }

    private void parseWeatherData(@NonNull String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);

            // Получаем данные из JSON
            JSONObject main = jsonResponse.getJSONObject("main");
            double temperature = main.getDouble("temp");
            int humidity = main.getInt("humidity");

            JSONObject weather = jsonResponse.getJSONArray("weather").getJSONObject(0);
            String weatherCondition = weather.getString("description");

            // Обновляем UI
            runOnUiThread(() -> {
                temperatureTextView.setText(getString(R.string.temperature_format, temperature));
                humidityTextView.setText(getString(R.string.humidity_format, humidity));
                weatherConditionTextView.setText(weatherCondition);
                swipeRefreshLayout.setRefreshing(false);  // Отключаем индикатор обновления
            });

        } catch (Exception e) {
            runOnUiThread(() -> {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(WeatherActivity.this, "Ошибка обработки данных", Toast.LENGTH_SHORT).show();
            });
            e.printStackTrace();
        }
    }
}

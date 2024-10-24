package com.example.chatapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastResponse {

    @SerializedName("daily")
    private List<Daily> daily;

    public List<Daily> getDaily() {
        return daily;
    }

    public class Daily {
        @SerializedName("temp")
        private Temp temp;

        @SerializedName("weather")
        private List<Weather> weather;

        public Temp getTemp() {
            return temp;
        }

        public List<Weather> getWeather() {
            return weather;
        }
    }

    public class Temp {
        @SerializedName("day")
        private double day;

        public double getDay() {
            return day;
        }
    }

    public class Weather {
        @SerializedName("description")
        private String description;

        public String getDescription() {
            return description;
        }
    }
}

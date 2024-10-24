package com.example.chatapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherForecastResponse {

    @SerializedName("list")
    private List<DayForecast> list;

    public List<DayForecast> getList() {
        return list;
    }

    public class DayForecast {
        @SerializedName("dt")
        private long date;

        @SerializedName("temp")
        private Temperature temp;

        @SerializedName("weather")
        private List<Weather> weather;

        public long getDate() {
            return date;
        }

        public Temperature getTemp() {
            return temp;
        }

        public List<Weather> getWeather() {
            return weather;
        }
    }

    public class Temperature {
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

package com.example.chatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<ForecastResponse.Daily> forecastData = new ArrayList<>();

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastResponse.Daily dailyForecast = forecastData.get(position);
        holder.bind(dailyForecast);
    }

    @Override
    public int getItemCount() {
        return forecastData.size();
    }

    public void setForecastData(List<ForecastResponse.Daily> forecastList) {
        this.forecastData = forecastList;
        this.forecastData.clear(); // Очистка предыдущих данных
        this.forecastData.addAll(forecastList); // Добавление новых данных
        notifyDataSetChanged(); // Уведомление адаптера об изменениях
        notifyDataSetChanged(); // Обновляем адаптер при изменении данных
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        private TextView temperatureTextView;
        private TextView weatherDescriptionTextView;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
            weatherDescriptionTextView = itemView.findViewById(R.id.weatherDescriptionTextView);
        }

        public void bind(ForecastResponse.Daily dailyForecast) {
            String temperature = String.format("%.1f°C", dailyForecast.getTemp().getDay());
            String description = dailyForecast.getWeather().get(0).getDescription();

            temperatureTextView.setText(temperature);
            weatherDescriptionTextView.setText(description);
        }
    }
}

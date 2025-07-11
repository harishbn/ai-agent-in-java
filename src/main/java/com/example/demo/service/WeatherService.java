package com.example.demo.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    public WeatherResponse getWeather(WeatherRequest request) {
        String location = request.getLocation();
        if("paris".equalsIgnoreCase(location))
            return new WeatherResponse("rainy", 12, "Do not forget to carry umbrella");

        return new WeatherResponse("sunny", 25, "Carry sunscreen");
    }

    @Data
    @AllArgsConstructor
    public static class WeatherResponse {
        public String weather;
        public int temperature;
        public String advice;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WeatherRequest {
        public String location;
    }
} 
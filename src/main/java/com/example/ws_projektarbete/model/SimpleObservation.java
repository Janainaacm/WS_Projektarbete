package com.example.ws_projektarbete.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleObservation {

    @JsonProperty("city_name")
    private String cityName;

    @JsonProperty("temp")
    private Double temperature;

    @JsonProperty("weather_description")
    private String weatherDescription;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }
}

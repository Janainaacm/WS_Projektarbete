package com.example.ws_projektarbete.service;

import com.example.ws_projektarbete.model.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class WeatherReportService {

    private final RestClient restClient;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherReportService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Weather getWeatherReportByCity(String city) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("key", apiKey)
                            .queryParam("city", city)
                            .build())
                    .retrieve()
                    .body(Weather.class);
        } catch (RestClientResponseException e) {

            System.err.println("Error: " + e.getRawStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        }
    }
}

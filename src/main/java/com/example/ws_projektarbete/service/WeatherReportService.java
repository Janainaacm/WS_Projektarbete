package com.example.ws_projektarbete.service;

import com.example.ws_projektarbete.model.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.io.EOFException;

@Service
public class WeatherReportService {

    private final RestClient restClient;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherReportService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Weather getWeatherReportByCity(String city) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", apiKey)
                        .queryParam("city", city)
                        .build())
                .retrieve()
                .onStatus(
                        status -> status.value() == 404, (request, response) -> {
                            throw new RuntimeException(

                            );
                        })
                .body(Weather.class);
    }
}

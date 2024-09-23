package com.example.ws_projektarbete.controller;

import com.example.ws_projektarbete.model.Weather;
import com.example.ws_projektarbete.service.WeatherReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    WeatherReportService weatherReportService;

    public UserController(WeatherReportService weatherReportService) {
        this.weatherReportService = weatherReportService;
    }

    @GetMapping("/weather/{city}")
    public Weather getWeatherReportByCity(@PathVariable ("city") String city){
        return  weatherReportService.getWeatherReportByCity(city);
    }
}

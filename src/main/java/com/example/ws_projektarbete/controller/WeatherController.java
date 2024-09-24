package com.example.ws_projektarbete.controller;

import com.example.ws_projektarbete.model.WeatherCity;
import com.example.ws_projektarbete.repository.City;
import com.example.ws_projektarbete.model.Weather;
import com.example.ws_projektarbete.repository.CityRepository;
import com.example.ws_projektarbete.service.WeatherReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WeatherController {

    private final CityRepository cityRepository;
    private final WeatherReportService weatherReportService;

    public WeatherController(CityRepository cityRepository, WeatherReportService weatherReportService) {
        this.cityRepository = cityRepository;
        this.weatherReportService = weatherReportService;
    }

    @GetMapping("/weather/{city}")
    public ResponseEntity<Weather> getWeatherReportByCity(@PathVariable("city") String city) {
        var response = weatherReportService.getWeatherReportByCity(city);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/weather/cities")
    public ResponseEntity<List<WeatherCity>> listCities() {
        List<City> cities = cityRepository.findAll();
        List<WeatherCity> response = new ArrayList<>();
        if (!cities.isEmpty()) {
            cities.stream().forEach(
                    city -> {
                        var weather = weatherReportService.getWeatherReportByCity(city.getName());
                        if (weather != null) {
                            response.add(new WeatherCity(city.getName(), weather));
                        }
                    }
            );
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/add-city")
    public String addCity(@RequestParam String cityName) {
        City city = new City(cityName);
        cityRepository.save(city);
        return "redirect:/cities";
    }


    @DeleteMapping("/delete-city/{cityName}")
    public ResponseEntity<Void> deleteCity(@PathVariable("cityName") String cityName) {
        cityRepository.deleteByCityName(cityName);
        return ResponseEntity.ok().build();
    }

}

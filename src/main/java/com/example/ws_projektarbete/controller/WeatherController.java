package com.example.ws_projektarbete.controller;

import com.example.ws_projektarbete.exception.CityNotFoundException;
import com.example.ws_projektarbete.model.WeatherCity;
import com.example.ws_projektarbete.repository.City;
import com.example.ws_projektarbete.model.Weather;
import com.example.ws_projektarbete.repository.CityRepository;
import com.example.ws_projektarbete.service.WeatherReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class WeatherController {

    private final CityRepository cityRepository;
    private final WeatherReportService weatherReportService;

    public WeatherController(CityRepository cityRepository, WeatherReportService weatherReportService) {
        this.cityRepository = cityRepository;
        this.weatherReportService = weatherReportService;
    }

    @PutMapping("/weather/{city}")
    public ResponseEntity<Weather> getWeatherReportByCity(@PathVariable("city") String city) {
        try {
            var response = weatherReportService.getWeatherReportByCity(city);
            if (response == null) {
                throw new CityNotFoundException("Weather data for city: " + city + " not found");
            }
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            throw new CityNotFoundException("City " + city + " not found");
        }
    }

    @GetMapping("/weather/cities")
    public ResponseEntity<List<WeatherCity>> listCities() {
        try {
            List<City> cities = cityRepository.findAll();
            List<WeatherCity> response = new ArrayList<>();
            if (!cities.isEmpty()) {
                cities.stream().forEach(city -> {
                    var weather = weatherReportService.getWeatherReportByCity(city.getName());
                    if (weather != null) {
                        response.add(new WeatherCity(city.getName(), weather));
                    }
                });
            }
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add-city")
    public ResponseEntity<String> addCity(@RequestParam String cityName) {
        try {
            Optional<City> existingCity = cityRepository.findByName(cityName);
            if (existingCity.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("City already exists");
            }

            City city = new City(cityName);
            cityRepository.save(city);
            return ResponseEntity.ok("City added successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while adding city");
        }
    }

    @DeleteMapping("/delete-city/{cityName}")
    public ResponseEntity<Void> deleteCity(@PathVariable("cityName") String cityName) {
        try {
            Optional<City> city = cityRepository.findByName(cityName);
            if (!city.isPresent()) {
                throw new CityNotFoundException("City " + cityName + " not found");
            }

            cityRepository.deleteByCityName(cityName);
            return ResponseEntity.ok().build();
        } catch (CityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

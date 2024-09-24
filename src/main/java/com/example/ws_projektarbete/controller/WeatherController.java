package com.example.ws_projektarbete.controller;

import com.example.ws_projektarbete.exception.CityNotFoundException;
import com.example.ws_projektarbete.model.*;
import com.example.ws_projektarbete.repository.City;
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


    @GetMapping("/weather/{city}")
    public ResponseEntity<?> getWeatherReport(
            @PathVariable("city") String city,
            @RequestParam(value = "detailed", required = false, defaultValue = "true") boolean detailed) {

        Weather weather = weatherReportService.getWeatherReportByCity(city);

        if (weather == null || weather.getData() == null || weather.getData().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (detailed) {
            return ResponseEntity.ok(weather);
        } else {
            List<SimpleObservation> simpleObservations = weather.getData().stream()
                    .map(observation -> {
                        SimpleObservation simpleObservation = new SimpleObservation();
                        simpleObservation.setCityName(observation.getCityName());

                        if (observation.getTemperature() != null) {
                            simpleObservation.setTemperature(observation.getTemperature());
                        }

                        WeatherCondition weatherCondition = observation.getWeather();
                        if (weatherCondition != null && weatherCondition.getDescription() != null) {
                            simpleObservation.setWeatherDescription(weatherCondition.getDescription());
                        }

                        return simpleObservation;
                    })
                    .filter(obs -> obs.getCityName() != null || obs.getTemperature() != null || obs.getWeatherDescription() != null)
                    .toList();

            SimpleWeather simpleWeather = new SimpleWeather();
            simpleWeather.setData(simpleObservations);

            return ResponseEntity.ok(simpleWeather);
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
    public ResponseEntity<String> deleteCity(@PathVariable("cityName") String cityName) {
        try {
            Optional<City> city = cityRepository.findByName(cityName);
            if (!city.isPresent()) {
                throw new CityNotFoundException("City " + cityName + " not found");
            }

            cityRepository.deleteByCityName(cityName);
            return ResponseEntity.ok("City deleted successfully");
        } catch (CityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

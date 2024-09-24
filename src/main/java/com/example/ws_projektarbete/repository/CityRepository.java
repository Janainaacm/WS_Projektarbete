package com.example.ws_projektarbete.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {

    @Modifying
    @Transactional
    @Query(value = "delete from city where name = :name", nativeQuery = true)
    void deleteByCityName(String name);

    Optional<City> findByName(String cityName);
}


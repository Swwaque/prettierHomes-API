package com.ph.controller;

import com.ph.payload.response.CityResponse;
import com.ph.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    //Not:U05 GetAllCities() *************************************************************************
    // http://localhost:8080/cities
    @GetMapping("/all")
    public List<CityResponse> getAllCities() {
        return cityService.getAllCities();
    }

    //Not:U02 GetAllCitiesByCountryId() *************************************************************************
    // http://localhost:8080/cities
    @GetMapping("/search/{countryId}")
    public List<CityResponse> getAllCitiesByCountryId(@PathVariable Long countryId) {
        return cityService.getAllCitiesByCountryId(countryId);
    }
}

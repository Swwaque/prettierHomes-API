package com.ph.controller;

import com.ph.payload.response.CountryResponse;
import com.ph.service.CountriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountriesController {

    private final CountriesService countriesService;

    //Not:U01 GetAllCountries() *************************************************************************
    // http://localhost:8080/countries
    @GetMapping("/all")
    public List<CountryResponse> getAllCountries() {
        return countriesService.getAllCountries();
    }
}

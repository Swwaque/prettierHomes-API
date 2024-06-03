package com.ph.service;

import com.ph.domain.entities.City;

import com.ph.exception.customs.ResourceNotFoundException;
import com.ph.payload.mapper.LocationMapper;
import com.ph.payload.response.CityResponse;
import com.ph.repository.CityRepository;
import com.ph.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final MessageUtil messageUtil;
    private final LocationMapper locationMapper;

    //Not:U05 GetAllCities() *************************************************************************

    /**
     * Retrieves all cities.
     *
     * @return a list of CityResponse objects representing all cities.
     */
    public List<CityResponse> getAllCities() {
        // Retrieve all cities from the cityRepository
        return cityRepository.findAll().stream().map(locationMapper::toCityResponse).collect(Collectors.toList());
    }

    //Not:U02 GetAllCitiesByCountryId() *************************************************************************
//     Retrieves all cities by country ID.
//     @param countryId The ID of the country.
//     @return a list of CityResponse objects representing all cities.
    @Cacheable(value = "city", key = "#countryId")
    public List<CityResponse> getAllCitiesByCountryId(Long countryId) {
        // Retrieve all cities from the cityRepository based on the country ID
        return cityRepository.findByCountry_Id(countryId).stream().map(locationMapper::toCityResponse).collect(Collectors.toList());
    }

    // Not : GetById() ***************************************************************************************

    /**
     * Retrieve a City object by its ID.
     *
     * @param id The ID of the City to retrieve.
     * @return The City object with the specified ID.
     * @throws ResourceNotFoundException if the City with the specified ID is not found.
     */

    public City getById(Long id) {
        return cityRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.city.not-found")));
    }

}

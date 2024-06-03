package com.ph.service;

import com.ph.domain.entities.Country;
import com.ph.exception.customs.ResourceNotFoundException;
import com.ph.payload.mapper.LocationMapper;
import com.ph.payload.response.CountryResponse;
import com.ph.repository.CountryRepository;
import com.ph.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountriesService {
    private final CountryRepository countriesRepository;
    private final MessageUtil messageUtil;
    private final LocationMapper locationMapper;

    //Not:U01 GetAllCountries() *************************************************************************

    /**
     * Retrieves a list of all countries.
     *
     * @return the list of country responses
     */
    @Cacheable(value = "country")
    public List<CountryResponse> getAllCountries() {
        return countriesRepository.findAll().stream().map(locationMapper::toCountryResponse).collect(Collectors.toList());
    }

    // Not : GetById() ***************************************************************************************

    /**
     * Retrieves a country by its ID.
     *
     * @param id The ID of the country.
     * @return The country with the specified ID.
     * @throws ResourceNotFoundException if no country is found with the given ID.
     */

    public Country getById(Long id) {
        return countriesRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.country.not-found")));
    }
}

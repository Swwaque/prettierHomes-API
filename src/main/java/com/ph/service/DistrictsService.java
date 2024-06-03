package com.ph.service;

import com.ph.domain.entities.District;
import com.ph.exception.customs.ResourceNotFoundException;
import com.ph.payload.mapper.LocationMapper;
import com.ph.payload.response.DistrictResponse;
import com.ph.repository.DistrictsRepository;
import com.ph.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistrictsService {
    private final DistrictsRepository districtsRepository;
    private final MessageUtil messageUtil;
    private final LocationMapper locationMapper;


    //Not:U03 GetAllDistricts() *************************************************************************

    /**
     * Retrieves all districts.
     *
     * @return a list of DistrictResponse objects representing all districts
     */

    public List<DistrictResponse> getAllDistricts() {
        return districtsRepository.findAll()
                .stream()
                .map(locationMapper::toDistrictResponse)
                .collect(Collectors.toList());
    }
    //Not:U03 GetAllDistrictsByCityId() *************************************************************************

    /**
     * Retrieves all districts by city ID.
     *
     * @param cityId The ID of the city.
     * @return A list of district responses.
     */
    @Cacheable(value = "district", key = "#cityId")
    public List<DistrictResponse> getAllDistrictsByCityId(Long cityId) {
        return districtsRepository.findByCity_Id(cityId)
                .stream()
                .map(locationMapper::toDistrictResponse)
                .collect(Collectors.toList());
    }

    // Not : GetById() ***************************************************************************************

    /**
     * Get a district by its ID.
     *
     * @param id The ID of the district to retrieve.
     * @return The district with the specified ID.
     * @throws ResourceNotFoundException if the district with the specified ID is not found.
     */

    public District getById(Long id) {
        return districtsRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.district.not-found")));
    }
}

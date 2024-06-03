package com.ph.controller;

import com.ph.payload.response.DistrictResponse;
import com.ph.service.DistrictsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/districts")
public class DistrictsController {

    private final DistrictsService districtsService;

    //Not:U04 GetAllDistricts() *************************************************************************
    // http://localhost:8080/districts
    @GetMapping("/all")
    public List<DistrictResponse> getAllDistricts() {
        return districtsService.getAllDistricts();
    }

    //Not:U03 GetAllDistrictsByCityId() *************************************************************************
    @GetMapping("/search/{cityId}")
    public List<DistrictResponse> getAllDistrictsByCityId(@PathVariable Long cityId) {
        return districtsService.getAllDistrictsByCityId(cityId);
    }

}

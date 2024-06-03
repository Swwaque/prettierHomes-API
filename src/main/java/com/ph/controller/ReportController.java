package com.ph.controller;

import com.ph.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    // Not: get most popular properties
    @GetMapping("/most-popular")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> getProperties(@RequestParam(name = "amount") Integer amount) {
        //http://localhost:8080/report/most-popular?amount=10
        return reportService.getMostPopularProperties(amount);
    }

    // Not: get  advert
    @GetMapping("adverts")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> getAdverts(
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "type", required = false) Long typeId,
            @RequestParam(name = "category", required = false) Long categoryId
    ) {
        return reportService.getAdverts(startDate, endDate, status, typeId, categoryId);
    }


    // Not: get all
    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> getStatistics() { //http://localhost:8080/report
        return reportService.getStatistics();
    }


    // Not: get all users
    @GetMapping("users")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> getUsersByRole(@RequestParam String role) { //http://localhost:8080/report/users
        return reportService.getUsersByRole(role);
    }


    // Not: get all tour request
    @GetMapping("tour-requests")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> getTourRequests(
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "status", required = false) Integer status
    ) { //http://localhost:8080/report
        return reportService.getTourRequests(startDate, endDate, status);
    }


}

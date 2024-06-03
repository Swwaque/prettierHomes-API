package com.ph.controller;

import com.ph.domain.entities.DailyReport;
import com.ph.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/daily-report")
public class DailyReportController {

    private final SchedulerService schedulerService;


    @GetMapping
    public Map<String, Map<LocalDate, Integer>> getDailyReport() {
        return schedulerService.getDailyReport();
    }
}

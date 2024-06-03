package com.ph.controller;


import com.ph.service.ResetDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class ResetDatabaseController {

    private final  ResetDatabaseService resetDatabaseService;

    //Not: ResetDatabase ************************************************* X01
    @DeleteMapping("/db-reset")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> resetDatabase() {
        return resetDatabaseService.resetDatabase();
    }

}

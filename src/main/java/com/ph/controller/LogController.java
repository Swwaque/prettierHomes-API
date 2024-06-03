package com.ph.controller;

import com.ph.payload.response.LogResponse;
import com.ph.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    public ResponseEntity<Page<LogResponse>> getLogs(@PathVariable Long id,
             @RequestParam(value = "page", defaultValue = "0", required = false) int page,
             @RequestParam(value = "size", defaultValue = "20", required = false) int size,
             @RequestParam(value = "sort", defaultValue = "message", required = false) String sort,
             @RequestParam(value = "type", defaultValue = "asc", required = false) String type

    ) {

        return ResponseEntity.ok(logService.getLogs(id,page,sort,size,type));
    }
}
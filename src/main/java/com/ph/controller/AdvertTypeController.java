package com.ph.controller;

import com.ph.payload.request.AdvertTypeRequest;
import com.ph.payload.response.AdvertTypeResponse;
import com.ph.service.AdvertTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("advert-types")
@RequiredArgsConstructor
public class AdvertTypeController {

    private final AdvertTypeService service;

    // Create new AdvertType method.
    @PostMapping
    // @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<AdvertTypeResponse> create(@RequestBody @Valid AdvertTypeRequest request) {
        return service.create(request);
    }

    // Get AdvertType by id method.
    @GetMapping("{id}")
    // @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<AdvertTypeResponse> get(@PathVariable Long id) {
        return service.get(id);
    }

    // Get All AdvertType method.
    @GetMapping("/all")
    public ResponseEntity<List<AdvertTypeResponse>> getAll() {
        return service.getAll();
    }

    // Delete AdvertType method.
    @DeleteMapping("{id}")
    //@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<AdvertTypeResponse> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    // Update AdvertType method.
    @PutMapping("{id}")
    // @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<AdvertTypeResponse> update(@PathVariable Long id, @Valid @RequestBody AdvertTypeRequest request) {
        return service.update(id, request);
    }

}

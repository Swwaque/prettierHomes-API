package com.ph.controller;

import com.ph.payload.request.CategoryPropertyKeyRequest;
import com.ph.payload.response.CategoryPropertyKeyResponse;
import com.ph.service.CategoryPropertyKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoriesKey")
@RequiredArgsConstructor
public class CategoryPropertyKeyController {

    private final CategoryPropertyKeyService propertyKeyService;

    //Not: save() ************************************************************C08
    @PostMapping("/{categoryId}/properties") // http://localhost:8080/categoriesKey/1/properties
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<CategoryPropertyKeyResponse> save(
            @Valid @RequestBody CategoryPropertyKeyRequest propertyKeyRequest,
            @PathVariable Long categoryId
    ) {
        return propertyKeyService.save(propertyKeyRequest, categoryId);
    }

    // Not: getPropertyKeysOfCategory() *************************************** C07
    @GetMapping("/{categoryId}/properties") // http://localhost:8080/categoriesKey/1/properties
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'CUSTOMER')")
    public ResponseEntity<List<CategoryPropertyKeyResponse>> getPropertyKeysOfCategory(@PathVariable Long categoryId) {
        //return ResponseEntity.ok(propertyKeyService.getPropertyKeysOfCategory(categoryId));
        return propertyKeyService.getPropertyKeysOfCategory(categoryId);
    }


    // Not: deletePropertyKey() *************************************** C10
    @DeleteMapping("/properties/{propertyId}") // http://localhost:8080/categoriesKey/properties/1
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<CategoryPropertyKeyResponse> deletePropertyKey(@PathVariable Long propertyId) {
        return propertyKeyService.deletePropertyKey(propertyId);
    }

    // Not: updatePropertyKey() *************************************** C09
    @PutMapping("/properties/{propertyId}") // http://localhost:8080/categoriesKey/properties/1
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<CategoryPropertyKeyResponse> updatePropertyKey(
            @PathVariable Long propertyId,
            @Valid @RequestBody CategoryPropertyKeyRequest propertyKeyRequest
    ) {

        return propertyKeyService.updatePropertyKey(propertyId, propertyKeyRequest);
    }

}

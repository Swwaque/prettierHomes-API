package com.ph.controller;

import com.ph.payload.request.CategoryRequest;
import com.ph.payload.response.CategoryResponse;
import com.ph.payload.response.CategoryWithoutPropertiesResponse;
import com.ph.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    //NOT: saveCategory **************************************************C04
    @PostMapping("/save") // http://localhost:8080/categories
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<?> save(@Valid @RequestBody CategoryRequest categoryRequest) {
        return categoryService.save(categoryRequest);
    }

    //NOT: getAllCategoryWithPage **************************************************C01
    //    @GetMapping()  //http://localhost:8080/categories?page=0&size=10&sort=id&type=asc
    //    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'CUSTOMER')")
    //    public Page<CategoryResponse> getAllCategoryWithPage(
    //            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
    //            @RequestParam(value = "size", defaultValue = "10", required = false ) int size,
    //            @RequestParam(value = "sort", defaultValue = "id", required = false) String sort,
    //            @RequestParam(value = "type", defaultValue = "asc",required = false) String type
    //    ) {
    //        return categoryService.getAllCategoryWithPage(page, size, sort, type);
    //    }

    //NOT: getAllCategoryWithList **************************************************C01 WithList
    @GetMapping("/all") //http://localhost:8080/categories
    public ResponseEntity<List<CategoryWithoutPropertiesResponse>> getAllCategory() {
        return categoryService.getAllCategory();
    }

    //NOT: getAllCategoryWithPageAndWithAdmin **************************************************C02
    @GetMapping("/admin")  // http://localhost:8080/categories/admin?page=0&size=10&sort=id&type=asc
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public Page<CategoryWithoutPropertiesResponse> getAllCategoryWithPageAndWithAdmin(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "seq", required = false) String sort,
            @RequestParam(value = "type", defaultValue = "asc", required = false) String type,
            @RequestParam(value = "query", required = false) String query
    ) {
        return categoryService.getAllCategoryWithPageAndWithAdmin(page, size, sort, type, query);
    }

    //NOT: deleteCategory **************************************************C06
    @DeleteMapping("/delete/{categoryId}") //http://localhost:8080/categories/1
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    //NOT: getById **************************************************C03
    @GetMapping("/one/{categoryId}") //http://localhost:8080/categories/1
    //@PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'CUSTOMER')")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long categoryId) {
        return categoryService.getById(categoryId);
    }


    //NOT: updateById **************************************************C05
    @PutMapping("update/{categoryId}") //http://localhost:8080/categories/1
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<?> updateById(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) {
        return categoryService.updateById(categoryId, categoryRequest);
    }

}

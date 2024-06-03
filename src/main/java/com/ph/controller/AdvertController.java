package com.ph.controller;

import com.ph.exception.customs.ValuesNotMatchException;
import com.ph.payload.response.*;
import com.ph.payload.request.AdvertRequest;
import com.ph.payload.request.AdvertRequestForUpdateByAdmin;
import com.ph.payload.request.AdvertRequestForUpdateByCustomer;
import com.ph.service.AdvertService;
import com.ph.utils.MessageUtil;
import com.ph.utils.ValidationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/adverts")
@RequiredArgsConstructor
public class AdvertController {

    private final AdvertService service;
    private final MessageUtil messageUtil;


    // NOT:A01 / getByAdminPage() ************************************************************
    @GetMapping("/search")
    public Page<SimpleAdvertResponse> getByAnonymusPage(
            @RequestParam(value = "q", defaultValue = "", required = false) String query,
            @RequestParam(value = "c", required = false) Long categoryId,
            @RequestParam(value = "at", required = false) Long advertTypeId,
            @RequestParam(value = "ps", required = false) Integer priceStart,
            @RequestParam(value = "pe", required = false) Integer priceEnd,
            @RequestParam(value = "ctry", required = false) Long country,
            @RequestParam(value = "city", required = false) Long city,
            @RequestParam(value = "dist", required = false) Long district,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "sort", defaultValue = "category.id") String sort,
            @RequestParam(value = "type", defaultValue = "asc") String type
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (type.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return service.getForAnyms(
                query,
                categoryId,
                advertTypeId,
                priceStart,
                priceEnd,
                country,
                city,
                district,
                pageable
        );
    }

    // NOT:A02 / getAdvertsByCities() ************************************************************
    @GetMapping("/cities/{limit}")
    public List<AdvertCityResponse> getAdvertsByCities(@PathVariable() Integer limit) {
        return service.getAdvertsByCities(limit);
    }


    // NOT:A03 / getAdvertsByCategories() ************************************************************
    @GetMapping("/categories")
    public List<AdvertCategoryResponse> getAdvertsByCategories() {
        return service.getAdvertsByCategories();
    }


    // NOT:A04 / getMostPopularAdverts() ************************************************************
    @GetMapping("/popular/{type}/{amount}")
    public List<SimpleAdvertResponse> getMostPopularAdverts(@PathVariable String type, @PathVariable Integer amount) {
        return service.getMostPopularAdverts(type,amount);
    }


    // NOT:A05 / getForCustomerById() ************************************************************
    @GetMapping("/auth")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Page<DetailedAdvertResponse> getByCustomerPage(

            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "category_id", required = false) String sort,
            @RequestParam(value = "type", defaultValue = "asc", required = false) String type,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return service.getByCustomerPage(page, size, sort, type, userDetails);
    }


    // NOT:A06 / getByAdminPage() ************************************************************
    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public Page<DetailedAdvertResponse> getByAdminPage(
            @RequestParam(value = "q", defaultValue = "", required = false) String query,
            @RequestParam(value = "category_id", required = false) Long categoryId,
            @RequestParam(value = "advert_type_id", required = false) Long advertTypeId,
            @RequestParam(value = "price_start", required = false) Integer priceStart,
            @RequestParam(value = "price_end", required = false) Integer priceEnd,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "sort", defaultValue = "category.id") String sort,
            @RequestParam(value = "type", defaultValue = "asc") String type
    ) {
        // Validate price range
        if (priceStart != null && priceEnd != null && priceStart > priceEnd) {
            throw new ValuesNotMatchException(messageUtil.getMessage("error.advert.price.start.gt.price.end"));
        }

        // Use ascending or descending order based on the sorting type
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (type.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return service.getForAdmin(
                query,
                categoryId,
                advertTypeId,
                priceStart,
                priceEnd,
                status,
                pageable
        );
    }


    // NOT:A07 / getBySlug() ************************************************************
    @GetMapping("/details/{slug}")
    public ResponseEntity<DetailedAdvertResponse> getBySlug(@PathVariable String slug ,
                                                            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return service.getBySlug(slug, userDetails);
    }


    // NOT:A08 / getForCustomer() ************************************************************
    @GetMapping("/{id}/auth")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<DetailedAdvertResponse> getByCustomer(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return service.getByCustomer(id, userDetails);
    }


    // NOT:A09 / getForAdmin() ************************************************************
    @GetMapping("/{id}/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<DetailedAdvertResponse> getByAdmin(@PathVariable Long id,  @AuthenticationPrincipal UserDetails userDetails) {
        return service.getByAdmin(id,userDetails);
    }


    // NOT:A10 / save() ************************************************************
    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<DetailedAdvertResponse> create(
            @RequestParam(value = "advert") String advert,
            @RequestParam(value = "images") List<MultipartFile> images,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        AdvertRequest request = ValidationUtil.convertAndValidate(advert, AdvertRequest.class);
        return service.create(request, images, userDetails);
    }


    // NOT:A11 / updateForCustomer() ************************************************************

    @PutMapping("/auth/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<DetailedAdvertResponse> updateForCustomer(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid AdvertRequestForUpdateByCustomer request
    ) {
        return service.updateForCustomer(id, request, userDetails);
    }

    // NOT:A12 / updateForAdmin() ************************************************************

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<DetailedAdvertResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid AdvertRequestForUpdateByAdmin request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return service.update(id, request, userDetails);
    }


    // NOT:A13 / delete() ************************************************************
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN', 'MANAGER')") //CUSTOMER EKLENDİ
    public String delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return service.delete(id, userDetails);
    }
    // Not: getAllAdvertsByUserId
    @GetMapping("/getAll/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    public ResponseEntity<Page<DetailedAdvertResponse>> getAllAdvertsByUserId(@PathVariable Long id,
                                                     @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                     @RequestParam(value = "size", defaultValue = "20", required = false) int size,
                                                     @RequestParam(value = "sort", defaultValue = "id", required = false) String sort,
                                                     @RequestParam(value = "type", defaultValue = "asc", required = false) String type

    ) {

        return ResponseEntity.ok(service.getAllAdvertsByUserId(id,page,sort,size,type));
    }


    // NOT:A06 / getByAdminPage() ************************************************************ (ESKİ VE EKSİK OLAN)

    //            @GetMapping("/adverts/admin")
    //    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    //    public Page<AdvertResponse> getByAdminPage(
    //            @RequestParam(value = "query", defaultValue = "" ) String query,
    //            @PageableDefault(sort = "category_id", direction = Sort.Direction.DESC, page = 0, size = 20) Pageable pageable) {
    //        return service.getByAdminPage(pageable,query);
    //            }


}

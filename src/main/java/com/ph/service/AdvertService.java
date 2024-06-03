package com.ph.service;

import com.ph.domain.entities.Advert;
import com.ph.domain.entities.AdvertType;
import com.ph.domain.entities.CategoryPropertyKey;
import com.ph.domain.entities.User;
import com.ph.domain.enums.StatusForAdvert;
import com.ph.domain.entities.*;
import com.ph.exception.customs.BuiltInFieldException;
import com.ph.exception.customs.NonDeletableException;
import com.ph.exception.customs.ResourceNotFoundException;
import com.ph.payload.request.abstracts.AdvertRequestAbs;
import com.ph.payload.response.*;
import com.ph.payload.mapper.AdvertMapper;
import com.ph.payload.request.AdvertRequest;
import com.ph.payload.request.AdvertRequestForUpdateByAdmin;
import com.ph.payload.request.AdvertRequestForUpdateByCustomer;
import com.ph.repository.*;
import com.ph.utils.GeneralUtils;
import com.ph.utils.MessageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdvertService {

    private final AdvertRepository repository;
    private final AdvertTypeService typeService;
    private final AdvertMapper mapper;
    private final ImageService imageService;
    private final CategoryRepository categoryRepository;
    private final CategoryPropertyValueService propertyValueService;
    private final CityService cityService;
    private final DistrictsService districtService;
    private final CountriesService countriesService;
    private final LogService logService;
    private final MessageUtil messageUtil;

    private void checkPrice(Integer priceStart, Integer priceEnd) {
        // Check if the start price is greater than the end price
        if (priceStart != null && priceEnd != null && priceStart > priceEnd) {
            throw new ResourceNotFoundException(messageUtil.getMessage("error.advert.price.start.gt.price.end"));
        }
    }

    private void setAdvertField(Advert advert, AdvertRequestAbs requestAbs) {
        AdvertType type = typeService.getById(requestAbs.getAdvertTypeId());
        Country country = countriesService.getById(requestAbs.getCountryId());
        City city = cityService.getById(requestAbs.getCityId());
        District district = districtService.getById(requestAbs.getDistrictId());
        Category category = categoryRepository.findById(requestAbs.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(messageUtil.getMessage("error.categories.not.found"), requestAbs.getCategoryId())));
        advert.setCategory(category);
        advert.setAdvertType(type);
        advert.setCountry(country);
        advert.setCity(city);
        advert.setDistrict(district);

    }


    // NOT:  helperMethodGetById() ************************************************************

    /**
     * Retrieves an Advert by its ID.
     *
     * @param id The ID of the Advert to retrieve
     * @return The Advert with the specified ID
     * @throws ResourceNotFoundException If no Advert is found with the specified ID
     */
    public Advert getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(messageUtil.getMessage("error.advert.not.found"), id)));
    }


    // NOT:A01 / getForAnonymus() ************************************************************

    /**
     * Retrieves a page of AdvertResponse objects based on the provided parameters.
     *
     * @param query        The search query string.
     * @param categoryId   The ID of the category to filter by.
     * @param advertTypeId The ID of the advert type to filter by.
     * @param priceStart   The starting price range to filter by.
     * @param priceEnd     The ending price range to filter by.
     * @param country      The status to filter by.
     * @param city         The status to filter by.
     * @param district     The status to filter by.
     * @param pageable     The pageable object containing pagination information.
     * @return A page of AdvertResponse objects.
     * @throws ResourceNotFoundException If the start price is greater than the end price.
     */

    public Page<SimpleAdvertResponse> getForAnyms(
            String query, Long categoryId, Long advertTypeId,
            Integer priceStart, Integer priceEnd,
            Long country, Long city, Long district,
            Pageable pageable
    ) throws ResourceNotFoundException {

        checkPrice(priceStart, priceEnd);

        // Retrieve the Advert entities based on the provided parameters and map them to AdvertResponse objects
        return repository.findForAnyms(query, categoryId, advertTypeId, priceStart, priceEnd, StatusForAdvert.ACTIVATED, country, city, district, pageable)
                .map(mapper::toSimpleAdvertResponse);
    }


    // NOT:A02 / getAdvertsByCities() ************************************************************

    /**
     * Retrieves a list of AdvertCityDTO objects by cities.
     *
     * @return The list of AdvertCityDTO objects.
     */
    @Cacheable(value = "advertsByCities")
    public List<AdvertCityResponse> getAdvertsByCities(Integer limit) {
        return repository.getAdvertsByCities(limit);
    }

    /**
     * Retrieves a list of AdvertCategoryDTO objects by categories.
     *
     * @return the list of AdvertCategoryDTO objects
     */
    @Cacheable(value = "advertsByCategories")
    // NOT:A03 / getAdvertsByCategories() ************************************************************
    public List<AdvertCategoryResponse> getAdvertsByCategories() {
        return repository.getAdvertsByCategories();
    }


    // NOT:A04 / getMostPopularAdverts() ************************************************************

    /**
     * Retrieves the most popular adverts.
     *
     * @param amount The number of popular adverts to retrieve.
     * @return A list of AdvertResponse objects representing the most popular adverts.
     */

    @Cacheable(value = "mostPopularAdverts", key = "#type + ' ' + #amount")
    public List<SimpleAdvertResponse> getMostPopularAdverts(String type,Integer amount) {

        // Set the default amount if null
        if (amount == null) {
            amount = 10;
        }

        // Retrieve the most popular adverts from the repository
      String newType =  type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
        List<Advert> popularAdverts =  repository.findPopularSaleOrRentAdverts(amount, newType);
        // Convert the Advert objects to AdvertResponse objects
        return popularAdverts.stream()
                .map(mapper::toSimpleAdvertResponse)
                .collect(Collectors.toList());
    }

    // NOT:A05 / getForCustomerById() ************************************************************

    /**
     * Retrieves a page of AdvertResponse objects based on the specified criteria.
     *
     * @param page        The page number to retrieve.
     * @param size        The number of items per page.
     * @param sort        The sort field.
     * @param type        The sort order (either "asc" or "desc").
     * @param userDetails The UserDetails of the user making the request.
     * @return A Page object containing AdvertResponse objects.
     */
    public Page<DetailedAdvertResponse> getByCustomerPage(int page, int size, String sort, String type, UserDetails userDetails) {

        User user = (User) userDetails;

        // Create a Pageable object with the specified page, size, and sort field in ascending order
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        // If the sort order is "desc", update the Pageable object to sort in descending order
        if (type.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        // Retrieve the page of Advert objects for the specified user ID and map them to AdvertResponse objects
        return repository.findByUser_Id(user.getId(), pageable).map(mapper::toDetailedAdvertResponse);
    }

    // NOT:A06 / getByAdminPage() ************************************************************

    /**
     * Retrieves a page of AdvertResponse objects for the admin.
     *
     * @param query        The search query string.
     * @param categoryId   The ID of the category.
     * @param advertTypeId The ID of the advert type.
     * @param priceStart   The starting price.
     * @param priceEnd     The ending price.
     * @param status       The status of the advert.
     * @param pageable     The pagination information.
     * @return A page of AdvertResponse objects.
     * @throws ResourceNotFoundException if priceStart is greater than priceEnd.
     */
    public Page<DetailedAdvertResponse> getForAdmin(
            String query, Long categoryId, Long advertTypeId,
            Integer priceStart, Integer priceEnd,
            Integer status, Pageable pageable
    ) {

        // Check if priceStart is greater than priceEnd
        checkPrice(priceStart, priceEnd);

        // Map status to StatusForAdvert enum
        StatusForAdvert statusForAdvert = null;
        if (status != null) {
            switch (status) {
                case 1 -> statusForAdvert = StatusForAdvert.PENDING;
                case 2 -> statusForAdvert = StatusForAdvert.ACTIVATED;
                case 3 -> statusForAdvert = StatusForAdvert.REJECTED;
            }
        }

        // Retrieve the page of AdvertResponse objects using the repository
        return repository.findForAdmin(query, categoryId, advertTypeId, priceStart, priceEnd, statusForAdvert, pageable)
                .map(mapper::toDetailedAdvertResponse);
    }


    // NOT:A07 / getBySlug() ************************************************************

    /**
     * Retrieves an advert by its slug.
     *
     * @param slug The slug of the advert to retrieve.
     * @return The response entity containing the advert response.
     * @throws ResourceNotFoundException If the advert is not found.
     */
    public ResponseEntity<DetailedAdvertResponse> getBySlug(String slug, UserDetails userDetails) {
        User user = (User) userDetails;
        // Find the advert by slug in the repository
        Advert advert = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(messageUtil.getMessage("error.advert.not.found.slug"), slug)));

        // Increment the view count of the advert
        if (user != null && !user.getId().equals(advert.getUser().getId())) {
            advert.setViewCount(advert.getViewCount() + 1);
        }

        // Save the updated advert in the repository
        Advert savedAdvert = repository.save(advert);

        // Convert the saved advert to a response object
        DetailedAdvertResponse response = mapper.toDetailedAdvertResponse(savedAdvert);

        // Return the response entity with the advert response
        return ResponseEntity.ok(response);
    }


    // NOT:A08 / getForCustomer() ************************************************************

    /**
     * Retrieves an Advert by customer ID.
     *
     * @param id          the ID of the Advert to retrieve
     * @param userDetails the user details of the customer
     * @return the ResponseEntity containing the Advert response
     * @throws ResourceNotFoundException if the Advert is not found or the user does not have permission
     */
    public ResponseEntity<DetailedAdvertResponse> getByCustomer(Long id, UserDetails userDetails) {

        User user = (User) userDetails;

        // Retrieve the list of adverts for the user
        List<Advert> adverts = repository.findByUser_Id(user.getId());

        // Check if the user has permission to access the specified Advert
        if (adverts.stream().noneMatch(adv -> adv.getId().equals(id))) {
            throw new ResourceNotFoundException(messageUtil.getMessage("error.advert.permission"));
        }

        // Increment the view count of the Advert
        Advert advert = getById(id);
        if (user != null && !user.getId().equals(advert.getUser().getId())) {
            advert.setViewCount(advert.getViewCount() + 1);
        }

        // Save the updated Advert
        Advert savedAdvert = repository.save(advert);

        // Return the ResponseEntity with the Advert response
        return ResponseEntity.ok(mapper.toDetailedAdvertResponse(savedAdvert));
    }


    // NOT:A09 / getForAdmin() ************************************************************

    /**
     * Retrieves an Advert by its ID for an admin user.
     * Increments the view count of the advert and saves the updated advert.
     * Returns a ResponseEntity with the response body containing the saved advert.
     *
     * @param id The ID of the advert to retrieve.
     * @return A ResponseEntity with the response body containing the saved advert.
     */
    public ResponseEntity<DetailedAdvertResponse> getByAdmin(Long id, UserDetails userDetails) {
        User user = (User) userDetails;
        // Retrieve the advert by its ID
        Advert advert = getById(id);

        // Increment the view count of the advert
        if (user != null && !user.getId().equals(advert.getUser().getId())) {
            advert.setViewCount(advert.getViewCount() + 1);
        }

        // Save the updated advert
        Advert savedAdvert = repository.save(advert);

        // Return a ResponseEntity with the response body containing the saved advert
        return ResponseEntity.ok(mapper.toDetailedAdvertResponse(savedAdvert));
    }


    // NOT:A10 / save() ************************************************************

    /**
     * Creates a new advert based on the given request and user details.
     *
     * @param request     The advert request containing the necessary data.
     * @param userDetails The user details of the user creating the advert.
     * @return The response entity containing the created advert.
     * @throws ResourceNotFoundException If the category is not found by id.
     */
    public ResponseEntity<DetailedAdvertResponse> create(AdvertRequest request, List<MultipartFile> images, UserDetails userDetails) throws ResourceNotFoundException {
        // Get the user from the user details
        User user = (User) userDetails;

        Advert advert = mapper.toEntity(request);
        setAdvertField(advert, request);
        advert.setUser(user);

        // Save the advert to the repository
        Advert savedAdvert = repository.save(advert);

        // Get the category property keys and property values from the request
        List<CategoryPropertyKey> propertyIds = advert.getCategory().getCategoryPropertyKeys();
        List<String> valuesOfProperty = request.getPropertyValues();

        // Save the property values for the advert
        for (int i = 0; i < propertyIds.size(); i++) {
            propertyValueService.saveValue(propertyIds.get(i), valuesOfProperty.get(i), savedAdvert);
        }

        // Save the images for the advert
        imageService.createImage(images, savedAdvert.getId());


        // Set the slug for the advert and save it again
        savedAdvert.setSlug(GeneralUtils.generateSlug(advert.getTitle()));
        repository.save(savedAdvert);

        // Log the creation of the advert
        logService.logMessage("Advert created by: " + user.getUsername(), savedAdvert, user);

        // Return the response entity with the created advert
        return ResponseEntity.ok(mapper.toResponseForSave(savedAdvert));
    }


    // NOT:A11 / updateForCustomer() ************************************************************

    /**
     * Updates an advert for a customer.
     *
     * @param id          The ID of the advert to update.
     * @param request     The request containing the updated advert data.
     * @param userDetails The details of the authenticated user.
     * @return The response entity containing the updated advert.
     * @throws BuiltInFieldException     If the advert is a built-in advert and cannot be updated.
     * @throws NonDeletableException     If the user does not have permission to update the advert.
     * @throws ResourceNotFoundException If the category specified in the request does not exist.
     */
    public ResponseEntity<DetailedAdvertResponse> updateForCustomer(Long id, AdvertRequestForUpdateByCustomer request, UserDetails userDetails) {

        Advert advert = getById(id);
        boolean categorySame=advert.getCategory().getId().equals(request.getCategoryId());

        // Check if the advert is a built-in advert
        if (advert.isBuiltIn()) {
            throw new BuiltInFieldException(messageUtil.getMessage("error.advert.builtin"));
        }

        User user = (User) userDetails;
        List<Advert> adverts = repository.findByUser_Id(user.getId());

        // Check if the user has permission to update the advert
        if (adverts.stream().noneMatch(adv -> adv.getId().equals(id))) {
            throw new NonDeletableException(messageUtil.getMessage("error.advert.permission"));
        }

        // Get the advert type, country, city, district, and category specified in the request


        // Update the advert with the new data
        mapper.toEntityForUpdateCustomer(advert, request);
        setAdvertField(advert, request);
        advert.setStatusForAdvert(StatusForAdvert.PENDING);
        advert.setSlug(GeneralUtils.generateSlug(advert.getTitle()));


        // Update the property values of the advert
        List<CategoryPropertyKey> propertyKeys = advert.getCategory().getCategoryPropertyKeys();
        List<String> valuesOfProperty = request.getPropertyValues();


        if(!categorySame)  {
            propertyValueService.deleteValue(advert.getCategoryPropertyValues());
            List<CategoryPropertyValue> propertyValueList = new ArrayList<>();
            for (int i = 0; i < propertyKeys.size(); i++) {
                var categoryPropertyValue =propertyValueService.saveValue(propertyKeys.get(i), valuesOfProperty.get(i), advert);
                propertyValueList.add(categoryPropertyValue);
            }
            advert.setCategoryPropertyValues(propertyValueList);
        }
        else {
            List<Long> propertyValuesIds = advert.getCategoryPropertyValues().stream()
                    .map(CategoryPropertyValue::getId)
                    .sorted()
                    .toList();
            for (int i = 0; i < propertyKeys.size(); i++) {
                    propertyValueService.updateValue(valuesOfProperty.get(i), propertyValuesIds.get(i));
                }
            }

        Advert savedAdvert = repository.save(advert);
        // Log the update event
        logService.logMessage("Advert updated by :" + user.getUsername(), savedAdvert, user);
        DetailedAdvertResponse detailedAdvertResponse = mapper.toDetailedAdvertResponse(savedAdvert);
        return ResponseEntity.ok(detailedAdvertResponse);
    }


    // NOT:A12 / updateForAdmin() ************************************************************

    /**
     * Updates an advert with the given ID using the provided request data.
     *
     * @param id          The ID of the advert to update.
     * @param request     The request data for the update.
     * @param userDetails The details of the user performing the update.
     * @return The response entity containing the updated advert.
     * @throws BuiltInFieldException     If the advert is a built-in field.
     * @throws ResourceNotFoundException If the category is not found by ID.
     */
    public ResponseEntity<DetailedAdvertResponse> update(
            Long id,
            AdvertRequestForUpdateByAdmin request,
            UserDetails userDetails
    ) {

        User user = (User) userDetails;

        // Get the advert by ID
        Advert advert = getById(id);
        boolean categorySame=advert.getCategory().getId().equals(request.getCategoryId());

        // Check if advert is built-in
        if (advert.isBuiltIn()) {
            throw new BuiltInFieldException(messageUtil.getMessage("error.advert.builtin"));
        }

        // Get the advert type, country, city, district, and category by ID
        setAdvertField(advert, request);
        // Update the advert with the request data
        mapper.toEntityForUpdate(advert, request);

        // Set the advert type, country, city, district, category, and slug

        advert.setSlug(GeneralUtils.generateSlug(advert.getTitle()));

        // Save the updated advert

        // Update the property values of the advert
        List<CategoryPropertyKey> propertyKeys = advert.getCategory().getCategoryPropertyKeys();
        List<String> valuesOfProperty = request.getPropertyValues();


        if(!categorySame)  {
            propertyValueService.deleteValue(advert.getCategoryPropertyValues());
            List<CategoryPropertyValue> propertyValueList = new ArrayList<>();
            for (int i = 0; i < propertyKeys.size(); i++) {
                var categoryPropertyValue =propertyValueService.saveValue(propertyKeys.get(i), valuesOfProperty.get(i), advert);
                propertyValueList.add(categoryPropertyValue);
            }
            advert.setCategoryPropertyValues(propertyValueList);
        }
        else {
            List<Long> propertyValuesIds = advert.getCategoryPropertyValues().stream()
                    .map(CategoryPropertyValue::getId)
                    .sorted()
                    .toList();
            for (int i = 0; i < propertyKeys.size(); i++) {
                propertyValueService.updateValue(valuesOfProperty.get(i), propertyValuesIds.get(i));
            }
        }

        Advert updatedAdvert = repository.save(advert);
        // Log the update
        logService.logMessage("Advert updated by: " + user.getUsername(), updatedAdvert, user);

        // Return the response entity with the updated advert
        return ResponseEntity.ok(mapper.toDetailedAdvertResponse(updatedAdvert));
    }


    // NOT:A13 / delete() ************************************************************

    /**
     * Deletes an advert by its ID.
     *
     * @param id          the ID of the advert to be deleted
     * @param userDetails the details of the user performing the deletion
     * @return a message indicating the success of the deletion
     * @throws BuiltInFieldException if the advert is a built-in field and cannot be deleted
     */

    public String delete(Long id, UserDetails userDetails) {

        User user = (User) userDetails;

        // Retrieve the advert by its ID
        Advert advert = getById(id);

        // Check if the advert is a built-in field
        if (advert.isBuiltIn()) {
            throw new BuiltInFieldException(messageUtil.getMessage("error.advert.builtin"));
        }

        // Log the deletion of the advert
        logService.logMessage("Advert deleted by: " + user.getUsername(), advert, user);

        // Delete the advert
        repository.delete(advert);

        return "Advert deleted successfully. Title: " + advert.getTitle();
    }


    // NOT:A06 / getByAdminPage() ************************************************************ (ESKİ VE EKSİK OLAN)
    //   public Page<AdvertResponse> getByAdminPage(Pageable pageable,String query) {
    //        return
    //         repository.searchAdvertByPage(query,pageable).map(mapper::toResponse);
    //    }


    /**
     * !!!
     * This method created for getting category property keys of specific category !!! for category service
     *
     * @param categoryId : represent category's id
     * @return : List of category property key
     */
    public List<Advert> getAdvertsOfCategory(Long categoryId) {

        return repository.findByCategory_Id(categoryId);

    }




    /**
     * Retrieves all adverts associated with a specific user ID.
     *
     * @param userId The ID of the user.
     * @return A list of adverts associated with the specified user ID.
     */
    public List<Advert> getAllAdvertsByUserId(Long userId) {
        return repository.findByUser_Id(userId);
    }


    // Not: getAllAdvertsByUserId
    public Page<DetailedAdvertResponse> getAllAdvertsByUserId(Long id, int page, String sort, int size, String type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return repository.findByUser_Id(id,pageable).map(mapper::toDetailedAdvertResponse);

    }

}


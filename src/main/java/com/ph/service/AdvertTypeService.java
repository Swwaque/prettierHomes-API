package com.ph.service;


import com.ph.domain.entities.AdvertType;
import com.ph.exception.customs.BuiltInFieldException;
import com.ph.exception.customs.NonDeletableException;
import com.ph.exception.customs.ResourceNotFoundException;
import com.ph.payload.mapper.AdvertMapper;
import com.ph.payload.request.AdvertTypeRequest;
import com.ph.payload.response.AdvertTypeResponse;
import com.ph.repository.AdvertTypeRepository;
import com.ph.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertTypeService {

    private final AdvertTypeRepository repository;
    private final AdvertMapper mapper;
    private final MessageUtil messageUtil;


 /*
  !!! Create new AdvertType method start.
    */

    /**
     * Create a new AdvertType.
     *
     * @param request The request object containing the data for the new AdvertType.
     * @return The ResponseEntity with the AdvertTypeResponse for the created AdvertType.
     */
    public ResponseEntity<AdvertTypeResponse> create(AdvertTypeRequest request) {
        // Convert the request object to an AdvertType entity
        AdvertType advertType = mapper.toEntity(request);

        // Save the AdvertType entity to the repository
        repository.save(advertType);

        // Convert the AdvertType entity to an AdvertTypeResponse object
        AdvertTypeResponse response = mapper.toResponse(advertType);

        // Return the ResponseEntity with the AdvertTypeResponse
        return ResponseEntity.ok(response);
    }
 /*
  !!! Create new AdvertType method end.
    */

    /*
    !!! HELPER METHOD START .
     */

    /**
     * Retrieves the AdvertType with the specified id.
     *
     * @param id the id of the AdvertType to retrieve
     * @return the AdvertType with the specified id
     * @throws ResourceNotFoundException if the AdvertType is not found
     */
    @Cacheable(value = "advertType", key = "#id")
    public AdvertType getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(messageUtil.getMessage("error.advert.type.not.found"), id)));
    }
      /*
    !!! HELPER METHOD END .
     */

    /*
    !!! Get AdvertType by id method start.
     */

    /**
     * Retrieves the AdvertType by its id.
     *
     * @param id The id of the AdvertType to retrieve.
     * @return The ResponseEntity containing the AdvertTypeResponse.
     */
    public ResponseEntity<AdvertTypeResponse> get(Long id) {
        // Retrieve the AdvertType by its id
        AdvertType advertType = getById(id);

        // Convert the AdvertType to the AdvertTypeResponse using the mapper
        AdvertTypeResponse advertTypeResponse = mapper.toResponse(advertType);

        // Return the ResponseEntity containing the AdvertTypeResponse
        return ResponseEntity.ok(advertTypeResponse);
    }

      /*
    !!! Get AdvertType by id method end.
     */

      /*
    !!! Get All  AdvertTypes method start.
     */

    /**
     * Retrieves all advert types.
     *
     * @return the list of advert types
     */
    public ResponseEntity<List<AdvertTypeResponse>> getAll() {
        List<AdvertType> advertTypes = repository.findAll();
        List<AdvertTypeResponse> advertTypeResponses = advertTypes.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(advertTypeResponses);
    }

    /*
    !!! Get All  AdvertTypes method end.
     */


    /*
    !!! Delete AdvertType method start.
     */

    /**
     * Deletes an AdvertType by its ID.
     *
     * @param id The ID of the AdvertType to delete.
     * @return The response entity with the deleted AdvertType.
     */
    @CacheEvict(value = "advertType", key = "#id")
    public ResponseEntity<AdvertTypeResponse> delete(Long id) {
        // Retrieve the AdvertType by its ID
        AdvertType advertType = getById(id);
        if (advertType.isBuiltIn()) {
            throw new BuiltInFieldException("Advert Type cannot be deleted");
        }
        if (!advertType.getAdverts().isEmpty()) {
            throw new NonDeletableException("AdvertType cannot be deleted because it has associated Adverts");
        }

        // Delete the AdvertType from the repository
        repository.delete(advertType);

        // Return the deleted AdvertType as a response entity
        return ResponseEntity.ok(mapper.toResponse(advertType));
    }

     /*
    !!! Delete AdvertType method end.
     */


    /*
    !!! Update AdvertType method start.
     */

    /**
     * Updates an advert type by its ID.
     *
     * @param id      The ID of the advert type to update.
     * @param request The updated advert type data.
     * @return The response entity containing the updated advert type.
     */
    public ResponseEntity<AdvertTypeResponse> update(Long id, AdvertTypeRequest request) {
        // Get the advert type by its ID
      AdvertType found =  getById(id);
        if (found.isBuiltIn()) {
            throw new BuiltInFieldException("AdvertType cannot be updated");
        }

        // Map the request data to an advert type entity
        AdvertType advertType = mapper.toEntity(request);
        advertType.setId(id);

        // Save the updated advert type
        AdvertType updatedAdvertType = repository.save(advertType);

        // Return the response entity containing the updated advert type
        return ResponseEntity.ok(mapper.toResponse(updatedAdvertType));
    }

      /*
    !!! Update AdvertType method end.
     */


}

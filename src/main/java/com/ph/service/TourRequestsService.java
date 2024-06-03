package com.ph.service;

import com.ph.domain.entities.Advert;
import com.ph.domain.entities.TourRequest;
import com.ph.domain.entities.User;
import com.ph.domain.enums.Status;
import com.ph.exception.customs.*;
import com.ph.payload.mapper.TourRequestsMapper;
import com.ph.payload.request.TourRequestRequest;
import com.ph.payload.response.*;
import com.ph.repository.TourRequestsRepository;
import com.ph.utils.MessageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourRequestsService {

    private final TourRequestsRepository tourRequestsRepository;
    private final TourRequestsMapper tourRequestsMapper;
    private final UserService userService;
    private final AdvertService advertService;
    private final MessageUtil messageUtil;
    private final LogService logService;


    // Not :S05 - Save() *************************************************************************

    /**
     * Saves a tour request.
     *
     * @param request     The tour request details.
     * @param userDetails The details of the user making the request.
     * @return The response entity with the saved tour request details.
     * @throws ConflictException         If there is a conflict with the tour date and time.
     * @throws RelatedFieldException     If the tour time is not valid.
     * @throws ResourceNotFoundException If the user or advert is not found.
     */
    public ResponseEntity<?> save(TourRequestRequest request, UserDetails userDetails) {
        TourRequest tourRequest = request.get();
        // Check if the tour time is valid
        if (!isValidTourTime(tourRequest.getTourTime())) {
            throw new RelatedFieldException(messageUtil.getMessage("error.tour-time.bad-request"));
        }
        // Get the advert and user
        Advert advert = advertService.getById(request.getAdvertId());
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.user.not-found.id")));
        // Check if the user is the owner of the advert
        if (advert.getUser().getId().equals(user.getId())) {
            throw new RelatedFieldException(messageUtil.getMessage("error.tour-request.owner-cannot-create"));
        }
        // Check if the tour date is before today
        LocalDate tourDate = tourRequest.getTourDate();
        LocalDate today = LocalDate.now();
        // Check if the tour date is before today
        if (tourDate.isBefore(today) || tourDate.isEqual(today)) {
            throw new ConflictException(messageUtil.getMessage("error.tour-request.invalid-date"));
        }
        // Check if the tour request already exists
        if (tourRequestsRepository.existsByTourDateAndTourTimeAndStatus(tourDate, tourRequest.getTourTime(), Status.APPROVED)) {
            throw new ConflictException(messageUtil.getMessage("error.tour-request.approved-request-exists"));
        }
        // Check if the tour request already exists for the user
        if (tourRequestsRepository.existsByTourDateAndTourTimeAndStatusAndGuestUser_Id(tourRequest.getTourDate(), tourRequest.getTourTime(), Status.PENDING, user.getId())) {
            throw new ConflictException(messageUtil.getMessage("error.tour-request.pending-request-exists-for-user"));
        }
        // Set the advert, guest user, and owner user for the tour request
        User ownerUser = advert.getUser();
        tourRequest.setAdvert(advert);
        tourRequest.setGuestUser(user);
        tourRequest.setOwnerUser(ownerUser);
        // Save the tour request
        TourRequest saved = tourRequestsRepository.save(tourRequest);
        // Log the tour request creation
        logService.logMessage(messageUtil.getMessage("success.tour-request.created"), advert, user);
        // Return the response entity with the saved tour request details
        return ResponseEntity.ok(tourRequestsMapper.toTourRequestsSaveResponse(saved));
    }


    // Not :Helper Method

    /**
     * Check if the given tour time is valid.
     * A valid tour time has minutes set to either 00 or 30.
     *
     * @param tourTime the time of the tour
     * @return true if the tour time is valid, false otherwise
     */
    private boolean isValidTourTime(LocalTime tourTime) {
        int minute = tourTime.getMinute();
        return (minute == 00 || minute == 30);
    }

    // Not :S06 - update() ****************************************************************************

    /**
     * Update a tour request.
     *
     * @param tourId  The ID of the tour request to update.
     * @param request The updated tour request data.
     * @return The response entity with the updated tour request.
     * @throws ResourceNotFoundException If the tour request is not found.
     * @throws ConflictException         If there is a conflict in tour time.
     * @throws RelatedFieldException     If the tour time is not valid.
     */
    public ResponseEntity<?> update(Long tourId, TourRequestRequest request) {

        // Check if the tour request exists for the given ID
        TourRequest tourRequest = tourRequestsRepository.findById(tourId).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.tour-request.not-found")));
        // Check if the tour date is before today
        if (tourRequest.getTourDate().isEqual(LocalDate.now())) {
            throw new RelatedFieldException(messageUtil.getMessage("error.tour-request.cannot-updated"));
        }
        // Check if the tour request already exists
        if (tourRequestsRepository.existsByTourDateAndTourTimeAndStatus(request.getTourDate(), request.getTourTime(), Status.APPROVED)) {
            throw new ConflictException(messageUtil.getMessage("error.tour-request.approved-request-exists"));
        }
        // Check if the tour request already exists for the user
        if (tourRequestsRepository.existsByTourDateAndTourTimeAndStatusAndGuestUser_Id(request.getTourDate(), request.getTourTime(), Status.PENDING, tourRequest.getGuestUser().getId())) {
            throw new ConflictException(messageUtil.getMessage("error.tour-request.pending-request-exists-for-user"));
        }
        // Check if the tour request status is approved or canceled
        if ((Status.APPROVED.name()).equals(tourRequest.getStatus().name())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", messageUtil.getMessage("error.tour-request.cannot-approved")));
        }
        if ((Status.CANCELED.name()).equals(tourRequest.getStatus().name())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", messageUtil.getMessage("error.tour-request.cannot-canceled")));
        }
        // Check if the tour time is valid
        if (!isValidTourTime(tourRequest.getTourTime())) {
            throw new RelatedFieldException(messageUtil.getMessage("error.tour-time.bad-request"));
        }
        // Update the tour request data and save it
        tourRequest.setTourDate(request.getTourDate());
        tourRequest.setTourTime(request.getTourTime());
        tourRequest.setStatus(Status.PENDING);
        tourRequestsRepository.save(tourRequest);
        // Update the tour request data and save it
        return ResponseEntity.ok(tourRequestsMapper.toTourRequestsSaveResponse(tourRequest));
    }

    // Not :S01 - GetAllTourRequestByCustomerAsPage() ***************************************************

    /**
     * Retrieves all tour requests by customer as a pageable response.
     *
     * @param userDetails the user details of the customer
     * @param page        the page number
     * @param size        the number of items per page
     * @param sort        the sort order
     * @param type        the type of sorting (asc/desc)
     * @return a pageable response of tour request status
     * @throws ResourceNotFoundException if the user is not found
     */
    @Transactional
    public Page<TourRequestsStatusResponse> getAllTourRequestByCustomerAsPage(UserDetails userDetails, int page, int size, String sort, String type) {
        // Retrieve the user by email
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.user.not-found.id")));
        // Create a pageable object
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        // Retrieve tour requests by guest user ID
        return tourRequestsRepository.findAllByGuestUser_Id(user.getId(), pageable)
                .map(tourRequestsMapper::toTourRequestsResponse);
    }

    // Not:S10 - deleteTourRequest() *******************************************************************

    /**
     * Deletes a tour request with the given ID.
     *
     * @param id The ID of the tour request to delete.
     * @return The response entity containing the deleted tour request.
     * @throws ResourceNotFoundException If the tour request with the given ID is not found.
     */
    public ResponseEntity<TourRequestsResponse> deleteTourRequest(Long id) {
        // Find the tour request with the given ID
        TourRequest tourRequest = tourRequestsRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.tour-request.not-found")));

        // Check if the tour request can be deleted
        if (tourRequest.getStatus() == Status.APPROVED || tourRequest.getStatus() == Status.CANCELED) {
            throw new NonDeletableException(messageUtil.getMessage("error.tour-request.cannot-deleted"));
        }

        // Delete the tour request from the repository
        tourRequestsRepository.deleteById(id);
        // Return the response entity containing the deleted tour request
        return ResponseEntity.ok(tourRequestsMapper.toTourRequestsSaveResponse(tourRequest));
    }

    /*
    private boolean isCustomerUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            boolean isCustomer = false;
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("CUSTOMER")) {
                    isCustomer = true; // Kullanıcı "CUSTOMER" roline sahipse true döndür
                } else if (authority.getAuthority().equals("ADMIN") || authority.getAuthority().equals("MANAGER")) {
                    return false; // "ADMIN" veya "MANAGER" rolüne sahipse false döndür
                }
            }
            return isCustomer;
        }
        return false; // Kullanıcı "CUSTOMER" roline sahip değilse false döndür
    }
     */

    // Not :S02 - GetAllTourRequestByManagerAndAdminAsPage() ***************************************************

    /**
     * Retrieves all tour requests by manager and admin as a pageable response.
     *
     * @param page  the page number
     * @param size  the number of items per page
     * @param sort  the sort order
     * @param type  the type of sorting (asc/desc)
     * @param query the query to filter tour requests by advert title
     * @return a pageable response of tour requests
     */
    @Transactional
    public Page<TourRequestsFullResponse> getAllTourRequestByManagerAndAdminAsPage(int page, int size, String sort, String type, String query) {
        // Create a pageable object with the specified page, size, and sort order
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        // Check if the sort type is descending and update the pageable object accordingly
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        // Check if the query is null or empty
        if (query == null || query.isEmpty()) {
            // If there is no query, retrieve all tour requests using the pageable object and map them to TourRequestsFullResponse
            return tourRequestsRepository.findAll(pageable).map(tourRequestsMapper::toTourRequestsFullResponse);
        }
        // Retrieve all tour requests using the pageable object
        return tourRequestsRepository.search(query, pageable)
                .map(tourRequestsMapper::toTourRequestsFullResponse);


     /*   List<TourRequestsFullResponse> tourRequestResponses = tourRequestsRepository.findAll(pageable)
                .stream()
                .filter(tourRequest -> tourRequest.getAdvert().getTitle().toLowerCase().contains(query.toLowerCase()))
                .map(tourRequestsMapper::toTourRequestsFullResponse)
                .collect(Collectors.toList());*/
    }

    // Not :S03 - GetTourRequestByCustomerAsTourId() *******************************************************************

    /**
     * Retrieves a tour request by customer ID.
     *
     * @param userDetails The details of the user making the request.
     * @param tourId      The ID of the tour request.
     * @return The response entity with the tour request details.
     * @throws ResourceNotFoundException If the user or tour request is not found.
     */
    public ResponseEntity<TourRequestsFullResponse> getTourRequestByCustomerId(UserDetails userDetails, Long tourId) {
        // Get the user by email
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.user.not-found.id")));
        // Find the tour request by tour ID and user ID
        TourRequest tourRequest = tourRequestsRepository.findByIdAndGuestUser_Id(tourId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("error.tour-request.not-found")));
        // Return the response entity with the tour request details
        return ResponseEntity.ok(tourRequestsMapper.toTourRequestsFullResponse(tourRequest));
    }

    @Transactional
    public Page<TourRequestsResponseForOwner> getAllTourRequestByOwnerAsPage(UserDetails userDetails, int page, int size, String sort, String type) {
        // Retrieve the user by email
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.user.not-found.id")));
        // Create a pageable object
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        // Retrieve tour requests by guest user ID
        return tourRequestsRepository.findByOwnerUser_Id(user.getId(), pageable)
                .map(tourRequestsMapper::toTourRequestsResponseForOwner);
    }

    // Not :S04 - GetTourRequestByManagerAndAdminAsTourId() **************************************************

    /**
     * Retrieves a tour request by manager and admin ID.
     *
     * @param tourId The ID of the tour request.
     * @return The response entity with the tour request details.
     * @throws ResourceNotFoundException If the tour request is not found.
     */
    public ResponseEntity<TourRequestsFullResponse> getTourRequestByManagerAndAdminId(Long tourId) {
        // Retrieve the tour request by ID from the tour requests repository
        TourRequest tourRequest = tourRequestsRepository.findById(tourId).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.tour-request.not-found")));
        // Return the tour request details as a response entity
        return ResponseEntity.ok(tourRequestsMapper.toTourRequestsFullResponse(tourRequest));
    }

    // Not :S07 - CancelByCustomerAsTourId() *******************************************************************

    /**
     * Cancels a tour request by the customer based on the tour ID.
     *
     * @param tourId      The ID of the tour to cancel.
     * @param userDetails The details of the user canceling the tour.
     * @return The response entity containing the canceled tour request.
     * @throws ResourceNotFoundException If the user or tour request is not found.
     */
    public ResponseEntity<TourRequestsResponse> cancelByCustomerAsTourId(Long tourId, UserDetails userDetails) {
        // Get the user based on the email from the user details
        User user = userService.getUserByEmail(userDetails.getUsername()).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.user.update.not-found")));
        // Get the tour request based on the tour ID and user ID
        TourRequest tourRequest = tourRequestsRepository.findByIdAndGuestUser_Id(tourId, user.getId()).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.tour-request.not-found")));
        // Check if the tour request is already canceled or declined
        if (tourRequest.getStatus() != Status.APPROVED) {
            throw new ConflictException(messageUtil.getMessage("error.tour-request.already-processed"));
        }
        // Check if the tour date is within the allowed cancellation period
        LocalDate today = LocalDate.now();
        LocalDate tourDate = tourRequest.getTourDate();

        int allowedCancellationDays = 1;
        if (today.isAfter(tourDate.minusDays(allowedCancellationDays))) {
            throw new NonDeletableException(messageUtil.getMessage("error.tour-request.invalid-cancellation-date"));
        }
        // Update the tour request status to CANCELED
        tourRequest.setStatus(Status.CANCELED);
        tourRequestsRepository.save(tourRequest);
        // Log the canceled tour request
        logService.logMessage(messageUtil.getMessage("success.tour-request.canceled"), tourRequest.getAdvert(), tourRequest.getGuestUser());
        // Return the response entity with the canceled tour request
        return ResponseEntity.ok(tourRequestsMapper.toTourRequestsSaveResponse(tourRequest));
    }

    // Not :S08 - ApproveByCustomerAsTourId() *******************************************************************

    /**
     * Approves a tour request by the customer based on the tour ID.
     *
     * @param tourId The ID of the tour to be approved.
     * @return The status response of the approved tour request.
     * @throws ResourceNotFoundException If the tour request is not found.
     */
    @Transactional
    public ResponseEntity<TourRequestsStatusResponse> approveByCustomerAsTourId(Long tourId) {
        // Find the tour request by ID or throw an exception if not found
        TourRequest tourRequest = tourRequestsRepository.findById(tourId).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.tour-request.not-found")));
        // Check if the tour request has already been approved or declined
        if (tourRequest.getStatus() == Status.APPROVED || tourRequest.getStatus() == Status.CANCELED) {
            throw new ConflictException(messageUtil.getMessage("error.tour-request.already-processed"));
        }
        LocalDate tourDate = tourRequest.getTourDate();
        LocalDate today = LocalDate.now();
        // Check if the tour date is before today
        if (tourDate.isBefore(today) || tourDate.isEqual(today)) {
            throw new ConflictException(messageUtil.getMessage("error.tour-request.approved"));
        }
        // Check if there is already an approved request for the same date and time
        if (hasApprovedRequestAtSameDateTime(tourRequest)) {
            throw new ConflictException(messageUtil.getMessage("error.tour-request.approved-request-exists"));
        }
        // Update the status of the tour request to "APPROVED"
        tourRequest.setStatus(Status.APPROVED);
        // Save the updated tour request in the repository
        tourRequestsRepository.save(tourRequest);
        // Automatically decline other tour requests created at the same date and time
        declineOtherTourRequestsAtSameDateTime(tourRequest);
        // Log a message indicating that the tour request has been approved
        logService.logMessage(messageUtil.getMessage("success.tour-request.approved"), tourRequest.getAdvert(), tourRequest.getGuestUser());
        // Return the status response of the approved tour request
        return ResponseEntity.ok(tourRequestsMapper.toTourRequestsResponse(tourRequest));
    }

    // Not: Helper Method***********************************************************************
    private void declineOtherTourRequestsAtSameDateTime(TourRequest tourRequest) {
        LocalDate requestDate = tourRequest.getTourDate();
        LocalTime requestTime = tourRequest.getTourTime();
        List<TourRequest> otherRequestsAtSameDateTime = tourRequestsRepository.findAllByTourDateAndTourTime(requestDate, requestTime);
        for (TourRequest otherRequest : otherRequestsAtSameDateTime) {
            if (otherRequest.getId() != tourRequest.getId()) {
                // Set the status of other requests to "DECLINED"
                otherRequest.setStatus(Status.DECLINED);
                tourRequestsRepository.save(otherRequest);
            }
        }
    }

    // Not: Helper Method***********************************************************************
    private boolean hasApprovedRequestAtSameDateTime(TourRequest tourRequest) {
        LocalDate requestDate = tourRequest.getTourDate();
        LocalTime requestTime = tourRequest.getTourTime();
        List<TourRequest> approvedRequestsAtSameDateTime = tourRequestsRepository.findAllByTourDateAndTourTimeAndStatus(requestDate, requestTime, Status.APPROVED);
        return !approvedRequestsAtSameDateTime.isEmpty();
    }


    // Not :S09 - DeclinedByCustomerAsTourId() *******************************************************************

    /**
     * Declines a tour request by the customer based on the tour ID.
     *
     * @param tourId The ID of the tour to decline.
     * @return The response entity containing the declined tour request.
     * @throws ResourceNotFoundException If the tour request is not found.
     */
    @Transactional
    public ResponseEntity<TourRequestsStatusResponse> declinedByCustomerAsTourId(Long tourId) {
        // Find the tour request by ID or throw an exception if not found
        TourRequest tourRequest = tourRequestsRepository.findById(tourId).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.tour-request.not-found")));
        // Check if the tour request has already been approved or declined
        if (tourRequest.getStatus() == Status.DECLINED || tourRequest.getStatus() == Status.CANCELED) {
            throw new ConflictException(messageUtil.getMessage("error.tour-request.already-processed"));
        }
        if (tourRequest.getStatus() == Status.APPROVED && tourRequest.getTourDate().equals(LocalDate.now())) {
            throw new RelatedFieldException(messageUtil.getMessage("error.tour-request.cannot-declined"));
        }
        // Update the status of the tour request to "DECLINED"
        tourRequest.setStatus(Status.DECLINED);
        // Save the updated tour request in the repository
        tourRequestsRepository.save(tourRequest);
        // Log a message indicating that the tour request has been declined
        logService.logMessage(messageUtil.getMessage("success.tour-request.declined"), tourRequest.getAdvert(), tourRequest.getGuestUser());
        // Return the status response of the declined tour request
        return ResponseEntity.ok(tourRequestsMapper.toTourRequestsResponse(tourRequest));
    }

    // Not : GetById() ***************************************************************************************

    /**
     * Retrieves a tour request by its ID.
     *
     * @param id the ID of the tour request to retrieve
     * @return the tour request with the given ID
     * @throws ResourceNotFoundException if the tour request is not found
     */
    public TourRequest getById(Long id) {
        return tourRequestsRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(messageUtil.getMessage("error.tour-request.not-found")));
    }

    @Transactional
    public Page<TourRequestResponseSimple> getTourRequestByAdvertId(Pageable pageable, Long advertId) {
        Page<TourRequest> tourRequests = tourRequestsRepository.findByAdvert_Id(advertId, pageable);
        return tourRequests.map(tourRequestsMapper::toResponseSimple);
    }

    // Not: getTourRequestCount

    /**
     * Retrieve the count of tour requests for a specific advertisement.
     *
     * @param advertId    the ID of the advertisement
     * @param userDetails the details of the authenticated user
     * @return the count of tour requests
     * @throws ResourceNotFoundException if the user or advertisement is not found
     */
    public ResponseEntity<Long> getTourRequestCount(Long advertId, UserDetails userDetails) {
        // Retrieve the user by email from the user service
        User user = userService.getUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("error.user.update.not-found")));

        // Retrieve the advertisement by ID from the advert service
        Advert advert = advertService.getById(advertId);

        // Retrieve all advertowned by the user
        List<Advert> advertList = advertService.getAllAdvertsByUserId(user.getId());

        if (advertList.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("error.advert.not.found"));
        }

        // Retrieve the count of tour requests for the specified advert and user
        Long tourRequestCount = tourRequestsRepository.countByAdvert_IdAndOwnerUser_Id(advertId, user.getId());

        // Return the count of tour requests as a ResponseEntity
        return ResponseEntity.ok(tourRequestCount);
    }


    // Not: getAllAdvertsByUserId
    @Transactional
    public ResponseEntity<Page<TourRequestsFullResponse>> getAllTourRequestsByUserId(Long id, int page, String sort, int size, String type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<TourRequest> tourRequests = tourRequestsRepository.findAllByGuestUser_Id(id, pageable);
        Page<TourRequestsFullResponse> tourRequestsResponses = tourRequests
                .map(tourRequestsMapper::toTourRequestsFullResponse);
        return ResponseEntity.ok(tourRequestsResponses);
    }

    @Transactional
    public List<TourRequest> checkingTheDatesOfTourRequests() {
        // LocalDate.now() ile mevcut tarihi al
        LocalDate currentDate = LocalDate.now();
        // plusDays(1) ile bir sonraki günü elde et
        LocalDate nextDay = currentDate.plusDays(1);
        return tourRequestsRepository.findByTourDateEquals(nextDay);

    }


    // Not: getTourRequestCount
    public List<TourRequestCountResponse> getTourRequestCounts() {

        return tourRequestsRepository.getCountsTourRequests();

    }


    public List<TourRequestCountResponse> getTourRequestCountsCustomer(UserDetails userDetails) {

        User user = (User) userDetails;

        List<Long> advertIds =
                advertService.getAllAdvertsByUserId(user.getId()).stream().map(Advert::getId).collect(Collectors.toList());

        return tourRequestsRepository.getCountsTourRequestsCustomer(advertIds);
    }

    @Transactional
    public List<TourRequest> getExpiredPendingTourRequests() {
        LocalDate today = LocalDate.now();
        return tourRequestsRepository.findPendingTourRequestsBeforeToday(today, Status.PENDING);
    }

    @Transactional
    public void declineExpiredPendingTourRequests(List<TourRequest> expiredPendingTourRequests) {
        for (TourRequest tourRequest : expiredPendingTourRequests) {
            tourRequest.setStatus(Status.DECLINED);
            tourRequestsRepository.save(tourRequest);
        }
    }
}
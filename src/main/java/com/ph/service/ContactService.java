package com.ph.service;

import com.ph.domain.entities.Contact;
import com.ph.exception.customs.ResourceNotFoundException;
import com.ph.exception.customs.ValuesNotMatchException;
import com.ph.payload.mapper.UserMapper;
import com.ph.payload.request.ContactRequest;
import com.ph.payload.response.ContactResponse;
import com.ph.repository.ContactRepository;
import com.ph.utils.MessageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final MessageUtil messageUtil;
    private final UserMapper userMapper;

    // Not :J02 - Save() *************************************************************************
    // Save the contact and return a response entity with a success message
    public ResponseEntity<String> save(ContactRequest request) {
        Contact contact = request.get();
        contactRepository.save(contact);
        return ResponseEntity.ok(messageUtil.getMessage("success.contact.saved"));
    }

    // Not :J01 - GetAll() *************************************************************************

    /**
     * Retrieves all contacts with pagination, sorting, and filtering options.
     *
     * @param page The page number to retrieve.
     * @param size The number of contacts per page.
     * @param sort The sorting criteria for the contacts.
     * @param type The sorting type (ascending or descending).
     * @return A ResponseEntity containing a Page of ContactResponse objects.
     */
    public ResponseEntity<Page<ContactResponse>> getAllContact(
            String query, Boolean status, LocalDate startDate, LocalDate endDate,
            int page, int size, String sort, String type
    ) {

        // Check if the start date is after the end date
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ValuesNotMatchException(messageUtil.getMessage("error.report.date"));
        }

        // Create a Pageable object with the provided page, size, and sort criteria
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        // If the sorting type is "desc", create a new Pageable object with descending sort
        if (Objects.equals(type, "DESC")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        if (startDate != null) {
            startDateTime = startDate.atTime(LocalTime.MIN);
        } else {
            startDateTime = LocalDateTime.of(1900, 1, 1, 0, 0);
        }
        if (endDate != null) {
            endDateTime = endDate.atTime(LocalTime.MAX);
        } else {
            endDateTime = LocalDateTime.of(2400, 1, 1, 0, 0);
        }

        // Return a ResponseEntity containing the contact responses
        return ResponseEntity.ok(contactRepository
                        .findContactsPageableBySearch(query, status, startDateTime, endDateTime, pageable)
                        .map(userMapper::toContactResponse)
        );
    }

    // Not :J03 - getById() *************************************************************************
    public ResponseEntity<ContactResponse> getById(Long id) {

        Contact contact = contactRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException(messageUtil.getMessage("error.contact.not.found")));

        return ResponseEntity.ok(userMapper.toContactResponse(contact));
    }

    // Not :J04 - delete() *************************************************************************
    public ResponseEntity<?> delete(Long id) {

        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException(messageUtil.getMessage("error.contact.not.found"));
        }

        contactRepository.deleteById(id);
        return ResponseEntity.ok(messageUtil.getMessage("success.contact.deleted"));
    }

    // Not :J05 - deleteAll() **********************************************************************
    @Transactional
    public ResponseEntity<?> deleteAllMessages() {
        if (contactRepository.count() == 0) {
            throw new ResourceNotFoundException(messageUtil.getMessage("error.contact.not.found"));
        }

        contactRepository.deleteAll();
        return ResponseEntity.ok(messageUtil.getMessage("success.contact.deleted"));
    }

    // Not :J06 - toggleMessageStatus() *************************************************************************
    public ResponseEntity<ContactResponse> toggleMessageStatus(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("error.contact.not.found")));

        System.err.println(contact);
        System.err.println("Ben çalıştım");

        contact.setStatus(!contact.isStatus());

        contactRepository.save(contact);
        return ResponseEntity.ok(userMapper.toContactResponse(contact));
    }

}

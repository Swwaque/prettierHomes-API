package com.ph.service;


import com.ph.domain.entities.*;
import com.ph.exception.customs.*;
import com.ph.payload.mapper.UserMapper;
import com.ph.payload.request.*;
import com.ph.repository.FavoritesRepository;
import com.ph.repository.TourRequestsRepository;
import com.ph.repository.UserRepository;
import com.ph.security.role.Role;
import com.ph.utils.MessageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MessageUtil messageUtil;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ProfilePhotoService profilePhotoService;
    private final FavoritesRepository favoritesRepository;
    private final ConfirmationTokenService confirmationTokenService;


    /**
     * This method is used to retrieve user for login process and some other methods.
     * Retrieve a user by their email address.
     *
     * @param email The email address of the user.
     * @return An optional User object if a user with the email address is found, otherwise an empty optional.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Saves a new user to the database.
     *
     * @param userSaveRequest the request object containing user information
     * @return the response entity with the saved user information
     */
    public ResponseEntity<?> saveUser(UserSaveRequest userSaveRequest) {
        User user = userSaveRequest.get();
        // Check for duplicate fields
        checkDuplicate(user.getPhone(), user.getEmail(), user);
        // Set the built-in flag to false
        user.setBuiltIn(false);
        // Hash the user's password
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        // Set the user's roles
        user.setRole(Role.CUSTOMER);
        // Save the user to the database
        User saved = userRepository.save(user);

        final ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        emailService.sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken(), confirmationToken.getCreatedTime());
        // Return the saved user information
        return ResponseEntity.ok(userMapper.toUserSaveResponse(saved));
    }

    /**
     * This method handles the forgot password functionality.
     * It generates a reset password code for the user,
     * saves it to the user object, and sends the code to the user's email.
     *
     * @param request The forgot password request object containing the user's email.
     * @return The HTTP response entity with status OK.
     */
    public ResponseEntity<?> forgotPassword(ForgotPasswordRequest request) {
        // Find the user by email
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        // If the user exists
        if (user != null) {
            // Generate a reset password code
            String resetPasswordCode = UUID.randomUUID().toString();
            // Save the reset password code to the user object
            user.setResetPasswordCode(resetPasswordCode);
            userRepository.save(user);
            // Send the reset password code to the user's email
            sendResetPasswordCode(user.getEmail(), resetPasswordCode);
        }
        // Return the HTTP response entity with status OK
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * This method sends the reset password code to the user's email.
     *
     * @param email             The email address of the user.
     * @param resetPasswordCode The reset password code.
     */
    private void sendResetPasswordCode(String email, String resetPasswordCode) {
        emailService.sendResetPasswordEmail(email, resetPasswordCode);
    }

    /**
     * Resets the password for a user.
     *
     * @param request the reset password request containing the code and new password
     * @return a ResponseEntity with a status of OK if the password was reset successfully
     * @throws RuntimeException if the reset password code is not valid
     */
    public ResponseEntity<?> resetPassword(ResetPasswordRequest request) {
        // Find the user with the provided reset password code
        User user = userRepository
                .findByResetPasswordCode(request.getCode())
                .orElseThrow(() -> new ValuesNotMatchException(messageUtil.getMessage("error.reset-code.invalid")));
        // Set the new password for the user
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        // Save the user with the updated password
        userRepository.save(user);
        // Return a ResponseEntity with a status of OK
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Change the password of the authenticated user.
     *
     * @param request     The change password request.
     * @param userDetails The details of the authenticated user.
     * @return A response entity indicating the success of the password change.
     * @throws BuiltInFieldException   If the user is a built-in user.
     * @throws ValuesNotMatchException If the current password provided does not match the user's password.
     */
    public ResponseEntity<?> changeAuthenticatedUserPassword(ChangePasswordRequest request, UserDetails userDetails) {
        // Cast the userDetails to User object
        User user = (User) userDetails;
        // Check if the user is built-in and return a bad request if true
        if (user.isBuiltIn()) {
            throw new BuiltInFieldException(messageUtil.getMessage("error.user.update.built-in"));
        }
        // Check if the current password matches the user's password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new ValuesNotMatchException(messageUtil.getMessage("error.user.password.invalid"));
        }
        // Update the user's password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        // Save the updated user
        User updated = userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    /**
     * Get the authenticated user.
     *
     * @param userDetails The user details of the authenticated user.
     * @return The response entity containing the user information.
     */
    public ResponseEntity<?> getAuthenticatedUser(UserDetails userDetails) {
        User user = (User) userDetails;
        return ResponseEntity.ok(userMapper.toUserResponse(user));
    }

    /**
     * Updates the authenticated user with the given request.
     *
     * @param request     The user update request.
     * @param userDetails The details of the authenticated user.
     * @return The response entity containing the updated user information.
     */
    public ResponseEntity<?> updateAuthenticatedUser(UserUpdateRequest request, UserDetails userDetails) {
        // Cast the userDetails to User object
        User user = (User) userDetails;
        // Using helper methods, Update the authenticated user with the request
        User updatedUser = updateUser(user, request);
        // Map the updated user to a response entity
        return ResponseEntity.ok(userMapper.toUserResponse(updatedUser));
    }

    /**
     * Deletes the authenticated user from the user repository.
     *
     * @param userDetails the authenticated user details
     * @return a ResponseEntity representing the response with status 200 OK
     */
    public ResponseEntity<?> deleteAuthenticatedUser(UserDetails userDetails) {
        // Cast the userDetails to User type
        User user = (User) userDetails;
        // Check for built-in and related fields in the user object
        checkBuiltInAndRelatedFieldsBeforeDeletion(user);
        // Delete the user from the user repository
        userRepository.delete(user);
        // Return ResponseEntity with status 200 OK
        return ResponseEntity.ok().build();
    }

    /**
     * This method is for Admin or Manager Users
     * Retrieves a pageable list of users based on a search query.
     * If a search query is provided, it filters the users based on the query.
     * If no search query is provided, it returns all users.
     *
     * @param q        The search query string.
     * @param pageable The pageable object containing pagination information.
     * @return A ResponseEntity with the paginated list of users.
     */
    public ResponseEntity<?> getUsersPageable(String q, Pageable pageable) {
        if (q != null) {
            // Find users based on search query
            Page<User> pageableBySearch = userRepository.findUserPageableBySearch(q, pageable);
            return ResponseEntity.ok(pageableBySearch.map(userMapper::toUserResponse));
        }
        // Find all users
        Page<User> all = userRepository.findAll(pageable);
        return ResponseEntity.ok(all.map(userMapper::toUserResponse));
    }

    /**
     * This method is for Admin or Manager Users
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the user with the specified ID
     */
    public ResponseEntity<?> getOneUser(Long userId) {
        return ResponseEntity.ok(userMapper.toUserResponse(getOneUserById(userId)));
    }

    /**
     * This method is for Admin or Manager Users
     * Updates a user with the given ID and request data.
     *
     * @param userId      The ID of the user to update.
     * @param request     The request data containing the updated user information.
     * @param userDetails The details of the authenticated user making the request.
     * @return The response entity containing the updated user information.
     */
    public ResponseEntity<?> updateOneUser(Long userId, UserUpdateRequest request, UserDetails userDetails) {
        // Cast the userDetails to User
        User authenticatedUser = (User) userDetails;
        // Get the user to update by ID
        User foundUser = getOneUserById(userId);
        // Check if the authenticated user is a manager and the found user is not a customer
        if (authenticatedUser.getRole().name().equals("MANAGER") && !foundUser.getRole().name().equals("CUSTOMER")) {
            // Return bad request response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", messageUtil.getMessage("error.user.update.unauthorized")));
        }
        // Update the found user with the request data
        User updatedUser = updateUser(foundUser, request);
        // Return OK response with the updated user information
        return ResponseEntity.ok(userMapper.toUserResponse(updatedUser));
    }

    /**
     * This method is for Admin or Manager Users
     * Deletes a user from the repository.
     *
     * @param userId      The ID of the user to be deleted.
     * @param userDetails The details of the authenticated user.
     * @return A ResponseEntity representing the result of the deletion.
     */
    public ResponseEntity<?> deleteOneUser(Long userId, UserDetails userDetails) {
        // Cast the userDetails to User type
        User authenticatedUser = (User) userDetails;
        // Get the user to delete by ID
        User foundUser = getOneUserById(userId);
        // Check if the authenticated user is a manager and the found user is not a customer
        if (authenticatedUser.getRole().name().equals("MANAGER") && !foundUser.getRole().name().equals("CUSTOMER")) {
            // Return a bad request response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", messageUtil.getMessage("error.user.delete.unauthorized")));
        }
        // Check if the found user has any built-in and related fields
        checkBuiltInAndRelatedFieldsBeforeDeletion(foundUser);
        favoritesRepository.deleteAllById(foundUser.getFavorites().stream().map(Favorite::getId).collect(Collectors.toList()));
        // Delete the found user from the repository
        userRepository.delete(foundUser);
        // Return a success response
        return ResponseEntity.ok().build();
    }

    /**
     * Update the role of a user.
     *
     * @param userId  The ID of the user to update.
     * @param request The request containing the new role.
     * @return The updated user with the new role.
     * @throws BuiltInFieldException If the user is a built-in user.
     */
    public ResponseEntity<?> updateUserRole(Long userId, UserRoleUpdateRequest request) {
        // Get the user by ID
        User user = getOneUserById(userId);
        // Check if the user is a built-in user
        if (user.isBuiltIn()) {
            throw new BuiltInFieldException(messageUtil.getMessage("error.user.update.built-in"));
        }
        // Update the user's role
        user.setRole(request.getRole());
        // Save the updated user
        User updated = userRepository.save(user);
        // Return the updated user
        return ResponseEntity.ok(userMapper.toUserResponse(updated));
    }

    /**
     * This method is a helper method
     * Checks for duplicate phone number and email in the UserRepository.
     * Throws a ConflictException if a duplicate is found.
     *
     * @param phone The phone number to check.
     * @param email The email address to check.
     */
    private void checkDuplicate(String phone, String email, User user) {
        // Check if phone number already exists
        if (phone != null && !user.getPhone().equals(phone) && userRepository.existsByPhone(phone)) {
            throw new ConflictException(messageUtil.getMessage("error.phone.exists"));
        }
        // Check if email already exists
        if (email != null && !user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new ConflictException(messageUtil.getMessage("error.email.exists"));
        }
    }

    /**
     * This method is a helper method
     * Retrieves a User object by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user with the specified ID.
     * @throws ResourceNotFoundException If the user is not found.
     */
    public User getOneUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("error.user.not-found.id")));
    }


    /**
     * This method is a helper method that updates a user.
     * Updates a user with the provided request.
     *
     * @param user    the user to be updated
     * @param request the update request containing the new user data
     * @return the updated user
     * @throws BuiltInFieldException if the user is built-in and cannot be updated
     */
    private User updateUser(User user, UserUpdateRequest request) {
        // Check if the user is built-in and return a bad request if true
        if (user.isBuiltIn()) {
            throw new BuiltInFieldException(messageUtil.getMessage("error.user.update.built-in"));
        }
        // Check for duplicate phone and email values
        checkDuplicate(request.getPhone(), request.getEmail(), user);
        // Update the user with the provided request
        request.accept(user);
        // Save the updated user
        return userRepository.save(user);
    }

    /**
     * Checks if the user is built-in or has any related fields that prevent deletion.
     *
     * @param user the user to check
     * @throws BuiltInFieldException if the user is built-in
     * @throws RelatedFieldException if the user has an advert or a tour request
     */
    private void checkBuiltInAndRelatedFieldsBeforeDeletion(User user) {
        // Check if the user is built-in
        if (user.isBuiltIn()) {
            throw new BuiltInFieldException(messageUtil.getMessage("error.user.delete.built-in"));
        }
        // Check if the user has any adverts
//        if (advertService.isHaveUserAdvert(user.getId())) {
//            throw new RelatedFieldException(messageUtil.getMessage("error.user.delete.adverts"));
//        }
//        // Check if the user has any guest or owned tour requests
//        if (tourRequestsRepository.existsByGuestUser_Id(user.getId()) || tourRequestsRepository.existsByOwnerUser_Id(user.getId())) {
//            throw new RelatedFieldException(messageUtil.getMessage("error.user.delete.tou-requests"));
//        }
    }

    /**
     * Updates the user's photo.
     *
     * @param photo       the photo file to be uploaded
     * @param userDetails the details of the user
     * @return the ResponseEntity with the updated user information
     * @throws BuiltInFieldException if the user is a built-in user
     */
    public ResponseEntity<?> updateUserPhoto(MultipartFile photo, UserDetails userDetails) {
        User user = (User) userDetails;
        if (user.isBuiltIn()) {
            throw new BuiltInFieldException(messageUtil.getMessage("error.user.update.built-in"));
        }
        ProfilePhoto profilePhoto;
        if (user.getProfilePhoto() == null) {
            profilePhoto = profilePhotoService.saveProfilePhoto(photo);
        } else {
            profilePhoto = profilePhotoService.updateProfilePhoto(user.getProfilePhoto(), photo);
        }
        user.setProfilePhoto(profilePhoto);
        User updated = userRepository.save(user);
        return ResponseEntity.ok(userMapper.toUserResponse(updated));

    }

    /**
     * Deletes the user's profile photo and updates the user in the database.
     *
     * @param userDetails The details of the user.
     * @return A ResponseEntity containing the updated user information.
     * @throws BuiltInFieldException if the user is a built-in user.
     */
    public ResponseEntity<?> deleteUserPhoto(UserDetails userDetails) {
        User user = (User) userDetails;
        if (user.isBuiltIn()) {
            throw new BuiltInFieldException(messageUtil.getMessage("error.user.update.built-in"));
        }
        profilePhotoService.deleteProfilePhoto(user.getProfilePhoto());
        user.setProfilePhoto(null);
        User updated = userRepository.save(user);
        return ResponseEntity.ok(userMapper.toUserResponse(updated));
    }

    public List<Long> getFavoriteAdvertIdList(UserDetails userDetails) {
        User user = (User) userDetails;
       return favoritesRepository
               .findByUser_Id(user.getId())
               .stream().map(Favorite::getAdvert)
               .map(Advert::getId)
               .collect(Collectors.toList());
      }


    public void confirmUser(ConfirmationToken confirmationToken) {

        final User user = confirmationToken.getUser();

        user.setEnabled(true);

        userRepository.save(user);

        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());

    }
}

package com.ph.controller;

import com.ph.domain.entities.ConfirmationToken;
import com.ph.payload.request.*;
import com.ph.payload.response.LoginResponse;
import com.ph.security.service.AuthService;
import com.ph.service.ConfirmationTokenService;
import com.ph.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final ConfirmationTokenService confirmationTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> userSave(@RequestBody @Valid UserSaveRequest request) {
        return userService.saveUser(request);
    }

    @GetMapping("/register/confirm")
    public boolean confirmMail(@RequestParam("token") String token) {

        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);

        optionalConfirmationToken.ifPresent(userService::confirmUser);

        return optionalConfirmationToken.isPresent();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        return userService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        return userService.resetPassword(request);
    }

    @PatchMapping("/change-password")
    @PreAuthorize("hasAnyAuthority('CUSTOMER','MANAGER','ADMIN')")
    public ResponseEntity<?> changeAuthenticatedUserPassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        return userService.changeAuthenticatedUserPassword(request, userDetails);
    }

    @GetMapping("/auth")
    @PreAuthorize("hasAnyAuthority('CUSTOMER','MANAGER','ADMIN')")
    public ResponseEntity<?> getAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getAuthenticatedUser(userDetails);
    }

    @PatchMapping("/auth")
    @PreAuthorize("hasAnyAuthority('CUSTOMER','MANAGER','ADMIN')")
    public ResponseEntity<?> updateAuthenticatedUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        return userService.updateAuthenticatedUser(request, userDetails);
    }

    @DeleteMapping("/auth")
    @PreAuthorize("hasAnyAuthority('CUSTOMER','MANAGER','ADMIN')")
    public ResponseEntity<?> deleteAuthenticatedUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return userService.deleteAuthenticatedUser(userDetails);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> getUsersPageable(
            @RequestParam(name = "q", required = false) String query,
            @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return userService.getUsersPageable(query, pageable);
    }

    @GetMapping("/{userId}/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> getOneUser(@PathVariable(name = "userId") Long userId) {
        return userService.getOneUser(userId);
    }

    @PatchMapping("/{userId}/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> updateOneUser(
            @PathVariable(name = "userId") Long userId,
            @RequestBody @Valid UserUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return userService.updateOneUser(userId, request, userDetails);
    }

    @DeleteMapping("/{userId}/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> deleteOneUser(
            @PathVariable(name = "userId") Long userId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return userService.deleteOneUser(userId, userDetails);
    }

    @PatchMapping("/{userId}/role/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> updateUserRole(
            @PathVariable(name = "userId") Long userId,
            @RequestBody @Valid UserRoleUpdateRequest request
    ) {
        return userService.updateUserRole(userId, request);
    }

    @PatchMapping("/photo")
    @PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER','MANAGER')")
    public ResponseEntity<?> updateUserStatus(
            @RequestParam(name = "photo") MultipartFile photo,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return userService.updateUserPhoto(photo, userDetails);
    }

    @DeleteMapping("/photo")
    @PreAuthorize("hasAnyAuthority('ADMIN','CUSTOMER','MANAGER')")
    public ResponseEntity<?> deleteUserPhoto(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return userService.deleteUserPhoto(userDetails);
    }


    @GetMapping("/fav")
    public List<Long> getFavoriteAdvertIdList(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return userService.getFavoriteAdvertIdList(userDetails);
    }

}

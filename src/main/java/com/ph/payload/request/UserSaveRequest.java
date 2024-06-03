package com.ph.payload.request;

import com.ph.domain.entities.User;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.function.Supplier;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveRequest implements Supplier<User>, Serializable {

    @NotNull(message = "{validation.firstName.null}")
    @Size(min = 1, max = 50, message = "{validation.firstName.size}")
    @NotBlank(message = "{validation.firstName.notblank}")
    private String firstName;

    @NotNull(message = "{validation.lastName.null}")
    @Size(min = 1, max = 50, message = "{validation.lastName.size}")
    @NotBlank(message = "{validation.lastName.notblank}")
    private String lastName;

    @NotNull(message = "{validation.phone.null}")
    @NotBlank(message = "{validation.phone.notblank}")
    @Pattern(regexp = "\\(\\d{3}\\) \\d{3}-\\d{4}", message = "{validation.phone.pattern}")
    private String phone;

    @NotNull(message = "{validation.password.null}")
    @Size(min = 8, max = 30, message = "{validation.password.size}")
    @NotBlank(message = "{validation.password.notblank}")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]).*$",
            message = "{validation.password.pattern}")
    private String password;

    @NotNull(message = "{validation.email.null}")
    @NotBlank(message = "{validation.email.notblank}")
    @Email(message = "{validation.email.pattern}")
    private String email;

    @Override
    public User get() {
        return User.builder()
                .firstName(getFirstName())
                .lastName(getLastName())
                .email(getEmail())
                .phone(getPhone())
                .passwordHash(getPassword())
                .build();
    }

}

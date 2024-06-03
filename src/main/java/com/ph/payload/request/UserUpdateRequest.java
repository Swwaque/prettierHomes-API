package com.ph.payload.request;

import com.ph.domain.entities.User;
import com.ph.security.role.Role;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.function.Consumer;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest implements Consumer<User>, Serializable {

    @Size(min = 1, max = 50, message = "{validation.firstName.size}")
    @NotBlank(message = "{validation.firstName.notblank}")
    private String firstName;

    @Size(min = 1, max = 50, message = "{validation.lastName.size}")
    @NotBlank(message = "{validation.lastName.notblank}")
    private String lastName;

    @Pattern(regexp = "\\(\\d{3}\\) \\d{3}-\\d{4}", message = "{validation.phone.pattern}")
    @NotBlank(message = "{validation.phone.notblank}")
    private String phone;

    @Size(min = 8, max = 30, message = "{validation.password.size}")
    @NotBlank(message = "{validation.password.notblank}")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]).*$",
            message = "{validation.password.pattern}"
    )
    private String password;

    @Email(message = "{validation.email.pattern}")
    @NotBlank(message = "{validation.email.notblank}")
    private String email;

    @NotNull(message = "{validation.role.null}")
    private Role role;

    @Override
    public void accept(User user) {
        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);
        if (phone != null) user.setPhone(phone);
        if (email != null) user.setEmail(email);
        if (role != null) user.setRole(role);
    }

}

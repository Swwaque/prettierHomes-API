package com.ph.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements Serializable {

    @NotNull(message = "{validation.email.null}")
    @NotBlank(message = "{validation.email.notblank}")
    @Email(message = "{validation.email.pattern}")
    private String email;

    @NotNull(message = "{validation.password.null}")
    @NotBlank(message = "{validation.password.notblank}")
    @Size(min = 8, max = 20, message = "{validation.password.size}")
    private String password;

}

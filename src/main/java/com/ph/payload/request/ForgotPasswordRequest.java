package com.ph.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest implements Serializable {

    @NotNull(message = "{validation.email.null}")
    @NotBlank(message = "{validation.email.notblank}")
    @Email(message = "{validation.email.pattern}")
    private String email;

}

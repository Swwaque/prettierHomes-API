package com.ph.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest implements Serializable {

    @NotNull(message = "{validation.reset-code.null}")
    @NotBlank(message = "{validation.reset-code.notblank}")
    private String code;

    @NotNull(message = "{validation.password.null}")
    @NotBlank(message = "{validation.password.notblank}")
    @Size(min = 8, max = 30, message = "{validation.password.size}")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]).*$",
            message = "{validation.password.pattern}"
    )
    private String password;
}

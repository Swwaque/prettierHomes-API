package com.ph.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest implements Serializable {


    @NotNull(message = "{validation.password.null}")
    @Size(min = 8, max = 30, message = "{validation.password.size}")
    @NotBlank(message = "{validation.password.notblank}")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]).*$",
            message = "{validation.password.pattern}"
    )
    private String currentPassword;

    @NotNull(message = "{validation.password.null}")
    @Size(min = 8, max = 30, message = "{validation.password.size}")
    @NotBlank(message = "{validation.password.notblank}")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]).*$",
            message = "{validation.password.pattern}"
    )
    private String newPassword;

}
package com.ph.payload.request;

import com.ph.security.role.Role;
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
public class UserRoleUpdateRequest implements Serializable {

    @NotNull(message = "{validation.role.null}")
    private Role role;

}

package com.ph.payload.resource;

import com.ph.aboutexcel.ExportToExcel;
import com.ph.security.role.Role;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserResource implements Serializable {

    @ExportToExcel(index = 0, headerText = "USER ID", width = 2000)
    private Long id;

    @ExportToExcel(index = 1, headerText = "FIRST NAME", width = 5000)
    private String firstName;

    @ExportToExcel(index = 2, headerText = "LAST NAME", width = 5000)
    private String lastName;

    @ExportToExcel(index = 3, headerText = "E-MAIL", width = 8000)
    private String email;

    @ExportToExcel(index = 4, headerText = "PHONE NUMBER", width = 7000)
    private String phone;

    @ExportToExcel(index = 5, headerText = "BUILT IN", width = 3500)
    private boolean builtIn;

    @ExportToExcel(index = 6, headerText = "ROLE", width = 3000)
    private Role role;

}

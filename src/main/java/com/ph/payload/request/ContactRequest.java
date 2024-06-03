package com.ph.payload.request;

import com.ph.domain.entities.Contact;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.function.Supplier;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest implements Supplier<Contact>, Serializable {

    @NotBlank(message = "{validation.firstName.notblank}")
    private String firstName;

    @NotBlank(message = "{validation.lastName.notblank}")
    private String lastName;

    @NotBlank(message = "{validation.email.notblank}")
    @Email(message = "{validation.email.pattern}")
    private String email;

    @NotBlank(message = "{validation.message.notblank}")
    private String message;

    private boolean status;


    @Override
    public Contact get() {
        return Contact.builder()
                .firstName(getFirstName())
                .lastName(getLastName())
                .email(getEmail())
                .message(getMessage())
                .status(isStatus())
                .build();
    }

}

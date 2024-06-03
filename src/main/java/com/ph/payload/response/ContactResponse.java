package com.ph.payload.response;

import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String message;
    private boolean status;
    private LocalDateTime createdAt;


}

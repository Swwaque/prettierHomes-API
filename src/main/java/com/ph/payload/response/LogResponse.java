package com.ph.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogResponse {
    private Long id;

    private String message;

    private LocalDateTime createdAt;
}

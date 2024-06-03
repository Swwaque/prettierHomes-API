package com.ph.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String message;
    private boolean status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Equals and HashCode - ToString methods start
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact contact)) return false;
        return getId().equals(contact.getId())
                && getFirstName().equals(contact.getFirstName())
                && getLastName().equals(contact.getLastName())
                && getEmail().equals(contact.getEmail())
                && getMessage().equals(contact.getMessage())
                && getCreatedAt().equals(contact.getCreatedAt())
                && isStatus() == contact.isStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getEmail(), getMessage(), getCreatedAt(), isStatus());
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt + '\'' +
                ", status=" + status +
                '}';
    }

    /**
     * Equals and HashCode - ToString methods end
     */

}

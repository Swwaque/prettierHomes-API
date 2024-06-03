package com.ph.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "logs")
public class Log implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    /**
     * Entity relationships start
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advert_id")
    private Advert advert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Entity relationships end
     */

    /**
     * Equals and HashCode - ToString methods start
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Log log)) return false;
        return Objects.equals(getId(), log.getId()) && Objects.equals(getMessage(), log.getMessage()) && Objects.equals(getAdvert(),
                log.getAdvert()) && Objects.equals(getUser(), log.getUser()) && Objects.equals(getCreatedAt(), log.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMessage(), getAdvert(), getUser(), getCreatedAt());
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", message='" + message + '\'' +
                /*", advert=" + advert +
                ", user=" + user +*/
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Equals and HashCode - ToString methods end
     */

}

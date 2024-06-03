package com.ph.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "advert_types")
public class AdvertType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String title;

    /**
     * Entity relationships start
     */

    @OneToMany(mappedBy = "advertType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Advert> adverts;

    private boolean builtIn;

    /**
     * Entity relationships end
     */

    /**
     * Equals and HashCode - ToString methods start
     */
    @Override
    public String toString() {
        return "AdvertType{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AdvertType other = (AdvertType) obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(title, other.title) &&
                Objects.equals(adverts, other.adverts);
    }

    /**
     * Equals and HashCode - ToString methods end
     */
}

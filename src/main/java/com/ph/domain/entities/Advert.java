package com.ph.domain.entities;

import com.ph.domain.enums.StatusForAdvert;
import com.ph.domain.abstracts.Entry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "adverts")

public class Advert extends Entry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 300)
    private String description;

    @Column(unique = true)
    private String slug;

    @Column(nullable = false)
    private Double price;

    private StatusForAdvert statusForAdvert = StatusForAdvert.PENDING;

    private boolean builtIn;

    private boolean isActive = true;

    private Integer viewCount = 0;

    @Embedded
    private Location location;

    @Column(nullable = false, length = 100)
    private String address;

    /**
     * Entity relationships start
     */

    @ManyToOne()
    @JoinColumn(name = "advert_type_id")
    private AdvertType advertType;

    @ManyToOne()
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne()
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne()
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CategoryPropertyValue> categoryPropertyValues;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TourRequest> tourRequests;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Image> images;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Log> logs;

    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Favorite> favorites;

    /**
     * Entity relationships end
     */

    @PreRemove
    private void preRemove() {
        categoryPropertyValues.forEach(categoryPropertyValue -> categoryPropertyValue.setAdvert(null));
        tourRequests.forEach(tourRequest -> tourRequest.setAdvert(null));
        images.forEach(image -> image.setAdvert(null));
        favorites.forEach(favorite -> favorite.setAdvert(null));
        logs.forEach(log -> log.setAdvert(null));

    }


    /**
     * Equals and HashCode - ToString methods start
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Advert advert = (Advert) obj;
        return statusForAdvert == advert.statusForAdvert && builtIn == advert.builtIn && isActive == advert.isActive && viewCount == advert.viewCount && Objects.equals(id, advert.id) && Objects.equals(title, advert.title) && Objects.equals(description, advert.description) && Objects.equals(slug, advert.slug) && Objects.equals(price, advert.price) && Objects.equals(location, advert.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, slug, price, statusForAdvert, builtIn, isActive, viewCount, location);
    }

    @Override
    public String toString() {
        return "Advert{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", slug='" + slug + '\'' +
                ", price=" + price +
                ", status=" + statusForAdvert +
                ", builtIn=" + builtIn +
                ", isActive=" + isActive +
                ", viewCount=" + viewCount +
                ", location='" + location + '\'' +
                "} " + super.toString();
    }

    /**
     * Equals and HashCode - ToString methods end
     */
}

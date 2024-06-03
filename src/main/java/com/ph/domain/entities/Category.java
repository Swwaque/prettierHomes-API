package com.ph.domain.entities;

import com.ph.domain.abstracts.Entry;
import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "categories")
//@ToString(callSuper = true) // toString de bazen iliskili tablolarda recursive yapiya giriyor bunuda JsonIgnore ile cozuyoruz.
public class Category extends Entry implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String title;

    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    @Column(nullable = false, length = 50)
    private String icon;

    @Column(name = "seq", nullable = false)
    private int seq;

    @Column(name = "built_in", nullable = false)
    private boolean builtIn;

    @Column(name = "is_active")
    private boolean active;  // NOt: isActive getter methodunda problem oluşturdugundan active olarak değiştirildi.
    /**
     * Entity relationships start
     */

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Advert> adverts;


    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<CategoryPropertyKey> categoryPropertyKeys;
    /**
     * Entity relationships end
     */


    /**
     * Equals and HashCode - ToString methods start
     */

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", icon='" + icon + '\'' +
                ", seq=" + seq +
                ", builtIn=" + builtIn +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return seq == category.seq && builtIn == category.builtIn && active == category.active && Objects.equals(id, category.id) && Objects.equals(title, category.title) && Objects.equals(slug, category.slug) && Objects.equals(icon, category.icon) && Objects.equals(adverts, category.adverts) && Objects.equals(categoryPropertyKeys, category.categoryPropertyKeys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, slug, icon, seq, builtIn, active, adverts, categoryPropertyKeys);
    }

    /**
     * Equals and HashCode - ToString methods end
     */

}
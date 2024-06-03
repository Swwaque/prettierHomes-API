package com.ph.repository;

import com.ph.domain.entities.Category;
import io.micrometer.common.KeyValues;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsBySlug(String slug);

    boolean existsByTitle(String title);

    @Query("SELECT c.title FROM Category c")
    List<String> findAllTitle();

    /**
     * This method created for getting all categories which active field is true
     *
     * @param b : represent builtIn
     * @return : all builtIn categories
     */
    @Query("select c from Category c where c.builtIn = ?1")
    List<Category> findAllByBuiltIn(boolean b);

    boolean existsBy();

    @Query("SELECT c FROM Category c WHERE (:query IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Category> findAllWithQuery(@Param("query") String query, Pageable pageable);

    boolean existsByTitleIgnoreCase(String title);
}

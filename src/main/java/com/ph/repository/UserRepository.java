package com.ph.repository;

import com.ph.domain.entities.User;
import com.ph.security.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(Role role);

    Optional<User> findByEmail(@NonNull String email);

    Optional<User> findByResetPasswordCode(@NonNull String resetPasswordCode);

    @Query("""
            select u from User u
            where u.firstName ilike concat('%', ?1, '%') or u.lastName ilike concat('%', ?1, '%') or u.email ilike concat('%', ?1, '%') or u.phone ilike concat('%', ?1, '%')""")
    Page<User> findUserPageableBySearch(@Nullable String q, Pageable pageable);

    boolean existsByPhone(@NonNull String phone);

    boolean existsByEmail(@NonNull String email);

    /**
     * This  created for getting all builtIn users
     *
     * @param b : represent builtIn
     * @return : all builtIn users
     */
    @Query("select u from User u where u.builtIn = ?1")
    List<User> findAllByBuiltIn(boolean b);

    boolean existsBy();


}

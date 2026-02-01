package com.calebtl.blog.repositories;

import com.calebtl.blog.entities.User;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email);

    Optional<User> findByEmail(@NotNull String email);

    @EntityGraph(attributePaths = "profile")
    @Query(value = "select u from User u order by email")
    List<User> findAllUsersSorted();

    @EntityGraph(attributePaths = "profile")
    @Query(value = "select u from User u where u.id=:id")
    Optional<User> findUserById(@Param("id") Long id);

    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"profile", "blogPosts", "comments"})
    Optional<User> findById(@Nonnull @Param("id") Long id);
}

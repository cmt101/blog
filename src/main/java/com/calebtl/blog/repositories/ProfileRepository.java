package com.calebtl.blog.repositories;

import com.calebtl.blog.entities.Profile;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query(value = "select * from profile where user_id = :id", nativeQuery = true)
    Optional<Profile> findByUserId(@NotNull @Param("id") Long id);
}

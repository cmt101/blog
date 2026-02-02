package com.calebtl.blog.repositories;

import com.calebtl.blog.entities.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @EntityGraph(attributePaths = "blogPosts")
    Optional<Tag> findByNameAndValue(@NotNull String name, @NotNull String value);
}

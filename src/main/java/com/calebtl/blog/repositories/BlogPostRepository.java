package com.calebtl.blog.repositories;

import com.calebtl.blog.entities.BlogPost;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    boolean existsByTitle(@NotBlank(message = "Title is required") String title);

    @EntityGraph(attributePaths = {"user", "comments", "tags"})
    @Query(value = "select bp from BlogPost bp where bp.id=:id")
    Optional<BlogPost> findBlogPostById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"user", "comments", "tags"})
    @Query(value = "select bp from BlogPost bp JOIN bp.tags t where t.name=:name and t.value=:value order by bp.createdAt desc")
    List<BlogPost> findBlogPostsWithTag(@Param("name") String name, @Param("value") String value);

    @EntityGraph(attributePaths = "user")
    @Query(value = "select bp from BlogPost bp where bp.id=:id")
    Optional<BlogPost> findBlogPostByIdForDelete(@Param("id")Long id);
}

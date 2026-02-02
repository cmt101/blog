package com.calebtl.blog.controllers;

import com.calebtl.blog.dtos.*;
import com.calebtl.blog.services.BlogService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/blog")
public class BlogController {
    private BlogService blogService;

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostDto> getBlogPost(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(blogService.getBlogPostById(id));
    }

    @GetMapping("/tag/{name}/{value}")
    public ResponseEntity<List<BlogPostDto>> getBlogPostsWithTags(@PathVariable(name = "name") String name,
                                                                  @PathVariable(name = "value") String value) {
        return ResponseEntity.ok(blogService.getBlogPostByTag(name, value));
    }

    @PostMapping
    public ResponseEntity<?> createBlogPost(@Valid @RequestBody CreateBlogPostRequest data, UriComponentsBuilder uriBuilder) {
        BlogPostDto blogPostDto = blogService.createBlogPost(data);
        URI uri = uriBuilder.path("/blog/{id}").buildAndExpand(blogPostDto.getId()).toUri();
        return ResponseEntity.created(uri).body(blogPostDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostDto> updateBlogPost(@PathVariable(name = "id") Long id,
                                                      @Valid @RequestBody UpdateBlogPostRequest data) {
        return ResponseEntity.ok(blogService.updateBlogPost(id, data));
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<Void> changeLikes(@PathVariable(name = "id") Long id,
                                            @RequestParam("direction") String direction) {
        blogService.changeLikesDislikes(id, direction, true);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/dislike")
    public ResponseEntity<Void> changeDislikes(@PathVariable(name = "id") Long id,
                                               @RequestParam("direction") String direction) {
        blogService.changeLikesDislikes(id, direction, false);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/tag")
    public ResponseEntity<Void> addTag(@PathVariable(name = "id") Long id,
                                       @Valid @RequestBody AddTagRequest data) {
        blogService.addTag(id, data);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/tag/remove")
    public ResponseEntity<Void> removeTag(@PathVariable(name = "id") Long id,
                                          @Valid @RequestBody AddTagRequest data) {
        blogService.removeTag(id, data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable(name = "id") Long id) {
        blogService.deleteBlogPost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<?> addComment(@PathVariable(name = "id") Long id,
                                        @Valid @RequestBody CommentDto data,
                                        UriComponentsBuilder uriBuilder) {
        BlogPostDto blogPostDto = blogService.addComment(id, data);
        URI uri = uriBuilder.path("/blog/{id}/comment").buildAndExpand(blogPostDto.getId()).toUri();
        return ResponseEntity.created(uri).body(blogPostDto);
    }

}

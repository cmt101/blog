package com.calebtl.blog.controllers;

import com.calebtl.blog.dtos.BlogPostDto;
import com.calebtl.blog.dtos.CommentDto;
import com.calebtl.blog.dtos.CreateBlogPostRequest;
import com.calebtl.blog.dtos.UpdateBlogPostRequest;
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

    // TODO: Check this one
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

    //TODO: Add endpoints to increment/decrement likes and dislikes

    //TODO: Add endpoints to add/remove tags

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

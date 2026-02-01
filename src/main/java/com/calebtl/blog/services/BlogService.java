package com.calebtl.blog.services;

import com.calebtl.blog.dtos.BlogPostDto;
import com.calebtl.blog.dtos.CommentDto;
import com.calebtl.blog.dtos.UserDto;
import com.calebtl.blog.entities.BlogPost;
import com.calebtl.blog.entities.Comment;
import com.calebtl.blog.entities.Tag;
import com.calebtl.blog.entities.User;
import com.calebtl.blog.exceptions.BlogPostExistsException;
import com.calebtl.blog.exceptions.BlogPostNotFoundException;
import com.calebtl.blog.exceptions.UserExistsException;
import com.calebtl.blog.exceptions.UserNotFoundException;
import com.calebtl.blog.mappers.BlogPostMapper;
import com.calebtl.blog.mappers.CommentMapper;
import com.calebtl.blog.mappers.UserMapper;
import com.calebtl.blog.repositories.BlogPostRepository;
import com.calebtl.blog.repositories.CommentRepository;
import com.calebtl.blog.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class BlogService {

    private UserService userService;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private BlogPostRepository blogPostRepository;

    private BlogPostMapper blogPostMapper;
    private CommentMapper commentMapper;

    public BlogPostDto getBlogPostById(Long id) {
        BlogPost bp = blogPostRepository.findBlogPostById(id).orElseThrow(BlogPostNotFoundException::new);
        return blogPostMapper.toDto(bp);
    }

    public List<BlogPostDto> getBlogPostByTag(String name, String value) {
        List<BlogPost> bpList = blogPostRepository.findBlogPostsWithTag(name, value);

        return bpList.stream()
                .map(blogPostMapper::toDto)
                .toList();
    }

    @Transactional
    public BlogPostDto createBlogPost(BlogPostDto data) {
        if (blogPostRepository.existsByTitle(data.getTitle())) {
            throw new BlogPostExistsException();
        }

        User u = userRepository.findUserById(data.getAuthor().getId()).orElseThrow(UserNotFoundException::new);
        BlogPost bp = blogPostMapper.toEntity(data);
        u.getBlogPosts().add(bp);
        bp.setUser(u);
        blogPostRepository.save(bp);

        return blogPostMapper.toDto(bp);
    }

    // Only the owner of the blog post can update it
    @Transactional
    public BlogPostDto updateBlogPost(Long id, BlogPostDto updates) {
        BlogPost bp = blogPostRepository.findBlogPostById(id).orElseThrow(BlogPostNotFoundException::new);

        userService.currentUserOwnsResource(bp.getUser().getId());

        blogPostMapper.update(blogPostMapper.toEntity(updates), bp);
        blogPostRepository.save(bp);
        return blogPostMapper.toDto(bp);
    }

    // Only the owner of the blog post can delete it
    @Transactional
    public void deleteBlogPost(Long id) {
        BlogPost bp = blogPostRepository.findById(id).orElseThrow(BlogPostNotFoundException::new);
        userService.currentUserOwnsResource(bp.getUser().getId());
        blogPostRepository.delete(bp);
    }

    @Transactional
    public BlogPostDto addComment(Long id, CommentDto data) {
        BlogPost bp = blogPostRepository.findBlogPostById(id).orElseThrow(BlogPostNotFoundException::new);
        Comment comment = commentMapper.toEntity(data);

        comment.setUser(bp.getUser());
        comment.setBlogPost(bp);
        commentRepository.save(comment);

        bp.getComments().add(comment);
        return blogPostMapper.toDto(bp);
    }
}

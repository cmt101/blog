package com.calebtl.blog.services;

import com.calebtl.blog.dtos.BlogPostDto;
import com.calebtl.blog.dtos.CommentDto;
import com.calebtl.blog.dtos.CreateBlogPostRequest;
import com.calebtl.blog.entities.BlogPost;
import com.calebtl.blog.entities.Comment;
import com.calebtl.blog.entities.User;
import com.calebtl.blog.exceptions.BlogPostExistsException;
import com.calebtl.blog.exceptions.BlogPostNotFoundException;
import com.calebtl.blog.exceptions.UserNotFoundException;
import com.calebtl.blog.mappers.BlogPostMapper;
import com.calebtl.blog.mappers.CommentMapper;
import com.calebtl.blog.repositories.BlogPostRepository;
import com.calebtl.blog.repositories.CommentRepository;
import com.calebtl.blog.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public BlogPostDto createBlogPost(CreateBlogPostRequest data) {
        if (blogPostRepository.existsByTitle(data.getTitle())) {
            throw new BlogPostExistsException();
        }
        Long currentUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User u = userRepository.findUserById(currentUserId).orElseThrow(UserNotFoundException::new);

        // I could make another mapper specifically for the create request, but this is just 3 lines
        BlogPost bp = new BlogPost();
        bp.setTitle(data.getTitle());
        bp.setBody(data.getBody());

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

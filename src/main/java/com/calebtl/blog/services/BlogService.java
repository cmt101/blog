package com.calebtl.blog.services;

import com.calebtl.blog.dtos.*;
import com.calebtl.blog.entities.BlogPost;
import com.calebtl.blog.entities.Comment;
import com.calebtl.blog.entities.Tag;
import com.calebtl.blog.entities.User;
import com.calebtl.blog.exceptions.BlogPostExistsException;
import com.calebtl.blog.exceptions.BlogPostNotFoundException;
import com.calebtl.blog.exceptions.UserNotFoundException;
import com.calebtl.blog.mappers.BlogPostMapper;
import com.calebtl.blog.mappers.CommentMapper;
import com.calebtl.blog.repositories.BlogPostRepository;
import com.calebtl.blog.repositories.CommentRepository;
import com.calebtl.blog.repositories.TagRepository;
import com.calebtl.blog.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class BlogService {

    private UserService userService;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private BlogPostRepository blogPostRepository;
    private TagRepository tagRepository;

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

        // Not sure if this is strictly necessary
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
    public BlogPostDto updateBlogPost(Long id, UpdateBlogPostRequest updates) {

        // TODO: Add extra error handling here. Title is unique, make this query findByIdOrEmail
        //  if more than one record is returned, we can't make the update
        BlogPost bp = blogPostRepository.findBlogPostById(id).orElseThrow(BlogPostNotFoundException::new);

        userService.currentUserOwnsResource(bp.getUser().getId());

        blogPostMapper.update(blogPostMapper.toEntity(updates), bp);
        blogPostRepository.save(bp);
        return blogPostMapper.toDto(bp);
    }

    // Only the owner of the blog post can delete it
    @Transactional
    public void deleteBlogPost(Long id) {
        BlogPost bp = blogPostRepository.findBlogPostByIdForDelete(id).orElseThrow(BlogPostNotFoundException::new);
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

    @Transactional
    public void changeLikesDislikes(Long id, String direction, boolean isLikes) {
        if (!Set.of("pos", "neg").contains(direction.toLowerCase())) {
            direction = "pos";
        }

        //Probably need to optimize the query that's being used for this, but meh for now
        BlogPost bp = blogPostRepository.findBlogPostById(id).orElseThrow(BlogPostNotFoundException::new);
        if ("neg".equalsIgnoreCase(direction)) {
            if (isLikes) {
                bp.setLikes(Math.max(0, bp.getLikes()-1));
            } else {
                bp.setDislikes(Math.max(0, bp.getDislikes()-1));
            }
        } else {
            if (isLikes) {
                bp.setLikes(bp.getLikes() + 1);
            } else {
                bp.setDislikes(bp.getDislikes() + 1);
            }
        }
        blogPostRepository.save(bp);
    }

    @Transactional
    public void addTag(Long id, AddTagRequest data) {
        BlogPost bp = blogPostRepository.findBlogPostById(id).orElseThrow(BlogPostNotFoundException::new);
        userService.currentUserOwnsResource(bp.getUser().getId());

        Tag t = tagRepository.findByNameAndValue(data.getName(), data.getValue()).orElse(null);
        if (t == null) {
            t = new Tag();
            t.setName(data.getName());
            t.setValue(data.getValue());
            t.setBlogPosts(new HashSet<BlogPost>());
        }

        bp.getTags().add(t);
        t.getBlogPosts().add(bp);

        tagRepository.save(t);
        blogPostRepository.save(bp);
    }

    @Transactional
    public void removeTag(Long id, AddTagRequest data) {
        BlogPost bp = blogPostRepository.findBlogPostById(id).orElseThrow(BlogPostNotFoundException::new);
        userService.currentUserOwnsResource(bp.getUser().getId());

        // If we don't find an existing tag, no need to throw an error.
        // It is technically removed from the blog post.
        Tag t = tagRepository.findByNameAndValue(data.getName(), data.getValue()).orElse(null);
        if (t != null) {
            t.getBlogPosts().remove(bp);
            bp.getTags().remove(t);

            tagRepository.save(t);
            blogPostRepository.save(bp);
        }
    }
}

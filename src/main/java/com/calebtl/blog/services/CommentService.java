package com.calebtl.blog.services;

import com.calebtl.blog.dtos.CommentDto;
import com.calebtl.blog.entities.Comment;
import com.calebtl.blog.exceptions.CommentNotFoundException;
import com.calebtl.blog.mappers.CommentMapper;
import com.calebtl.blog.repositories.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CommentService {
    private UserService userService;
    private CommentRepository commentRepository;
    private CommentMapper commentMapper;

    // Only the owner of the comment can edit that comment
    public CommentDto updateComment(Long id, CommentDto data) {
        Comment c = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        userService.currentUserOwnsResource(c.getUser().getId());
        commentMapper.update(data, c);
        commentRepository.save(c);
        return commentMapper.toDto(c);
    }

    // Only the owner of the comment can delete that comment
    public void deleteComment(Long id) {
        Comment c = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        userService.currentUserOwnsResource(c.getUser().getId());
        commentRepository.delete(c);
    }
}

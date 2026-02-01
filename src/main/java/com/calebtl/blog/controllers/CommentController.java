package com.calebtl.blog.controllers;

import com.calebtl.blog.dtos.CommentDto;
import com.calebtl.blog.services.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentService commentService;

    // TODO: When adding authentication, check to see if the user making the request was the original poster of the comment

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> editComment(@PathVariable(name = "commentId") Long commentId,
                                                  @Valid @RequestBody CommentDto data) {
        return ResponseEntity.ok(commentService.updateComment(commentId, data));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable(name = "commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}

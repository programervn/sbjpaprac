package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.entity.Comment;
import com.thaipd.sbjpaprac.exception.ResourceNotFoundException;
import com.thaipd.sbjpaprac.repository.CommentRepository;
import com.thaipd.sbjpaprac.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/{postId}/comments")
    public Page<Comment> getAllCommentsByPostId(@PathVariable(value = "postId") Long postId,
                                                Pageable pageable) {
        logger.debug("Get all comment: postid={}, {}", postId, pageable);
        return commentRepository.findByPostId(postId, pageable);
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public Comment getCommentById(@PathVariable(value = "postId") Long postId,
                                            @PathVariable (value = "commentId") Long commentId) {
        logger.debug("Get comment by: postid={}, commentId={}", postId, commentId);
        return commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new ResourceNotFoundException("postId" + postId + "; CommentId " + commentId + "not found"));
    }

    @PostMapping("/{postId}/comments")
    public Comment createComment(@PathVariable (value = "postId") Long postId,
                                 @Valid @RequestBody Comment comment) {
        logger.debug("Create comment: postid={}, comment={}", postId, comment);
        return postRepository.findById(postId).map(post -> {
            comment.setPost(post);
            return commentRepository.save(comment);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public Comment updateComment(@PathVariable (value = "postId") Long postId,
                                 @PathVariable (value = "commentId") Long commentId,
                                 @Valid @RequestBody Comment commentRequest) {
        logger.debug("Update comment: postid={}, commentId={}, commentRequest={}", postId, commentId, commentRequest);
        if(!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("PostId " + postId + " not found");
        }

        return commentRepository.findById(commentId).map(comment -> {
            comment.setText(commentRequest.getText());
            return commentRepository.save(comment);
        }).orElseThrow(() -> new ResourceNotFoundException("CommentId " + commentId + "not found"));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable (value = "postId") Long postId,
                                           @PathVariable (value = "commentId") Long commentId) {
        logger.debug("Create comment: postid={}, commentId={}", postId, commentId);
        return commentRepository.findByIdAndPostId(commentId, postId).map(comment -> {
            commentRepository.delete(comment);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId + " and postId " + postId));
    }
}

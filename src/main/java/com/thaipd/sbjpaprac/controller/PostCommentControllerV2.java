package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.entity.PostComment;
import com.thaipd.sbjpaprac.exception.ResourceNotFoundException;
import com.thaipd.sbjpaprac.repository.CommentRepository;
import com.thaipd.sbjpaprac.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v2/posts")
public class PostCommentControllerV2 {
    private static final Logger logger = LoggerFactory.getLogger(PostCommentControllerV2.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/{postId}/comments")
    public List<PostComment> getAllCommentsByPostId(@PathVariable(value = "postId") Long postId) {
        logger.debug("Get all comment: postid={}, {}", postId);
        return commentRepository.findByPostId(postId);
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public PostComment getCommentById(@PathVariable(value = "postId") Long postId,
                                      @PathVariable (value = "commentId") Long commentId) {
        logger.debug("Get comment by: postid={}, commentId={}", postId, commentId);
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("postId" + postId + "; CommentId " + commentId + "not found"));
    }

    @PostMapping("/{postId}/comments")
    public PostComment createComment(@PathVariable (value = "postId") Long postId,
                                     @Valid @RequestBody PostComment comment) {
        logger.debug("Create comment: postid={}, comment={}", postId, comment);
        return postRepository.findById(postId).map(post -> {
            comment.setPost(post);
            return commentRepository.save(comment);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public PostComment updateComment(@PathVariable (value = "postId") Long postId,
                                     @PathVariable (value = "commentId") Long commentId,
                                     @Valid @RequestBody PostComment commentRequest) {
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

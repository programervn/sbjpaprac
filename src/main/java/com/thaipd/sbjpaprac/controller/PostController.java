package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.entity.Post;
import com.thaipd.sbjpaprac.exception.ResourceNotFoundException;
import com.thaipd.sbjpaprac.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/posts")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    @Autowired
    private PostRepository postRepository;

    //@GetMapping({"", "/"})
    @GetMapping("")
    public Page<Post> getAllPosts(Pageable pageable) {
        logger.debug("Get all post: {}", pageable);
        return postRepository.findAll(pageable);
    }

    @GetMapping("/{postId}")
    public Post getOnePosts(@PathVariable Long postId) {
        logger.debug("Get post with id: {}", postId);
        return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }

    @PostMapping("")
    public Post createPost(@Valid @RequestBody Post post) {
        logger.debug("Get post: {}", post);
        return postRepository.save(post);
    }

    @PutMapping("/{postId}")
    public Post updatePost(@PathVariable Long postId, @Valid @RequestBody Post postRequest) {
        logger.debug("Update post: id={}, post={}", postId, postRequest);
        return postRepository.findById(postId).map(post -> {
            post.setTitle(postRequest.getTitle());
            post.setDescription(postRequest.getDescription());
            post.setContent(postRequest.getContent());
            return postRepository.save(post);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        logger.debug("Delete post: id={}", postId);
        return postRepository.findById(postId).map(post -> {
            postRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + postId + " not found"));
    }
}

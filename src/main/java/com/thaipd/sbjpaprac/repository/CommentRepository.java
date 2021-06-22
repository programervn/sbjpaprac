package com.thaipd.sbjpaprac.repository;

import com.thaipd.sbjpaprac.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostId(Long postId);
    Page<PostComment> findByPostId(Long postId, Pageable pageable);
    Optional<PostComment> findByIdAndPostId(Long id, Long postId);
}

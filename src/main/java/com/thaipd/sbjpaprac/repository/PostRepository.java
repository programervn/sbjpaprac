package com.thaipd.sbjpaprac.repository;

import com.thaipd.sbjpaprac.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}

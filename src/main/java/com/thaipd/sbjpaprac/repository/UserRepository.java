package com.thaipd.sbjpaprac.repository;

import com.thaipd.sbjpaprac.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.context.annotation.Profile;

@Profile("sample")
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}

package com.thaipd.sbjpaprac.service;

import com.thaipd.sbjpaprac.dto.UserCreateRequest;
import com.thaipd.sbjpaprac.dto.UserResponse;
import com.thaipd.sbjpaprac.dto.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
	UserResponse create(UserCreateRequest request);
	UserResponse getById(long id);
	Page<UserResponse> list(Pageable pageable);
	UserResponse update(long id, UserUpdateRequest request);
	void delete(long id);
}

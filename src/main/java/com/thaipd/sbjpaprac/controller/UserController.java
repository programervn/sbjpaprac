package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.common.api.ApiResponse;
import com.thaipd.sbjpaprac.dto.UserCreateRequest;
import com.thaipd.sbjpaprac.dto.UserResponse;
import com.thaipd.sbjpaprac.dto.UserUpdateRequest;
import com.thaipd.sbjpaprac.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
		logger.info("UserController initialized");
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
		logger.info("Creating new user with email: {}", request.getEmail());
		UserResponse response = userService.create(request);
		logger.debug("Created user with ID: {}", response.getId());
		return ApiResponse.ok(response);
	}

	@GetMapping("/{id}")
	public ApiResponse<UserResponse> getById(@PathVariable long id) {
		logger.debug("Fetching user with ID: {}", id);
		UserResponse response = userService.getById(id);
		if (response == null) {
			logger.warn("User with ID {} not found", id);
		} else {
			logger.debug("Found user with ID: {}", id);
		}
		return ApiResponse.ok(response);
	}

	@GetMapping
	public ApiResponse<Page<UserResponse>> list(Pageable pageable) {
		logger.debug("Fetching user list with page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<UserResponse> response = userService.list(pageable);
		logger.debug("Found {} users on page {}", response.getNumberOfElements(), pageable.getPageNumber());
		return ApiResponse.ok(response);
	}

	@PutMapping("/{id}")
	public ApiResponse<UserResponse> update(@PathVariable long id, @Valid @RequestBody UserUpdateRequest request) {
		logger.info("Updating user with ID: {}", id);
		UserResponse response = userService.update(id, request);
		logger.debug("Successfully updated user with ID: {}", id);
		return ApiResponse.ok(response);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable long id) {
		logger.info("Deleting user with ID: {}", id);
		userService.delete(id);
		logger.debug("Successfully deleted user with ID: {}", id);
	}
}

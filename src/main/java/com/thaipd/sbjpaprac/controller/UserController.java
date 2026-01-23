package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.common.api.ApiResponse;
import com.thaipd.sbjpaprac.dto.UserCreateRequest;
import com.thaipd.sbjpaprac.dto.UserResponse;
import com.thaipd.sbjpaprac.dto.UserUpdateRequest;
import com.thaipd.sbjpaprac.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
		log.info("Creating new user with email: {}", request.getEmail());
		UserResponse response = userService.create(request);
		log.debug("Created user with ID: {}", response.getId());
		return ApiResponse.ok(response);
	}

	@GetMapping("/{id}")
	public ApiResponse<UserResponse> getById(@PathVariable long id) {
		log.debug("Fetching user with ID: {}", id);
		UserResponse response = userService.getById(id);
		if (response == null) {
			log.warn("User with ID {} not found", id);
		} else {
			log.debug("Found user with ID: {}", id);
		}
		return ApiResponse.ok(response);
	}

	@GetMapping
	public ApiResponse<Page<UserResponse>> list(Pageable pageable) {
		log.debug("Fetching user list with page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
		Page<UserResponse> response = userService.list(pageable);
		log.debug("Found {} users on page {}", response.getNumberOfElements(), pageable.getPageNumber());
		return ApiResponse.ok(response);
	}

	@PutMapping("/{id}")
	public ApiResponse<UserResponse> update(@PathVariable long id, @Valid @RequestBody UserUpdateRequest request) {
		log.info("Updating user with ID: {}", id);
		UserResponse response = userService.update(id, request);
		log.debug("Successfully updated user with ID: {}", id);
		return ApiResponse.ok(response);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable long id) {
		log.info("Deleting user with ID: {}", id);
		userService.delete(id);
		log.debug("Successfully deleted user with ID: {}", id);
	}
}

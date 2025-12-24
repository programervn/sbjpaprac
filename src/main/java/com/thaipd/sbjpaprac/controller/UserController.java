package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.common.api.ApiResponse;
import com.thaipd.sbjpaprac.dto.UserCreateRequest;
import com.thaipd.sbjpaprac.dto.UserResponse;
import com.thaipd.sbjpaprac.dto.UserUpdateRequest;
import com.thaipd.sbjpaprac.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
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
@Profile("sample")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
		return ApiResponse.ok(userService.create(request));
	}

	@GetMapping("/{id}")
	public ApiResponse<UserResponse> getById(@PathVariable long id) {
		return ApiResponse.ok(userService.getById(id));
	}

	@GetMapping
	public ApiResponse<Page<UserResponse>> list(Pageable pageable) {
		return ApiResponse.ok(userService.list(pageable));
	}

	@PutMapping("/{id}")
	public ApiResponse<UserResponse> update(@PathVariable long id, @Valid @RequestBody UserUpdateRequest request) {
		return ApiResponse.ok(userService.update(id, request));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable long id) {
		userService.delete(id);
	}
}

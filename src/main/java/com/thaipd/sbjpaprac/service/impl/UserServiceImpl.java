package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.common.exception.BadRequestException;
import com.thaipd.sbjpaprac.common.exception.NotFoundException;
import com.thaipd.sbjpaprac.dto.UserCreateRequest;
import com.thaipd.sbjpaprac.dto.UserResponse;
import com.thaipd.sbjpaprac.dto.UserUpdateRequest;
import com.thaipd.sbjpaprac.entity.User;
import com.thaipd.sbjpaprac.repository.UserRepository;
import com.thaipd.sbjpaprac.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("sample")
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserResponse create(UserCreateRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new BadRequestException("Username already exists");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new BadRequestException("Email already exists");
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		return toResponse(userRepository.save(user));
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponse getById(long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User not found"));
		return toResponse(user);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserResponse> list(Pageable pageable) {
		return userRepository.findAll(pageable).map(this::toResponse);
	}

	@Override
	@Transactional
	public UserResponse update(long id, UserUpdateRequest request) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("User not found"));

		if (request.getUsername() != null && !request.getUsername().isBlank()) {
			if (!request.getUsername().equals(user.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
				throw new BadRequestException("Username already exists");
			}
			user.setUsername(request.getUsername());
		}

		if (request.getEmail() != null && !request.getEmail().isBlank()) {
			if (!request.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
				throw new BadRequestException("Email already exists");
			}
			user.setEmail(request.getEmail());
		}

		return toResponse(userRepository.save(user));
	}

	@Override
	@Transactional
	public void delete(long id) {
		if (!userRepository.existsById(id)) {
			throw new NotFoundException("User not found");
		}
		userRepository.deleteById(id);
	}

	private UserResponse toResponse(User user) {
		UserResponse r = new UserResponse();
		r.setId(user.getId());
		r.setUsername(user.getUsername());
		r.setEmail(user.getEmail());
		r.setCreatedAt(user.getCreatedAt());
		r.setUpdatedAt(user.getUpdatedAt());
		return r;
	}
}

package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.common.exception.BadRequestException;
import com.thaipd.sbjpaprac.common.exception.NotFoundException;
import com.thaipd.sbjpaprac.dto.UserCreateRequest;
import com.thaipd.sbjpaprac.dto.UserResponse;
import com.thaipd.sbjpaprac.dto.UserUpdateRequest;
import com.thaipd.sbjpaprac.entity.User;
import com.thaipd.sbjpaprac.repository.UserRepository;
import com.thaipd.sbjpaprac.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
		log.info("UserServiceImpl initialized with UserRepository: {}", userRepository.getClass().getSimpleName());
	}

	@Override
	@Transactional
	public UserResponse create(UserCreateRequest request) {
		log.debug("Creating new user with username: {}, email: {}", request.getUsername(), request.getEmail());

		if (userRepository.existsByUsername(request.getUsername())) {
			String errorMsg = String.format("Username '%s' already exists", request.getUsername());
			log.warn(errorMsg);
			throw new BadRequestException(errorMsg);
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			String errorMsg = String.format("Email '%s' already exists", request.getEmail());
			log.warn(errorMsg);
			throw new BadRequestException(errorMsg);
		}

		try {
			User user = new User();
			user.setUsername(request.getUsername());
			user.setEmail(request.getEmail());

			User savedUser = userRepository.save(user);
			if (savedUser != null) {
				log.info("Successfully created user with ID: {}, username: {}",
						savedUser.getId(), savedUser.getUsername());
			} else {
				log.error("Failed to create user - saved user is null");
			}
			return toResponse(savedUser);
		} catch (Exception e) {
			log.error("Error creating user: {}", e.getMessage(), e);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponse getById(long id) {
		log.debug("Fetching user by ID: {}", id);
		try {
			User user = userRepository.findById(id)
					.orElseThrow(() -> {
						String errorMsg = String.format("User with ID %d not found", id);
						log.warn(errorMsg);
						return new NotFoundException(errorMsg);
					});
			if (log.isDebugEnabled() && user != null) {
				log.debug("Successfully retrieved user with ID: {}", id);
			}
			return toResponse(user);
		} catch (Exception e) {
			log.error("Error fetching user with ID {}: {}", id, e.getMessage(), e);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserResponse> list(Pageable pageable) {
		log.debug("Fetching user list with page: {}, size: {}",
				pageable.getPageNumber(), pageable.getPageSize());

		try {
			Page<UserResponse> result = userRepository.findAll(pageable).map(this::toResponse);
			if (log.isDebugEnabled() && result != null) {
				log.debug("Retrieved {} users out of {} total",
						result.getNumberOfElements(), result.getTotalElements());
			}
			return result;
		} catch (Exception e) {
			log.error("Error fetching user list: {}", e.getMessage(), e);
			throw e;
		}
	}

	@Override
	@Transactional
	public UserResponse update(long id, UserUpdateRequest request) {
		log.debug("Updating user with ID: {}", id);
		try {
			User user = userRepository.findById(id)
					.orElseThrow(() -> {
						String errorMsg = String.format("User with ID %d not found for update", id);
						log.warn(errorMsg);
						return new NotFoundException(errorMsg);
					});

			boolean updated = false;

			if (request.getUsername() != null && !request.getUsername().isBlank() &&
					!request.getUsername().equals(user.getUsername())) {
				if (userRepository.existsByUsername(request.getUsername())) {
					String errorMsg = String.format("Username '%s' already exists", request.getUsername());
					log.warn(errorMsg);
					throw new BadRequestException(errorMsg);
				}
				log.debug("Updating username for user ID: {}", id);
				user.setUsername(request.getUsername());
				updated = true;
			}

			if (request.getEmail() != null && !request.getEmail().isBlank() &&
					!request.getEmail().equals(user.getEmail())) {
				if (userRepository.existsByEmail(request.getEmail())) {
					String errorMsg = String.format("Email '%s' already exists", request.getEmail());
					log.warn(errorMsg);
					throw new BadRequestException(errorMsg);
				}
				log.debug("Updating email for user ID: {}", id);
				user.setEmail(request.getEmail());
				updated = true;
			}

			if (updated) {
				User updatedUser = userRepository.save(user);
				if (updatedUser != null) {
					log.info("Successfully updated user with ID: {}", id);
				} else {
					log.error("Failed to update user - updated user is null for ID: {}", id);
				}
				return toResponse(updatedUser);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("No changes detected for user ID: {}", id);
				}
				return toResponse(user);
			}
		} catch (Exception e) {
			log.error("Error updating user with ID {}: {}", id, e.getMessage(), e);
			throw e;
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		log.debug("Deleting user with ID: {}", id);
		try {
			if (!userRepository.existsById(id)) {
				String errorMsg = String.format("User with ID %d not found for deletion", id);
				log.warn(errorMsg);
				throw new NotFoundException(errorMsg);
			}

			userRepository.deleteById(id);
			if (log.isInfoEnabled()) {
				log.info("Successfully deleted user with ID: {}", id);
			}
		} catch (Exception e) {
			log.error("Error deleting user with ID {}: {}", id, e.getMessage(), e);
			throw e;
		}
	}

	private UserResponse toResponse(User user) {
		try {
			if (user == null) {
				if (log.isWarnEnabled()) {
					log.warn("Attempted to convert null User to UserResponse");
				}
				return null;
			}

			UserResponse r = new UserResponse();
			r.setId(user.getId());
			r.setUsername(user.getUsername());
			r.setEmail(user.getEmail());
			r.setCreatedAt(user.getCreatedAt());
			r.setUpdatedAt(user.getUpdatedAt());
			return r;
		} catch (Exception e) {
			log.error("Error converting User to UserResponse: {}", e.getMessage(), e);
			throw e;
		}
	}
}

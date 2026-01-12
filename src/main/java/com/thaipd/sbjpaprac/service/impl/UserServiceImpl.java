package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.common.exception.BadRequestException;
import com.thaipd.sbjpaprac.common.exception.NotFoundException;
import com.thaipd.sbjpaprac.dto.UserCreateRequest;
import com.thaipd.sbjpaprac.dto.UserResponse;
import com.thaipd.sbjpaprac.dto.UserUpdateRequest;
import com.thaipd.sbjpaprac.entity.User;
import com.thaipd.sbjpaprac.repository.UserRepository;
import com.thaipd.sbjpaprac.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
		logger.info("UserServiceImpl initialized with UserRepository: {}", userRepository.getClass().getSimpleName());
	}

	@Override
	@Transactional
	public UserResponse create(UserCreateRequest request) {
		logger.debug("Creating new user with username: {}, email: {}", request.getUsername(), request.getEmail());

		if (userRepository.existsByUsername(request.getUsername())) {
			String errorMsg = String.format("Username '%s' already exists", request.getUsername());
			logger.warn(errorMsg);
			throw new BadRequestException(errorMsg);
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			String errorMsg = String.format("Email '%s' already exists", request.getEmail());
			logger.warn(errorMsg);
			throw new BadRequestException(errorMsg);
		}

		try {
			User user = new User();
			user.setUsername(request.getUsername());
			user.setEmail(request.getEmail());

			User savedUser = userRepository.save(user);
			if (savedUser != null) {
				logger.info("Successfully created user with ID: {}, username: {}",
						savedUser.getId(), savedUser.getUsername());
			} else {
				logger.error("Failed to create user - saved user is null");
			}
			return toResponse(savedUser);
		} catch (Exception e) {
			logger.error("Error creating user: {}", e.getMessage(), e);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponse getById(long id) {
		logger.debug("Fetching user by ID: {}", id);
		try {
			User user = userRepository.findById(id)
					.orElseThrow(() -> {
						String errorMsg = String.format("User with ID %d not found", id);
						logger.warn(errorMsg);
						return new NotFoundException(errorMsg);
					});
			if (logger.isDebugEnabled() && user != null) {
				logger.debug("Successfully retrieved user with ID: {}", id);
			}
			return toResponse(user);
		} catch (Exception e) {
			logger.error("Error fetching user with ID {}: {}", id, e.getMessage(), e);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserResponse> list(Pageable pageable) {
		logger.debug("Fetching user list with page: {}, size: {}",
				pageable.getPageNumber(), pageable.getPageSize());

		try {
			Page<UserResponse> result = userRepository.findAll(pageable).map(this::toResponse);
			if (logger.isDebugEnabled() && result != null) {
				logger.debug("Retrieved {} users out of {} total",
						result.getNumberOfElements(), result.getTotalElements());
			}
			return result;
		} catch (Exception e) {
			logger.error("Error fetching user list: {}", e.getMessage(), e);
			throw e;
		}
	}

	@Override
	@Transactional
	public UserResponse update(long id, UserUpdateRequest request) {
		logger.debug("Updating user with ID: {}", id);
		try {
			User user = userRepository.findById(id)
					.orElseThrow(() -> {
						String errorMsg = String.format("User with ID %d not found for update", id);
						logger.warn(errorMsg);
						return new NotFoundException(errorMsg);
					});

			boolean updated = false;

			if (request.getUsername() != null && !request.getUsername().isBlank() &&
					!request.getUsername().equals(user.getUsername())) {
				if (userRepository.existsByUsername(request.getUsername())) {
					String errorMsg = String.format("Username '%s' already exists", request.getUsername());
					logger.warn(errorMsg);
					throw new BadRequestException(errorMsg);
				}
				logger.debug("Updating username for user ID: {}", id);
				user.setUsername(request.getUsername());
				updated = true;
			}

			if (request.getEmail() != null && !request.getEmail().isBlank() &&
					!request.getEmail().equals(user.getEmail())) {
				if (userRepository.existsByEmail(request.getEmail())) {
					String errorMsg = String.format("Email '%s' already exists", request.getEmail());
					logger.warn(errorMsg);
					throw new BadRequestException(errorMsg);
				}
				logger.debug("Updating email for user ID: {}", id);
				user.setEmail(request.getEmail());
				updated = true;
			}

			if (updated) {
				User updatedUser = userRepository.save(user);
				if (updatedUser != null) {
					logger.info("Successfully updated user with ID: {}", id);
				} else {
					logger.error("Failed to update user - updated user is null for ID: {}", id);
				}
				return toResponse(updatedUser);
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("No changes detected for user ID: {}", id);
				}
				return toResponse(user);
			}
		} catch (Exception e) {
			logger.error("Error updating user with ID {}: {}", id, e.getMessage(), e);
			throw e;
		}
	}

	@Override
	@Transactional
	public void delete(long id) {
		logger.debug("Deleting user with ID: {}", id);
		try {
			if (!userRepository.existsById(id)) {
				String errorMsg = String.format("User with ID %d not found for deletion", id);
				logger.warn(errorMsg);
				throw new NotFoundException(errorMsg);
			}

			userRepository.deleteById(id);
			if (logger.isInfoEnabled()) {
				logger.info("Successfully deleted user with ID: {}", id);
			}
		} catch (Exception e) {
			logger.error("Error deleting user with ID {}: {}", id, e.getMessage(), e);
			throw e;
		}
	}

	private UserResponse toResponse(User user) {
		try {
			if (user == null) {
				if (logger.isWarnEnabled()) {
					logger.warn("Attempted to convert null User to UserResponse");
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
			logger.error("Error converting User to UserResponse: {}", e.getMessage(), e);
			throw e;
		}
	}
}

package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.dto.UserResponse;
import com.thaipd.sbjpaprac.entity.User;
import com.thaipd.sbjpaprac.repository.UserRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private Validator validator;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getById_WhenExists_ReturnsResponse() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");
        when(repository.findById(id)).thenReturn(Optional.of(user));

        UserResponse result = userService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("johndoe", result.getUsername());
    }
}

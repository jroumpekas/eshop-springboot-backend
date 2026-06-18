package gr.aueb.cf.eshop_app.service.impl;

import gr.aueb.cf.eshop_app.dto.UserInsertDTO;
import gr.aueb.cf.eshop_app.dto.UserReadOnlyDTO;
import gr.aueb.cf.eshop_app.mapper.UserMapper;
import gr.aueb.cf.eshop_app.models.User;
import gr.aueb.cf.eshop_app.models.enums.Role;
import gr.aueb.cf.eshop_app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsersReturnsMappedUsers() {
        // Arrange
        User user1 = createUser(1L, "dimitris", "dimitris@test.com");
        User user2 = createUser(2L, "maria", "maria@test.com");

        UserReadOnlyDTO dto1 = createReadOnlyDTO(user1);
        UserReadOnlyDTO dto2 = createReadOnlyDTO(user2);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.mapToReadOnlyDTO(user1)).thenReturn(dto1);
        when(userMapper.mapToReadOnlyDTO(user2)).thenReturn(dto2);

        // Act
        List<UserReadOnlyDTO> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));

        verify(userRepository).findAll();
        verify(userMapper).mapToReadOnlyDTO(user1);
        verify(userMapper).mapToReadOnlyDTO(user2);
    }

    @Test
    void getUserByIdWithExistingIdReturnsUser() {
        // Arrange
        Long userId = 1L;

        User user = createUser(userId, "dimitris", "dimitris@test.com");
        UserReadOnlyDTO expectedDTO = createReadOnlyDTO(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.mapToReadOnlyDTO(user)).thenReturn(expectedDTO);

        // Act
        UserReadOnlyDTO result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertSame(expectedDTO, result);

        verify(userRepository).findById(userId);
        verify(userMapper).mapToReadOnlyDTO(user);
    }

    @Test
    void getUserByIdWithUnknownIdThrowsEntityNotFoundException() {
        // Arrange
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getUserById(userId)
        );

        // Assert
        assertEquals("User with id 99 was not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void getUserByUsernameWithExistingUsernameReturnsUser() {
        // Arrange
        String username = "dimitris";

        User user = createUser(1L, username, "dimitris@test.com");
        UserReadOnlyDTO expectedDTO = createReadOnlyDTO(user);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userMapper.mapToReadOnlyDTO(user)).thenReturn(expectedDTO);

        // Act
        UserReadOnlyDTO result = userService.getUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertSame(expectedDTO, result);

        verify(userRepository).findByUsername(username);
        verify(userMapper).mapToReadOnlyDTO(user);
    }

    @Test
    void getUserByUsernameWithUnknownUsernameThrowsEntityNotFoundException() {
        // Arrange
        String username = "unknown";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getUserByUsername(username)
        );

        // Assert
        assertEquals("User with username unknown was not found", exception.getMessage());

        verify(userRepository).findByUsername(username);
        verifyNoInteractions(userMapper);
    }

    @Test
    void registerUserWithValidDataCreatesUser() {
        // Arrange
        UserInsertDTO insertDTO = UserInsertDTO.builder()
                .username("dimitris")
                .email("dimitris@test.com")
                .password("Password1!")
                .firstName("Dimitris")
                .lastName("Roumpis")
                .build();

        String encodedPassword = "encoded-password";

        User userToSave = User.builder()
                .username(insertDTO.getUsername())
                .email(insertDTO.getEmail())
                .password(encodedPassword)
                .firstName(insertDTO.getFirstName())
                .lastName(insertDTO.getLastName())
                .role(Role.USER)
                .build();

        User savedUser = User.builder()
                .id(1L)
                .username(insertDTO.getUsername())
                .email(insertDTO.getEmail())
                .password(encodedPassword)
                .firstName(insertDTO.getFirstName())
                .lastName(insertDTO.getLastName())
                .role(Role.USER)
                .build();

        UserReadOnlyDTO expectedDTO = createReadOnlyDTO(savedUser);

        when(userRepository.existsByUsername(insertDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(insertDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(insertDTO.getPassword())).thenReturn(encodedPassword);
        when(userMapper.mapToUser(insertDTO, encodedPassword)).thenReturn(userToSave);
        when(userRepository.save(userToSave)).thenReturn(savedUser);
        when(userMapper.mapToReadOnlyDTO(savedUser)).thenReturn(expectedDTO);

        // Act
        UserReadOnlyDTO result = userService.registerUser(insertDTO);

        // Assert
        assertNotNull(result);
        assertSame(expectedDTO, result);

        verify(userRepository).existsByUsername(insertDTO.getUsername());
        verify(userRepository).existsByEmail(insertDTO.getEmail());
        verify(passwordEncoder).encode(insertDTO.getPassword());
        verify(userMapper).mapToUser(insertDTO, encodedPassword);
        verify(userRepository).save(userToSave);
        verify(userMapper).mapToReadOnlyDTO(savedUser);
    }

    @Test
    void registerUserWithDuplicateUsernameThrowsIllegalArgumentException() {
        // Arrange
        UserInsertDTO insertDTO = createUserInsertDTO();

        when(userRepository.existsByUsername(insertDTO.getUsername())).thenReturn(true);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(insertDTO)
        );

        // Assert
        assertEquals("Username already exists", exception.getMessage());

        verify(userRepository).existsByUsername(insertDTO.getUsername());
        verify(userRepository, never()).existsByEmail(anyString());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(userMapper);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUserWithDuplicateEmailThrowsIllegalArgumentException() {
        // Arrange
        UserInsertDTO insertDTO = createUserInsertDTO();

        when(userRepository.existsByUsername(insertDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(insertDTO.getEmail())).thenReturn(true);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(insertDTO)
        );

        // Assert
        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository).existsByUsername(insertDTO.getUsername());
        verify(userRepository).existsByEmail(insertDTO.getEmail());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(userMapper);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUserWithExistingIdDeletesUser() {
        // Arrange
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteUserWithUnknownIdThrowsEntityNotFoundException() {
        // Arrange
        Long userId = 99L;

        when(userRepository.existsById(userId)).thenReturn(false);

        // Act
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.deleteUser(userId)
        );

        // Assert
        assertEquals("User with id 99 was not found", exception.getMessage());

        verify(userRepository).existsById(userId);
        verify(userRepository, never()).deleteById(userId);
    }

    private UserInsertDTO createUserInsertDTO() {
        return UserInsertDTO.builder()
                .username("dimitris")
                .email("dimitris@test.com")
                .password("Password1!")
                .firstName("Dimitris")
                .lastName("Roumpis")
                .build();
    }

    private User createUser(Long id, String username, String email) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password("encoded-password")
                .firstName("Test")
                .lastName("User")
                .role(Role.USER)
                .build();
    }

    private UserReadOnlyDTO createReadOnlyDTO(User user) {
        return UserReadOnlyDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }
}
package gr.aueb.cf.eshop_app.controller;

import gr.aueb.cf.eshop_app.dto.UserReadOnlyDTO;
import gr.aueb.cf.eshop_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserReadOnlyDTO> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();

        UserReadOnlyDTO user = userService.getUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserReadOnlyDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserReadOnlyDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
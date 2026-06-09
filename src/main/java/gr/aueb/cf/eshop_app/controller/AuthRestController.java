package gr.aueb.cf.eshop_app.controller;

import gr.aueb.cf.eshop_app.dto.AuthResponseDTO;
import gr.aueb.cf.eshop_app.dto.LoginDTO;
import gr.aueb.cf.eshop_app.dto.UserInsertDTO;
import gr.aueb.cf.eshop_app.dto.UserReadOnlyDTO;
import gr.aueb.cf.eshop_app.service.AuthService;
import gr.aueb.cf.eshop_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserReadOnlyDTO> registerUser(
            @Valid @RequestBody UserInsertDTO dto
    ) {
        UserReadOnlyDTO user = userService.registerUser(dto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginDTO dto
    ) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Auth controller works");
    }
}
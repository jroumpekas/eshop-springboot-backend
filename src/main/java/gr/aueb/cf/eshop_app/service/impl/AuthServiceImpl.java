package gr.aueb.cf.eshop_app.service.impl;

import gr.aueb.cf.eshop_app.dto.AuthResponseDTO;
import gr.aueb.cf.eshop_app.dto.LoginDTO;
import gr.aueb.cf.eshop_app.models.User;
import gr.aueb.cf.eshop_app.repository.UserRepository;
import gr.aueb.cf.eshop_app.security.JwtService;
import gr.aueb.cf.eshop_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginDTO dto) {
        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        boolean passwordMatches = passwordEncoder.matches(
                dto.password(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getUsername());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getId()) // <--- UUID
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
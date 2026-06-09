package gr.aueb.cf.eshop_app.service;

import gr.aueb.cf.eshop_app.dto.AuthResponseDTO;
import gr.aueb.cf.eshop_app.dto.LoginDTO;
import jakarta.validation.Valid;

public interface AuthService {
    AuthResponseDTO login(@Valid LoginDTO dto);
}

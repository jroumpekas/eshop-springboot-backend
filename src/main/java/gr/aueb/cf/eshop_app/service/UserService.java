package gr.aueb.cf.eshop_app.service;

import gr.aueb.cf.eshop_app.dto.UserInsertDTO;
import gr.aueb.cf.eshop_app.dto.UserReadOnlyDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserReadOnlyDTO> getAllUsers();

    UserReadOnlyDTO getUserById(UUID id);

    UserReadOnlyDTO getUserByUsername(String username);

    UserReadOnlyDTO registerUser(UserInsertDTO dto);

    void deleteUser(UUID id);
}

package gr.aueb.cf.eshop_app.service;

import gr.aueb.cf.eshop_app.dto.UserInsertDTO;
import gr.aueb.cf.eshop_app.dto.UserReadOnlyDTO;

import java.util.List;

public interface UserService {

    List<UserReadOnlyDTO> getAllUsers();

    UserReadOnlyDTO getUserById(Long id);

    UserReadOnlyDTO getUserByUsername(String username);

    UserReadOnlyDTO registerUser(UserInsertDTO dto);

    void deleteUser(Long id);
}

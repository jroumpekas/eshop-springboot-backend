package gr.aueb.cf.eshop_app.mapper;

import gr.aueb.cf.eshop_app.dto.UserInsertDTO;
import gr.aueb.cf.eshop_app.dto.UserReadOnlyDTO;
import gr.aueb.cf.eshop_app.models.enums.Role;
import gr.aueb.cf.eshop_app.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToUser(UserInsertDTO dto, String encodedPassword) {
        return User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(encodedPassword)
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .role(Role.USER)
                .build();
    }

    public UserReadOnlyDTO mapToReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name()
        );
    }
}
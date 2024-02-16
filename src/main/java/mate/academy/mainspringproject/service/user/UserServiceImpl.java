package mate.academy.mainspringproject.service.user;

import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.dto.user.UserRegistrationRequestDto;
import mate.academy.mainspringproject.dto.user.UserResponseDto;
import mate.academy.mainspringproject.exception.EntityNotFoundException;
import mate.academy.mainspringproject.mappers.UserMapper;
import mate.academy.mainspringproject.model.Role;
import mate.academy.mainspringproject.model.User;
import mate.academy.mainspringproject.repository.role.RoleRepository;
import mate.academy.mainspringproject.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Override
    public UserResponseDto save(UserRegistrationRequestDto requestDto) {
        User userForRegistration = userMapper.toModel(requestDto);
        String encodedPassword = passwordEncoder.encode(userForRegistration.getPassword());
        userForRegistration.setPassword(encodedPassword);
        Role role = roleRepository.findAll().stream()
                .filter(r -> r.getName().equals(Role.RoleName.ROLE_USER))
                .findFirst().orElseThrow(
                        () -> new EntityNotFoundException("Can't find role in DB")
                );
        userForRegistration.setRoles(Set.of(role));
        return userMapper.toDto(userRepository.save(userForRegistration));
    }
}

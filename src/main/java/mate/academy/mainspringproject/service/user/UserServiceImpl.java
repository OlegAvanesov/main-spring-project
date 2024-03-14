package mate.academy.mainspringproject.service.user;

import java.util.Set;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponseDto save(UserRegistrationRequestDto requestDto) {
        User userForRegistration = userMapper.toModel(requestDto);

        String encodedPassword = passwordEncoder.encode(userForRegistration.getPassword());
        userForRegistration.setPassword(encodedPassword);

        Role role = getRoleByName(Role.RoleName.ROLE_USER);
        userForRegistration.setRoles(Set.of(role));

        return userMapper.toDto(userRepository.save(userForRegistration));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find user with this id: " + id));
    }

    private Role getRoleByName(Role.RoleName roleName) {
        return roleRepository.findAll().stream()
                .filter(role -> role.getName().equals(roleName))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find role " + roleName + " in DB")
                );
    }
}

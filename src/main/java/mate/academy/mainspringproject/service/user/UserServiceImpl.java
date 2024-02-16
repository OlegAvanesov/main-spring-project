package mate.academy.mainspringproject.service.user;

import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.dto.user.UserRegistrationRequestDto;
import mate.academy.mainspringproject.dto.user.UserResponseDto;
import mate.academy.mainspringproject.mappers.UserMapper;
import mate.academy.mainspringproject.model.User;
import mate.academy.mainspringproject.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto save(UserRegistrationRequestDto requestDto) {
        User userForRegistration = userMapper.toModel(requestDto);
        String encodedPassword = passwordEncoder.encode(userForRegistration.getPassword());
        userForRegistration.setPassword(encodedPassword);
        return userMapper.toDto(userRepository.save(userForRegistration));
    }
}

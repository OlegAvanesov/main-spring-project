package mate.academy.mainspringproject.service.userservice;

import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.dto.user.UserRegistrationRequestDto;
import mate.academy.mainspringproject.dto.user.UserResponseDto;
import mate.academy.mainspringproject.mappers.UserMapper;
import mate.academy.mainspringproject.model.User;
import mate.academy.mainspringproject.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto save(UserRegistrationRequestDto requestDto) {
        User userForRegistration = userMapper.toModel(requestDto);
        return userMapper.toDto(userRepository.save(userForRegistration));
    }
}

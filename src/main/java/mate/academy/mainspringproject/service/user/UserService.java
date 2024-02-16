package mate.academy.mainspringproject.service.user;

import mate.academy.mainspringproject.dto.user.UserRegistrationRequestDto;
import mate.academy.mainspringproject.dto.user.UserResponseDto;

public interface UserService {

    UserResponseDto save(UserRegistrationRequestDto requestDto);
}

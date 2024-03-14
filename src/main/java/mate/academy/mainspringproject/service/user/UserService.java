package mate.academy.mainspringproject.service.user;

import mate.academy.mainspringproject.dto.user.UserRegistrationRequestDto;
import mate.academy.mainspringproject.dto.user.UserResponseDto;
import mate.academy.mainspringproject.model.User;

public interface UserService {

    UserResponseDto save(UserRegistrationRequestDto requestDto);

    User findByEmail(String email);

    User findById(Long id);
}

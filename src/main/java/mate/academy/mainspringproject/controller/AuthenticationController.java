package mate.academy.mainspringproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.dto.user.UserLoginRequestDto;
import mate.academy.mainspringproject.dto.user.UserLoginResponseDto;
import mate.academy.mainspringproject.dto.user.UserRegistrationRequestDto;
import mate.academy.mainspringproject.dto.user.UserResponseDto;
import mate.academy.mainspringproject.exception.RegistrationException;
import mate.academy.mainspringproject.security.AuthenticationService;
import mate.academy.mainspringproject.service.userservice.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.save(requestDto);
    }
}

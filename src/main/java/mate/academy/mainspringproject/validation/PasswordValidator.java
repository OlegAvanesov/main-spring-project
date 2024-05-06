package mate.academy.mainspringproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import mate.academy.mainspringproject.dto.user.UserRegistrationRequestDto;

public class PasswordValidator
        implements ConstraintValidator<PasswordMatch, UserRegistrationRequestDto> {
    @Override
    public boolean isValid(UserRegistrationRequestDto userRegistrationRequestDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(userRegistrationRequestDto.getPassword(),
                userRegistrationRequestDto.getRepeatPassword());
    }
}

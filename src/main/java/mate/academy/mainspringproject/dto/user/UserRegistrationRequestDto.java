package mate.academy.mainspringproject.dto.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.mainspringproject.validation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(first = "password", second = "repeatPassword", message = "Passwords should match")
public class UserRegistrationRequestDto {
    @Column(nullable = false, unique = true)
    @Email
    private String email;
    @NotNull
    @Length(min = 8, max = 20)
    private String password;
    @NotNull
    @Length(min = 8, max = 20)
    private String repeatPassword;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String shippingAddress;
}

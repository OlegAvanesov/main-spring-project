package mate.academy.mainspringproject.dto.user;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private String shippingAddress;
}

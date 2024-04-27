package mate.academy.mainspringproject.dto.cartitem;

import jakarta.validation.constraints.Positive;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CartItemRequestDto {
    @NotEmpty
    private Long bookId;
    @NotEmpty
    @Positive
    private int quantity;
}

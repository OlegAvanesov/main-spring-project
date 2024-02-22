package mate.academy.mainspringproject.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @NotNull
    private Long bookId;
    @NotNull
    @Positive
    private int quantity;
}

package mate.academy.mainspringproject.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import mate.academy.mainspringproject.dto.cartitem.CartItemResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}

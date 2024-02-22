package mate.academy.mainspringproject.service.shoppingcart;

import mate.academy.mainspringproject.dto.cartitem.CartItemRequestDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemResponseDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.mainspringproject.dto.shoppingcart.ShoppingCartResponseDto;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartResponseDto getAllInfo(Authentication authentication);

    CartItemResponseDto addBookToCart(CartItemRequestDto requestDto, Authentication authentication);

    CartItemResponseDto updateBooksQuantity(CartItemUpdateRequestDto requestDto, Long id);

    void deleteCartItem(Long id, Authentication authentication);
}

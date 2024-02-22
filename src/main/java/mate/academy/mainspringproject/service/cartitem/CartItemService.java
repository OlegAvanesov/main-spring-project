package mate.academy.mainspringproject.service.cartitem;

import mate.academy.mainspringproject.dto.cartitem.CartItemRequestDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemResponseDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.mainspringproject.model.ShoppingCart;

public interface CartItemService {
    CartItemResponseDto save(CartItemRequestDto requestDto, ShoppingCart shoppingCart);

    CartItemResponseDto update(CartItemUpdateRequestDto requestDto, Long id);

    void delete(Long id);
}

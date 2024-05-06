package mate.academy.mainspringproject.service.shoppingcart;

import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.dto.cartitem.CartItemRequestDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemResponseDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.mainspringproject.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.mainspringproject.mappers.ShoppingCartMapper;
import mate.academy.mainspringproject.model.ShoppingCart;
import mate.academy.mainspringproject.model.User;
import mate.academy.mainspringproject.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.mainspringproject.service.cartitem.CartItemService;
import mate.academy.mainspringproject.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemService cartItemService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserService userService;

    @Override
    public ShoppingCartResponseDto getAllInfo(Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        User user = userService.findById(authenticatedUser.getId());
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public CartItemResponseDto addBookToCart(
            CartItemRequestDto requestDto, Authentication authentication
    ) {
        User authenticatedUser = (User) authentication.getPrincipal();
        User user = userService.findById(authenticatedUser.getId());
        ShoppingCart userShoppingCart = shoppingCartRepository.findByUserId(user.getId());

        if (userShoppingCart == null) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            userShoppingCart = shoppingCartRepository.save(shoppingCart);
        }
        return cartItemService.save(requestDto, userShoppingCart);
    }

    @Override
    @Transactional
    public CartItemResponseDto updateBooksQuantity(CartItemUpdateRequestDto requestDto, Long id) {
        return cartItemService.update(requestDto,id);
    }

    @Override
    @Transactional
    public void deleteCartItem(Long id, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        User user = userService.findById(authenticatedUser.getId());
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        shoppingCart.getCartItems().removeIf(cartItem -> cartItem.getId().equals(id));
        shoppingCartRepository.save(shoppingCart);
        cartItemService.delete(id);
    }
}

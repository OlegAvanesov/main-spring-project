package mate.academy.mainspringproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.dto.cartitem.CartItemRequestDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemResponseDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.mainspringproject.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.mainspringproject.service.shoppingcart.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/cart/")
@RequiredArgsConstructor
@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get information about shopping cart",
            description = "Get detailed information about the contents of user's shopping cart")
    public ShoppingCartResponseDto getAllInfoAboutCart(Authentication authentication) {
        return shoppingCartService.getAllInfo(authentication);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Add book to shopping cart",
            description = "Add a book and indicate its quantity to the shopping cart")
    public CartItemResponseDto addBookToShoppingCart(
            @RequestBody CartItemRequestDto requestDto, Authentication authentication
    ) {
        return shoppingCartService.addBookToCart(requestDto, authentication);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Change number of books",
            description = "Change the number of a specific book in the shopping cart")
    public CartItemResponseDto changeNumberOfBooks(
            @RequestBody CartItemUpdateRequestDto requestDto, @PathVariable("cartItemId") Long id
    ) {
        return shoppingCartService.updateBooksQuantity(requestDto, id);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Remove a specific item",
            description = "Remove a specific item from shopping cart")
    public void deleteBookFromShoppingCart(
            @PathVariable("cartItemId") Long id, Authentication authentication
    ) {
        shoppingCartService.deleteCartItem(id, authentication);
    }
}

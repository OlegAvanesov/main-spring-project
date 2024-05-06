package mate.academy.mainspringproject.service.cartitem;

import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.dto.cartitem.CartItemRequestDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemResponseDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.mainspringproject.exception.EntityNotFoundException;
import mate.academy.mainspringproject.mappers.CartItemMapper;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.model.CartItem;
import mate.academy.mainspringproject.model.ShoppingCart;
import mate.academy.mainspringproject.repository.book.BookRepository;
import mate.academy.mainspringproject.repository.cartitem.CartItemRepository;
import mate.academy.mainspringproject.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    @Transactional
    public CartItemResponseDto save(CartItemRequestDto requestDto, ShoppingCart shoppingCart) {
        CartItem cartItem = cartItemMapper.toEntity(requestDto);
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find book by id: " + requestDto.getBookId())
        );
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    @Transactional
    public CartItemResponseDto update(CartItemUpdateRequestDto requestDto, Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find cartItem by this id: " + id)
        );
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }
}

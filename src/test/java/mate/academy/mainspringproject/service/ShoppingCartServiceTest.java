package mate.academy.mainspringproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.mainspringproject.dto.cartitem.CartItemRequestDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemResponseDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.mainspringproject.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.mainspringproject.mappers.ShoppingCartMapper;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.model.CartItem;
import mate.academy.mainspringproject.model.Category;
import mate.academy.mainspringproject.model.ShoppingCart;
import mate.academy.mainspringproject.model.User;
import mate.academy.mainspringproject.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.mainspringproject.service.cartitem.CartItemService;
import mate.academy.mainspringproject.service.shoppingcart.ShoppingCartServiceImpl;
import mate.academy.mainspringproject.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    @Mock
    private CartItemService cartItemService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    private User user;
    private Book book;
    private CartItem cartItem;
    private ShoppingCart shoppingCart;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        user = createUser();
        book = createBook(createCategory());
        cartItem = createCartItem(book);
        shoppingCart = createShoppingCart(user, cartItem);
        authentication = Mockito.mock(Authentication.class);
    }

    @Test
    @DisplayName("Verify getAllInfo(). Should return all information about shopping cart")
    void getAllInfo_ValidRequest_Success() {
        //Given
        ShoppingCartResponseDto expected =
                createShoppingCartResponseDto(shoppingCart, Set.of(cartItem));

        when(authentication.getPrincipal()).thenReturn(user);
        when(userService.findById(anyLong())).thenReturn(user);
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);
        //When
        ShoppingCartResponseDto actual = shoppingCartService.getAllInfo(authentication);

        //Then
        assertEquals(expected, actual);
        verify(userService, times(1)).findById(anyLong());
        verify(shoppingCartRepository, times(1)).findByUserId(user.getId());
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
        verifyNoMoreInteractions(userService, shoppingCartRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("Verify addBookToCart(). Should add book to cart item ")
    void addBookToCart_ValidCartItemRequestDto_Success() {
        //Given
        shoppingCart.getCartItems().clear();
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto()
                .setBookId(cartItem.getBook().getId())
                .setQuantity(cartItem.getQuantity());
        CartItemResponseDto expected = convertCartItemToCartItemResponseDto(cartItem);

        when(authentication.getPrincipal()).thenReturn(user);
        when(userService.findById(user.getId())).thenReturn(user);
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(shoppingCart);
        when(cartItemService.save(cartItemRequestDto, shoppingCart)).thenReturn(expected);

        //When
        CartItemResponseDto actual =
                shoppingCartService.addBookToCart(cartItemRequestDto, authentication);

        //Then
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userService, times(1)).findById(user.getId());
        verify(shoppingCartRepository, times(1)).findByUserId(user.getId());
        verify(cartItemService, times(1))
                .save(cartItemRequestDto, shoppingCart);
        verifyNoMoreInteractions(userService, shoppingCartRepository, cartItemService);
    }

    @Test
    @DisplayName("Verify updateBooksQuantity(). Should update books quantity in cart item")
    void updateBooksQuantity_ValidCartItemUpdateRequestDto_Success() {
        //Given
        int updatedBooksQuantity = 5;
        CartItemUpdateRequestDto cartItemUpdateRequestDto = new CartItemUpdateRequestDto();
        cartItemUpdateRequestDto.setQuantity(updatedBooksQuantity);

        CartItemResponseDto expected = convertCartItemToCartItemResponseDto(cartItem);
        expected.setQuantity(updatedBooksQuantity);

        when(cartItemService.update(cartItemUpdateRequestDto, cartItem.getId()))
                .thenReturn(expected);

        //When
        CartItemResponseDto actual =
                cartItemService.update(cartItemUpdateRequestDto, cartItem.getId());

        //Then
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(cartItemService, times(1))
                .update(cartItemUpdateRequestDto, cartItem.getId());
        verifyNoMoreInteractions(cartItemService);
    }

    @Test
    @DisplayName("Verify deleteCartItem(). Should delete cart item from shopping cart")
    void deleteCartItem_ValidCartItemId_Success() {
        //Given
        when(authentication.getPrincipal()).thenReturn(user);
        when(userService.findById(user.getId())).thenReturn(user);
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(shoppingCart);
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);
        doNothing().when(cartItemService).delete(cartItem.getId());

        //When
        shoppingCartService.deleteCartItem(cartItem.getId(), authentication);
        //Then
        verify(userService, times(1)).findById(user.getId());
        verify(shoppingCartRepository, times(1)).findByUserId(user.getId());
        verify(shoppingCartRepository, times(1)).save(shoppingCart);
        verify(cartItemService, times(1)).delete(cartItem.getId());
        verifyNoMoreInteractions(userService, shoppingCartRepository, cartItemService);
    }

    private static User createUser() {
        return new User()
                .setId(1L)
                .setEmail("Bob@gmail.com")
                .setPassword("1234567890")
                .setFirstName("Bob")
                .setLastName("Smith")
                .setShippingAddress("Los Angeles");
    }

    private static Category createCategory() {
        return new Category()
                .setId(1L)
                .setName("Novel")
                .setDescription("Category description");
    }

    private static Book createBook(Category category) {
        return new Book()
                .setId(1L)
                .setTitle("Book 1")
                .setAuthor("Author 1")
                .setIsbn("573-5-19-186492-1")
                .setPrice(BigDecimal.valueOf(20))
                .setDescription("Description for Book 1")
                .setCoverImage("image1.jpg")
                .setCategories(Set.of(category));
    }

    private static ShoppingCart createShoppingCart(User user, CartItem cartItem) {
        Set<CartItem> cartItemSet = new HashSet<>();
        cartItemSet.add(cartItem);
        return new ShoppingCart()
                .setId(1L)
                .setUser(user)
                .setCartItems(cartItemSet);
    }

    private static CartItem createCartItem(Book book) {
        int booksQuantity = 3;
        return new CartItem()
                .setId(1L)
                .setBook(book)
                .setQuantity(booksQuantity);
    }

    private static ShoppingCartResponseDto createShoppingCartResponseDto(
            ShoppingCart shoppingCart, Set<CartItem> cartItems
    ) {
        Set<CartItemResponseDto> cartItemResponseDtos = cartItems.stream()
                .map(ShoppingCartServiceTest::convertCartItemToCartItemResponseDto)
                .collect(Collectors.toSet());

        return new ShoppingCartResponseDto()
                .setId(shoppingCart.getId())
                .setUserId(shoppingCart.getUser().getId())
                .setCartItems(cartItemResponseDtos);
    }

    private static CartItemResponseDto convertCartItemToCartItemResponseDto(CartItem cartItem) {
        return new CartItemResponseDto()
                .setId(cartItem.getId())
                .setBookId(cartItem.getBook().getId())
                .setBookTitle(cartItem.getBook().getTitle())
                .setQuantity(cartItem.getQuantity());
    }
}

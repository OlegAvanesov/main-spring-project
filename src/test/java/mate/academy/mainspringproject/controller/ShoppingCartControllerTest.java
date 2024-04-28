package mate.academy.mainspringproject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.mainspringproject.dto.cartitem.CartItemRequestDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemResponseDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.mainspringproject.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.model.CartItem;
import mate.academy.mainspringproject.model.Category;
import mate.academy.mainspringproject.model.ShoppingCart;
import mate.academy.mainspringproject.model.User;
import mate.academy.mainspringproject.service.shoppingcart.ShoppingCartService;
import mate.academy.mainspringproject.service.user.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    private static MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private WebApplicationContext applicationContext;
    private User user;
    private Book book;
    private CartItem cartItem;
    private ShoppingCart shoppingCart;

    @BeforeAll
    public void beforeAll() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown();
        addDataToDatabase();
    }

    @AfterAll
    public void afterAll() {
        teardown();
    }

    @SneakyThrows
    private void addDataToDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/before/"
                            + "1-add-user-to-users-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/before/"
                            + "2-add-three-books-to-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/before/"
                            + "3-add-category-to-categories-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/before/"
                            + "4-add-category-to-book.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/before/"
                            + "5-add-shopping-cart-to-shopping_carts-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/before/"
                            + "6-add-cartitem-to-cart_items-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/before/"
                            + "7-add-role-to-roles-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/before/"
                            + "8-add-role-to-user.sql")
            );
        }

        user = createUser();
        book = createBook(createCategory());
        cartItem = createCartItem(book);
        shoppingCart = createShoppingCart(user, cartItem);
    }

    @SneakyThrows
    private void teardown() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/after/"
                            + "1-delete-all-from-cartitems-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/after/"
                            + "2-delete-all-from-book_category-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/after/"
                            + "3-delete-all-from-categories-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/after/"
                            + "4-delete-all-from-shopping_carts_table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/after/"
                            + "5-delete-all-from-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/after/"
                            + "7-delete-all-from-user_role-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/after/"
                            + "6-delete-all-from-users-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/after/"
                            + "8-delete-all-from-roles-table.sql")
            );
        }
    }

    @Test
    @DisplayName("Verify getAllInfoAboutCart(). "
            + "Should return all information about the shopping cart")
    @WithUserDetails("Tom@gmail.com")
    void getAllInfoAboutCart_ValidParams_Success() throws Exception {
        //Given
        ShoppingCartResponseDto expected =
                createShoppingCartResponseDto(shoppingCart, Set.of(cartItem));

        //When
        MvcResult result = mockMvc.perform(get("/api/cart/"))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class
        );
        assertNotNull(actual);
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Verify addBookToShoppingCart(). Should add a specific book to the shopping cart")
    @WithUserDetails("Tom@gmail.com")
    void addBookToShoppingCart_ValidCartItemRequestDto_Success()
            throws Exception {
        //Given
        Book secondBook = createSecondBook(createCategory());
        CartItem secondCartItem = createSecondCartItem(secondBook);
        CartItemRequestDto cartItemRequestDto = new CartItemRequestDto()
                .setBookId(secondCartItem.getBook().getId())
                .setQuantity(secondCartItem.getQuantity());

        CartItemResponseDto expected = convertCartItemToCartItemResponseDto(secondCartItem);
        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);

        //When
        MvcResult result = mockMvc.perform(post("/api/cart/")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        CartItemResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemResponseDto.class
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify changeNumberOfBooks(). "
            + "Should change the quantity of a specific book in the shopping cart")
    @WithMockUser(username = "Tom", roles = {"USER"})
    void changeNumberOfBooks_ValidCartItemIdAndCartItemUpdateRequestDto_Success()
            throws Exception {
        //Given
        int updatedBooksQuantity = 10;
        CartItemUpdateRequestDto cartItemUpdateRequestDto = new CartItemUpdateRequestDto();
        cartItemUpdateRequestDto.setQuantity(updatedBooksQuantity);
        cartItem.setQuantity(updatedBooksQuantity);

        CartItemResponseDto expected = convertCartItemToCartItemResponseDto(cartItem);
        String jsonRequest = objectMapper.writeValueAsString(cartItemUpdateRequestDto);

        //When
        MvcResult result = mockMvc.perform(put(
                        "/api/cart/cart-items/{cartItemId}", cartItem.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        //Then
        CartItemResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemResponseDto.class
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify deleteBookFromShoppingCart(). "
            + "Should delete a specific book from the shopping cart")
    @WithUserDetails("Tom@gmail.com")
    void deleteBookFromShoppingCart_ValidCartItemId_Success() throws Exception {
        mockMvc.perform(delete(
                        "/api/cart/cart-items/{cartItemId}", cartItem.getId())
                )
                .andExpect(status().isOk());
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

    private static Book createSecondBook(Category category) {
        return new Book()
                .setId(2L)
                .setTitle("Book 2")
                .setAuthor("Author 1")
                .setIsbn("573-5-19-186492-2")
                .setPrice(BigDecimal.valueOf(25))
                .setDescription("Description for Book 2")
                .setCoverImage("image2.jpg")
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

    private static CartItem createSecondCartItem(Book book) {
        int booksQuantity = 5;
        return new CartItem()
                .setId(2L)
                .setBook(book)
                .setQuantity(booksQuantity);
    }

    private static ShoppingCartResponseDto createShoppingCartResponseDto(
            ShoppingCart shoppingCart, Set<CartItem> cartItems
    ) {
        Set<CartItemResponseDto> cartItemResponseDtos = cartItems.stream()
                .map(ShoppingCartControllerTest::convertCartItemToCartItemResponseDto)
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

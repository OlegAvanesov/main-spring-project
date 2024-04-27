package mate.academy.mainspringproject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.mainspringproject.dto.cartitem.CartItemResponseDto;
import mate.academy.mainspringproject.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.model.CartItem;
import mate.academy.mainspringproject.model.Category;
import mate.academy.mainspringproject.model.ShoppingCart;
import mate.academy.mainspringproject.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private WebApplicationContext applicationContext;

    @BeforeAll
    public void beforeAll() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown();
    }

    @BeforeEach
    public void beforeEach() {
        addDataToDatabase();
    }

    @AfterEach
    public void afterEach() {
        teardown();
    }

    @SneakyThrows
    private void addDataToDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/shoppingCart/before/"
                            + "1-add-two-users-to-users-table.sql")
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
        }
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
                            + "6-delete-all-from-users-table.sql")
            );
        }
    }

    @Test
    @DisplayName("Verify getAllInfoAboutCart(). "
            + "Should return all information about the shopping cart")
    @WithMockUser(roles = "USER")
    void getAllInfoAboutCart() throws Exception {
        //Given
        User user = createUser();
        Book book = createBook(createCategory());
        CartItem cartItem = createCartItem(book);
        ShoppingCart shoppingCart = createShoppingCart(user, cartItem);
        Authentication authentication = Mockito.mock(Authentication.class);

        ShoppingCartResponseDto expected =
                createShoppingCartResponseDto(shoppingCart, Set.of(cartItem));

        when(authentication.getPrincipal()).thenReturn(user);

        //When
        MvcResult result = mockMvc.perform(get("/api/cart/", authentication))
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
    @WithMockUser(roles = "USER")
    void addBookToShoppingCart() {
        //Given

        //When

        //Then

    }

    @Test
    @DisplayName("Verify changeNumberOfBooks(). "
            + "Should change the quantity of a specific book in the shopping cart")
    @WithMockUser(roles = "USER")
    void changeNumberOfBooks() {
        //Given

        //When

        //Then

    }

    @Test
    @DisplayName("Verify deleteBookFromShoppingCart(). "
            + "Should delete a specific book from the shopping cart")
    @WithMockUser(roles = "USER")
    void deleteBookFromShoppingCart() {
        //Given

        //When

        //Then

    }

    private static User createUser() {
        return new User()
                .setId(4L)
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

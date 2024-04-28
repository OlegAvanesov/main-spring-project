package mate.academy.mainspringproject.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Set;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.model.CartItem;
import mate.academy.mainspringproject.model.Category;
import mate.academy.mainspringproject.model.ShoppingCart;
import mate.academy.mainspringproject.model.User;
import mate.academy.mainspringproject.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Find shopping cart by its owner id")
    @Sql(scripts = {
            "classpath:sql/repository/shoppingCart/before/1-add-user-to-users-table.sql",
            "classpath:sql/repository/shoppingCart/before/2-add-three-books-to-books-table.sql",
            "classpath:sql/repository/shoppingCart/before/3-add-category-to-categories-table.sql",
            "classpath:sql/repository/shoppingCart/before/4-add-category-to-book.sql",
            "classpath:sql/repository/shoppingCart/before/"
                    + "5-add-shopping-cart-to-shopping_carts-table.sql",
            "classpath:sql/repository/shoppingCart/before/6-add-cartitem-to-cart_items-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:sql/repository/shoppingCart/after/1-delete-all-from-cartitems-table.sql",
            "classpath:sql/repository/shoppingCart/after/2-delete-all-from-book_category-table.sql",
            "classpath:sql/repository/shoppingCart/after/3-delete-all-from-categories-table.sql",
            "classpath:sql/repository/shoppingCart/after/"
                    + "4-delete-all-from-shopping_carts_table.sql",
            "classpath:sql/repository/shoppingCart/after/5-delete-all-from-books-table.sql",
            "classpath:sql/repository/shoppingCart/after/6-delete-all-from-users-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_ValidUserId_ReturnShoppingCart() {
        //Given
        User user = createUser();
        Book book = createBook(createCategory());
        CartItem cartItem = createCartItem(book);
        ShoppingCart expected = createShoppingCart(user, cartItem);

        //When
        ShoppingCart actual = shoppingCartRepository.findByUserId(user.getId());

        //Then
        assertNotNull(actual);
        assertEquals(expected.getCartItems().stream().findFirst().get().getBook().getTitle(),
                actual.getCartItems().stream().findFirst().get().getBook().getTitle());
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
        return new ShoppingCart()
                .setId(1L)
                .setUser(user)
                .setCartItems(Set.of(cartItem));
    }

    private static CartItem createCartItem(Book book) {
        int booksQuantity = 3;
        return new CartItem()
                .setId(1L)
                .setBook(book)
                .setQuantity(booksQuantity);
    }
}

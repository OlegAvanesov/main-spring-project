package mate.academy.mainspringproject.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    private static final Long CATEGORY_ID = 1L;
    private static final String FIRST_BOOK_TITLE = "Book 1";
    private static final String FIRST_BOOK_AUTHOR = "Author 1";

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by valid category id")
    @Sql(scripts = {
            "classpath:sql/repository/book/before/1-add-three-books-to-books-table.sql",
            "classpath:sql/repository/book/before/2-add-category-to-categories-table.sql",
            "classpath:sql/repository/book/before/3-add-category-to-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:sql/repository/book/after/1-delete-all-from-book_category-table.sql",
            "classpath:sql/repository/book/after/2-delete-all-from-books-table.sql",
            "classpath:sql/repository/book/after/3-delete-all-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_ValidCategoryId_ReturnTwoBooksByCategory() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> actual = bookRepository.findAllByCategoryId(CATEGORY_ID, pageable);
        assertEquals(2, actual.size());
        assertEquals(FIRST_BOOK_TITLE, actual.get(0).getTitle());
        assertEquals(FIRST_BOOK_AUTHOR, actual.get(0).getAuthor());
    }
}

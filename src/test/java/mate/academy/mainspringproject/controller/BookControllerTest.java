package mate.academy.mainspringproject.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.mainspringproject.dto.book.BookDto;
import mate.academy.mainspringproject.dto.book.CreateBookRequestDto;
import mate.academy.mainspringproject.exception.EntityNotFoundException;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.model.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
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
    private void teardown() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/book/after/"
                            + "1-delete-all-from-book_category-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/book/after/"
                            + "2-delete-all-from-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/book/after/"
                            + "3-delete-all-from-categories-table.sql"));
        }
    }

    @SneakyThrows
    private void addDataToDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/book/before/"
                            + "1-add-three-books-to-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/book/before/"
                            + "2-add-category-to-categories-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/book/before/3-add-category-to-book.sql")
            );
        }
    }

    @Test
    @DisplayName("Verify getAll(). Should return list of all books from database")
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getAll_WithPagination_ShouldReturnPageWithBooks() throws Exception {
        //Given
        Category category = createFirstCategory();
        BookDto firstBookDto = createBookDto(createFirstBook(Set.of(category)));
        BookDto secondBookDto = createBookDto(createSecondBook(Set.of(category)));
        BookDto thirdBookDto = createBookDto(createThirdBook(Set.of(createSecondCategory())));
        List<BookDto> expected = List.of(firstBookDto, secondBookDto, thirdBookDto);

        Pageable pageable = PageRequest.of(0, 10);

        //When
        MvcResult result = mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        //Then
        assertNotNull(actual);
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
        assertEquals(expected.get(2), actual.get(2));
    }

    @Test
    @DisplayName("Verify getBookById(). Should return book by it's id")
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getBookById_ValidId_ShouldReturnBookWithSpecificId() throws Exception {
        //Given
        Book book = createSecondBook(Set.of(createFirstCategory()));
        BookDto expected = createBookDto(book);

        //When
        MvcResult result = mockMvc.perform(get("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);

        //Then
        assertNotNull(actual);
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Verify getBookById(). Should throw exception")
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getBookById_InvalidId_ShouldReturnException() throws Exception {
        Long invalidId = 5L;

        mockMvc.perform(get("/api/books/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Can't find book by id: " + invalidId,
                        result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Verify createBook(). Should add book to database and return bookDto")
    @WithMockUser(roles = {"ADMIN"})
    void createBook_ValidRequestDto_Success() throws Exception {
        //Given
        CreateBookRequestDto requestDto = createRequestDto(List.of(createFirstCategory().getId()));
        BookDto expected = createBookDto(requestDto);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //When
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        //Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Verify updateBook(). Should update info about specific book")
    @WithMockUser(roles = {"ADMIN"})
    void updateBook_ValidIdAndCreateBookRequestDto_ShouldReturnUpdatedBookDto() throws Exception {
        //Given
        Long updatedBookId = 1L;
        CreateBookRequestDto requestDto =
                createRequestDto(List.of(createFirstCategory().getId()));

        BookDto expected = createBookDto(requestDto);
        expected.setId(updatedBookId);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //When
        MvcResult result = mockMvc.perform(put("/api/books/{id}", updatedBookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify delete(). Should delete specific book")
    @WithMockUser(roles = {"ADMIN"})
    void delete_ValidId_Success() throws Exception {
        Book firstBook = createFirstBook(Set.of(createFirstCategory()));
        mockMvc.perform(delete("/api/books/{id}", firstBook.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Verify search(). Should return a list of books matching the parameters")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void search_ValidSearchParameters_ShouldReturnListOfBooksDto() throws Exception {
        //Given
        BookDto bookDto = createBookDto(createFirstBook(Set.of(createFirstCategory())));
        List<BookDto> expected = List.of(bookDto);

        //When
        MvcResult result = mockMvc.perform(get(
                        "/api/books/search?titles=Book 1&authors=Author 1"
                )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        List<BookDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    private static Category createFirstCategory() {
        return new Category()
                .setId(1L)
                .setName("Novel")
                .setDescription("Novel description");
    }

    private static Category createSecondCategory() {
        return new Category()
                .setId(2L)
                .setName("Detective")
                .setDescription("Detective description");
    }

    private static CreateBookRequestDto createRequestDto(List<Long> categoryIds) {
        return new CreateBookRequestDto()
                .setTitle("First book")
                .setAuthor("First author")
                .setIsbn("123-456-789-0")
                .setPrice(BigDecimal.valueOf(19.90))
                .setDescription("First book description")
                .setCoverImage("image1.jpg")
                .setCategoryIds(new HashSet<Long>(categoryIds));
    }

    private static BookDto createBookDto(CreateBookRequestDto requestDto) {
        return new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoryIds(requestDto.getCategoryIds());
    }

    private static BookDto createBookDto(Book book) {
        Set<Long> categoryIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        return new BookDto()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage())
                .setCategoryIds(categoryIds);
    }

    private static Book createFirstBook(Set<Category> categories) {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book 1");
        book.setAuthor("Author 1");
        book.setIsbn("573-5-19-186492-1");
        book.setPrice(BigDecimal.valueOf(20));
        book.setCategories(categories);
        book.setDescription("Description for Book 1");
        book.setCoverImage("image1.jpg");
        return book;
    }

    private static Book createSecondBook(Set<Category> categories) {
        Book book = new Book();
        book.setId(2L);
        book.setTitle("Book 2");
        book.setAuthor("Author 1");
        book.setIsbn("573-5-19-186492-2");
        book.setPrice(BigDecimal.valueOf(25));
        book.setCategories(categories);
        book.setDescription("Description for Book 2");
        book.setCoverImage("image2.jpg");
        return book;
    }

    private static Book createThirdBook(Set<Category> categories) {
        Book book = new Book();
        book.setId(3L);
        book.setTitle("Book 3");
        book.setAuthor("Author 2");
        book.setIsbn("573-5-19-186492-3");
        book.setPrice(BigDecimal.valueOf(30));
        book.setCategories(categories);
        book.setDescription("Description for Book 3");
        book.setCoverImage("image3.jpg");
        return book;
    }
}

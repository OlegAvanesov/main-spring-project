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
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.mainspringproject.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.mainspringproject.dto.category.CategoryRequestDto;
import mate.academy.mainspringproject.dto.category.CategoryResponseDto;
import mate.academy.mainspringproject.exception.EntityNotFoundException;
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
class CategoryControllerTest {
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
                    new ClassPathResource("sql/controller/category/after/"
                            + "1-delete-all-from-book_category-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/category/after/"
                            + "2-delete-all-from-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/category/after/"
                            + "3-delete-all-from-categories-table.sql"));
        }
    }

    @SneakyThrows
    private void addDataToDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/category/before/"
                            + "1-add-three-books-to-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/category/before/"
                            + "2-add-category-to-categories-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("sql/controller/category/before/"
                            + "3-add-category-to-book.sql")
            );
        }
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Verify createCategory(). "
            + "Should add category to database and return CategoryResponseDto"
    )
    void createCategory_ValidRequestDto_Success() throws Exception {
        //Given
        CategoryRequestDto requestDto = createCategoryRequestDto();
        CategoryResponseDto expected =
                convertCategoryToResponseDto(createFirstCategory());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //When
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryRequestDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryRequestDto.class
        );
        //Then

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Verify getAll(). Should return list of all categories from database")
    void getAllWithPagination_ShouldReturnPageWithCategories() throws Exception {
        //Given
        Category firstCategory = createFirstCategory();
        Category secondCategory = createSecondCategory();

        CategoryResponseDto firstCategoryResponseDto =
                convertCategoryToResponseDto(firstCategory);
        CategoryResponseDto secondCategoryResponseDto =
                convertCategoryToResponseDto(secondCategory);
        List<CategoryResponseDto> expected = List.of(
                firstCategoryResponseDto, secondCategoryResponseDto
        );

        Pageable pageable = PageRequest.of(0, 10);

        //When
        MvcResult result = mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryResponseDto> actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<>() {
                });

        //Then
        assertNotNull(actual);
        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.get(1), actual.get(1));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Verify getCategoryById(). Should return category by it's id")
    void getCategoryById_ValidId_ShouldReturnCategoryWithSpecificId() throws Exception {
        //Given
        Category category = createFirstCategory();
        CategoryResponseDto expected = convertCategoryToResponseDto(category);

        //When
        MvcResult result = mockMvc.perform(get("/api/categories/{id}", category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class
        );

        //Then
        assertNotNull(actual);
        assertEquals(actual, expected);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Verify getCategoryById(). Should throw exception")
    void getCategoryById_InvalidId_ShouldReturnException() throws Exception {
        Long invalidId = 5L;

        mockMvc.perform(get("/api/categories/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals("Can't find category by id: " + invalidId,
                        result.getResolvedException().getMessage()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Verify updateCategory(). Should update info about specific category")
    void updateCategory_ValidIdAndCategoryRequestDto_ShouldReturnCategoryResponseDto()
            throws Exception {
        //Given
        Long updatedCategoryId = 1L;
        CategoryRequestDto categoryRequestDto = createCategoryRequestDto().setName("Fantasy");
        CategoryResponseDto expected = new CategoryResponseDto()
                .setId(updatedCategoryId)
                .setName(categoryRequestDto.getName())
                .setDescription(categoryRequestDto.getDescription());

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        //When
        MvcResult result = mockMvc.perform(put("/api/categories/{id}", updatedCategoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then

        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class
        );
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Verify delete(). Should delete specific category")
    void deleteCategory_ValidId_Success() throws Exception {
        Category category = createFirstCategory();

        mockMvc.perform(delete("/api/categories/{id}", category.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    @DisplayName("Verify getBooksByCategoryId(). "
            + "Should return list of BookDtoWithoutCategoryIds with specific category"
    )
    void getBooksByCategoryId_ValidCategoryId_ShouldReturnListOfBookDtoWithoutCategoryIds()
            throws Exception {
        //Given
        Long categoryId = 1L;
        BookDtoWithoutCategoryIds firstBookDtoWithoutCategoryIds =
                createFirstBookDtoWithoutCategoryIds();

        BookDtoWithoutCategoryIds secondBookDtoWithoutCategoryIds =
                createSecondBookDtoWithoutCategoryIds();

        List<BookDtoWithoutCategoryIds> expected = List.of(
                firstBookDtoWithoutCategoryIds, secondBookDtoWithoutCategoryIds
        );

        Pageable pageable = PageRequest.of(0, 10);

        //When
        MvcResult result = mockMvc.perform(get("/api/categories/{id}/books",
                        categoryId, pageable)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        List<BookDtoWithoutCategoryIds> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>() {
                }
        );
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.contains(expected.get(0)));
        assertTrue(actual.contains(expected.get(1)));

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

    private static CategoryResponseDto convertCategoryToResponseDto(Category category) {
        return new CategoryResponseDto()
                .setId(category.getId())
                .setName(category.getName())
                .setDescription(category.getDescription());
    }

    private static CategoryRequestDto createCategoryRequestDto() {
        return new CategoryRequestDto()
                .setName("Novel")
                .setDescription("Novel description");
    }

    private static BookDtoWithoutCategoryIds createFirstBookDtoWithoutCategoryIds() {
        return new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setTitle("Book 1")
                .setAuthor("Author 1")
                .setIsbn("573-5-19-186492-1")
                .setPrice(BigDecimal.valueOf(20))
                .setDescription("Description for Book 1")
                .setCoverImage("image1.jpg");
    }

    private static BookDtoWithoutCategoryIds createSecondBookDtoWithoutCategoryIds() {
        return new BookDtoWithoutCategoryIds()
                .setId(2L)
                .setTitle("Book 2")
                .setAuthor("Author 1")
                .setIsbn("573-5-19-186492-2")
                .setPrice(BigDecimal.valueOf(25))
                .setDescription("Description for Book 2")
                .setCoverImage("image2.jpg");
    }
}

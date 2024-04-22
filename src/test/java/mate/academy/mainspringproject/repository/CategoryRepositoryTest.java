package mate.academy.mainspringproject.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import mate.academy.mainspringproject.model.Category;
import mate.academy.mainspringproject.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    private static final String CATEGORY_NAME = "Novel";
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find category by it's name")
    @Sql(scripts = {
            "classpath:sql/repository/category/before/add-category-to-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:sql/repository/category/after/delete-all-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByName_ValidName_ReturnCategory() {
        Category expected = new Category()
                .setId(1L)
                .setName("Novel")
                .setDescription("Novel description");

        Category actual = categoryRepository.findByName(CATEGORY_NAME);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}

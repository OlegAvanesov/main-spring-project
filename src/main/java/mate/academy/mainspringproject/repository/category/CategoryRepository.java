package mate.academy.mainspringproject.repository.category;

import mate.academy.mainspringproject.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("FROM Category c WHERE c.name = :name")
    Category findByName(String name);
}

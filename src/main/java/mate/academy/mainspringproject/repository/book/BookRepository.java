package mate.academy.mainspringproject.repository.book;

import java.util.List;
import mate.academy.mainspringproject.model.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.categories c "
            + "WHERE :categoryId IN (c.id)")
    List<Book> findAllByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}

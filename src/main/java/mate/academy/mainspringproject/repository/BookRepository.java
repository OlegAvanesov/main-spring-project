package mate.academy.mainspringproject.repository;

import mate.academy.mainspringproject.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}

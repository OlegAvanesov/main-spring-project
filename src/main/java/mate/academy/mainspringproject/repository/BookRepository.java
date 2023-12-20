package mate.academy.mainspringproject.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.mainspringproject.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);
}

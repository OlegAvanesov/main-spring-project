package mate.academy.mainspringproject.service;

import java.util.List;
import mate.academy.mainspringproject.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}

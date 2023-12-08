package mate.academy.mainspringproject.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}

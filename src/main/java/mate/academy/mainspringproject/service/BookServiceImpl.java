package mate.academy.mainspringproject.service;

import java.util.List;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        bookRepository.save(book);
        return book;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}

package mate.academy.mainspringproject.service.bookservice;

import java.util.List;
import mate.academy.mainspringproject.dto.book.BookDto;
import mate.academy.mainspringproject.dto.book.BookSearchParameters;
import mate.academy.mainspringproject.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParameters parameters);
}

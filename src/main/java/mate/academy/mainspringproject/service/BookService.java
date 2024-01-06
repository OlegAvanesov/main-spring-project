package mate.academy.mainspringproject.service;

import java.util.List;
import mate.academy.mainspringproject.dto.BookDto;
import mate.academy.mainspringproject.dto.BookSearchParameters;
import mate.academy.mainspringproject.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParameters parameters);
}

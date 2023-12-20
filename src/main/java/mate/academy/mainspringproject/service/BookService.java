package mate.academy.mainspringproject.service;

import java.util.List;
import mate.academy.mainspringproject.dto.BookDto;
import mate.academy.mainspringproject.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);
}

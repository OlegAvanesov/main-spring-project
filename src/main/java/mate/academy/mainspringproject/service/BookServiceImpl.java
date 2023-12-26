package mate.academy.mainspringproject.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.dto.BookDto;
import mate.academy.mainspringproject.dto.CreateBookRequestDto;
import mate.academy.mainspringproject.exception.EntityNotFoundException;
import mate.academy.mainspringproject.mappers.BookMapper;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
            Book book = bookRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("Can't find book by id: " + id));
            book.setId(id);
            book.setTitle(requestDto.getTitle());
            book.setAuthor(requestDto.getAuthor());
            book.setIsbn(requestDto.getIsbn());
            book.setPrice(requestDto.getPrice());
            book.setDescription(requestDto.getDescription());
            book.setCoverImage(requestDto.getCoverImage());
            return bookMapper.toDto(bookRepository.save(book));
    }
}

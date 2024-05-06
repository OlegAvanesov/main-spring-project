package mate.academy.mainspringproject.service.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.dto.book.BookDto;
import mate.academy.mainspringproject.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.mainspringproject.dto.book.BookSearchParameters;
import mate.academy.mainspringproject.dto.book.CreateBookRequestDto;
import mate.academy.mainspringproject.exception.EntityNotFoundException;
import mate.academy.mainspringproject.mappers.BookMapper;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.repository.SpecificationBuilder;
import mate.academy.mainspringproject.repository.book.BookRepository;
import mate.academy.mainspringproject.repository.category.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final SpecificationBuilder<Book> specificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
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
    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find book by id: " + id);
        }
        Book book = bookMapper.toModel(requestDto);
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> search(BookSearchParameters parameters) {
        Specification<Book> specification = specificationBuilder.build(parameters);
        return bookRepository.findAll(specification).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable) {
        List<Book> allBooksByCategoryId = bookRepository.findAllByCategoryId(categoryId, pageable);
        return allBooksByCategoryId.stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}

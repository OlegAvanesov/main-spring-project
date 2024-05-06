package mate.academy.mainspringproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import mate.academy.mainspringproject.dto.book.BookDto;
import mate.academy.mainspringproject.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.mainspringproject.dto.book.BookSearchParameters;
import mate.academy.mainspringproject.dto.book.CreateBookRequestDto;
import mate.academy.mainspringproject.exception.EntityNotFoundException;
import mate.academy.mainspringproject.mappers.BookMapper;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.model.Category;
import mate.academy.mainspringproject.repository.book.BookRepository;
import mate.academy.mainspringproject.repository.book.BookSpecificationBuilder;
import mate.academy.mainspringproject.repository.category.CategoryRepository;
import mate.academy.mainspringproject.service.book.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;
    private Category category;
    private List<Long> categoryIds;
    private List<Category> categories;
    private CreateBookRequestDto requestDto;
    private Book book;

    @BeforeEach
    void setUp() {
        category = createCategory();
        categoryIds = List.of(category.getId());
        categories = List.of(category);
        requestDto = createRequestDto(categoryIds);
        book = createBook(requestDto, categories);
    }

    @Test
    @DisplayName("Verify findAll() method. Should return list of all bookDto")
    void findAll_ValidPageable_ShouldReturnAllBooks() {
        //Given
        List<Book> books = List.of(book);

        BookDto bookDto = convertBookToBookDto(book, categoryIds);
        List<BookDto> expected = List.of(bookDto);

        Pageable pageable = PageRequest.of(0,10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        //When
        List<BookDto> actual = bookService.findAll(pageable);

        //Then
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Test findById() method. Should return bookDto by book id")
    void findById_GivenValidId_ShouldReturnSpecificBookDto() {
        //Given
        BookDto expected = convertBookToBookDto(book, categoryIds);

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        //When
        BookDto actual = bookService.findById(book.getId());

        //Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test findById() method. Should display exception message")
    void findById_GivenInvalidId_ShouldThrowExceptionMessage() {
        //Given
        Long invalidBookId = 10L;
        when(bookRepository.findById(invalidBookId)).thenThrow(
                new EntityNotFoundException("Can't find book by id: " + invalidBookId)
        );

        //When
        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class, () -> bookService.findById(invalidBookId));

        //Then
        assertEquals("Can't find book by id: " + invalidBookId,
                entityNotFoundException.getMessage());
        assertEquals(EntityNotFoundException.class, entityNotFoundException.getClass());
    }

    @Test
    @DisplayName("Verify save() method. Should save book and return bookDto")
    void save_ValidCreateBookRequestDto_ShouldSaveBookAndReturnBookDto() {
        //Given
        BookDto expected = convertBookToBookDto(book, categoryIds);

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        //When
        BookDto actual = bookService.save(requestDto);

        //Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify deleteById() method. Should delete book by it's id")
    void deleteById_ValidBookId_ShouldDeleteBook() {
        doNothing().when(bookRepository).deleteById(anyLong());
        bookService.deleteById(anyLong());
        verify(bookRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Verify updateById() method. Should update book and return bookDto")
    void updateById_ValidBookIdAndRequestDto_ShouldReturnUpdatedBookDto() {
        //Given
        Long id = 1L;
        Book updatedBook = createSecondBook(requestDto, categories);

        BookDto expected = convertBookToBookDto(updatedBook, categoryIds);

        when(bookRepository.existsById(id)).thenReturn(true);
        when(bookMapper.toModel(requestDto)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(expected);

        //When
        BookDto actual = bookService.updateById(id, requestDto);

        //Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify updateById() method. Should display exception message")
    void updateById_InvalidBookId_ShouldThrowExceptionMessage() {
        //Given
        Long invalidBookId = 10L;
        when(bookRepository.existsById(invalidBookId)).thenThrow(
                new EntityNotFoundException("Can't find book by id: " + invalidBookId)
        );

        //When
        EntityNotFoundException entityNotFoundException
                = assertThrows(EntityNotFoundException.class,
                    () -> bookService.updateById(invalidBookId, requestDto));

        //Then
        assertEquals("Can't find book by id: " + invalidBookId,
                entityNotFoundException.getMessage());
        assertEquals(EntityNotFoundException.class, entityNotFoundException.getClass());
    }

    @Test
    @DisplayName("Verify search() method. Should return books by input parameters")
    void search_ValidSearchParameters_ShouldReturnListOfBooks() {
        //Given
        Book secondBook = createSecondBook(requestDto, categories);
        List<Book> booksList = List.of(book, secondBook);

        BookDto firstBookDto = convertBookToBookDto(book, categoryIds);
        BookDto secondBookDto = convertBookToBookDto(secondBook, categoryIds);

        String[] authorParams = {book.getAuthor(), secondBook.getAuthor()};
        String[] titleParams = {book.getTitle(), secondBook.getTitle()};
        BookSearchParameters searchParameters = new BookSearchParameters(authorParams, titleParams);
        Specification<Book> specification = bookSpecificationBuilder.build(searchParameters);

        when(bookRepository.findAll(specification)).thenReturn(booksList);
        when(bookMapper.toDto(book)).thenReturn(firstBookDto);
        when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);

        List<BookDto> expected = List.of(firstBookDto, secondBookDto);
        //When
        List<BookDto> actual = bookService.search(searchParameters);

        //Then
        assertEquals(expected, actual);
        assertEquals(2, actual.size());
        verify(bookRepository, times(1)).findAll(specification);
    }

    @Test
    @DisplayName("Verify findAllByCategoryId() method. Should return all books by it's category id")
    void findAllByCategoryId_ValidCategoryId_ShouldReturnListOfBooks() {
        //Given
        Book secondBook = createSecondBook(requestDto, categories);
        List<Book> books = List.of(book, secondBook);

        Pageable pageable = PageRequest.of(0, 10);

        BookDtoWithoutCategoryIds firstBookDtoWithoutCategoryId
                = convertBookToBookDtoWithoutCategoryIds(book, categoryIds);
        BookDtoWithoutCategoryIds secondBookDtoWithoutCategoryId
                = convertBookToBookDtoWithoutCategoryIds(secondBook, categoryIds);

        when(bookRepository.findAllByCategoryId(category.getId(), pageable)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(book))
                .thenReturn(firstBookDtoWithoutCategoryId);
        when(bookMapper.toDtoWithoutCategories(secondBook))
                .thenReturn(secondBookDtoWithoutCategoryId);

        List<BookDtoWithoutCategoryIds> expected = List.of(firstBookDtoWithoutCategoryId,
                secondBookDtoWithoutCategoryId
        );
        //When
        List<BookDtoWithoutCategoryIds> actual
                = bookService.findAllByCategoryId(category.getId(), pageable);

        //Then
        assertEquals(expected, actual);
    }

    private static Category createCategory() {
        return new Category()
                .setId(1L)
                .setName("Novel")
                .setDescription("Category description");
    }

    private static CreateBookRequestDto createRequestDto(List<Long> categoryIds) {
        return new CreateBookRequestDto()
                .setTitle("First book")
                .setAuthor("First author")
                .setIsbn("123-456-789-0")
                .setPrice(BigDecimal.valueOf(19.90))
                .setDescription("First book description")
                .setCoverImage("image1.jpg")
                .setCategoryIds(new HashSet<Long>(categoryIds));
    }

    private static Book createBook(CreateBookRequestDto requestDto, List<Category> categories) {
        return new Book()
                .setId(1L)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage())
                .setCategories(new HashSet<>(categories));
    }

    private static Book createSecondBook(
            CreateBookRequestDto requestDto, List<Category> categories
    ) {
        return new Book()
                .setId(2L)
                .setTitle("Second book")
                .setAuthor("Second author")
                .setIsbn("123456-789")
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage())
                .setCategories(new HashSet<>(categories));
    }

    private static BookDto convertBookToBookDto(Book book, List<Long> categoryIds) {
        return new BookDto()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage())
                .setCategoryIds(new HashSet<>(categoryIds));
    }

    private static BookDtoWithoutCategoryIds convertBookToBookDtoWithoutCategoryIds(
            Book book, List<Long> categoryIds
    ) {
        return new BookDtoWithoutCategoryIds()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage());
    }
}

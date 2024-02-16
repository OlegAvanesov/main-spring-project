package mate.academy.mainspringproject.mappers;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.mainspringproject.config.MapperConfig;
import mate.academy.mainspringproject.dto.book.BookDto;
import mate.academy.mainspringproject.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.mainspringproject.dto.book.CreateBookRequestDto;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.model.Category;
import mate.academy.mainspringproject.repository.book.BookRepository;
import mate.academy.mainspringproject.repository.category.CategoryRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
            Set<Long> categoryIdsList = book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet());
            bookDto.setCategoryIds(categoryIdsList);
    }
}

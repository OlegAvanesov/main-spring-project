package mate.academy.mainspringproject.mappers;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.mainspringproject.config.MapperConfig;
import mate.academy.mainspringproject.dto.book.BookDto;
import mate.academy.mainspringproject.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.mainspringproject.dto.book.CreateBookRequestDto;
import mate.academy.mainspringproject.model.Book;
import mate.academy.mainspringproject.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> categoryIdsList = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(categoryIdsList);
    }

    Book toModel(CreateBookRequestDto requestDto);

    @AfterMapping
    default void setCategories(
            CreateBookRequestDto requestDto, @MappingTarget Book book
    ) {
        Set<Category> categoryIds = requestDto.getCategoryIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet());
        book.setCategories(categoryIds);
    }

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);
}

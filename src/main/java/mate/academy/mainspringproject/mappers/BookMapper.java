package mate.academy.mainspringproject.mappers;

import mate.academy.mainspringproject.config.MapperConfig;
import mate.academy.mainspringproject.dto.book.BookDto;
import mate.academy.mainspringproject.dto.book.CreateBookRequestDto;
import mate.academy.mainspringproject.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}

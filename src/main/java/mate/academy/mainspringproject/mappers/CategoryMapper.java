package mate.academy.mainspringproject.mappers;

import mate.academy.mainspringproject.config.MapperConfig;
import mate.academy.mainspringproject.dto.category.CategoryRequestDto;
import mate.academy.mainspringproject.dto.category.CategoryResponseDto;
import mate.academy.mainspringproject.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryDto);
}

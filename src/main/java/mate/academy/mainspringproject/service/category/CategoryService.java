package mate.academy.mainspringproject.service.category;

import java.util.List;
import mate.academy.mainspringproject.dto.category.CategoryRequestDto;
import mate.academy.mainspringproject.dto.category.CategoryResponseDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryDto);

    CategoryResponseDto update(Long id, CategoryRequestDto categoryDto);

    void deleteById(Long id);
}

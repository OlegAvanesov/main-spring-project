package mate.academy.mainspringproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.mainspringproject.dto.category.CategoryRequestDto;
import mate.academy.mainspringproject.dto.category.CategoryResponseDto;
import mate.academy.mainspringproject.exception.EntityNotFoundException;
import mate.academy.mainspringproject.mappers.CategoryMapper;
import mate.academy.mainspringproject.model.Category;
import mate.academy.mainspringproject.repository.category.CategoryRepository;
import mate.academy.mainspringproject.service.category.CategoryServiceImpl;
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

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify findAll() method. Should return list of all CategoryResponseDto")
    void findAll_ValidPageable_ShouldReturnAllCategoryResponseDto() {
        //Given
        Category firstCategory = createFirstCategory();
        Category secondCategory = createSecondCategory();
        List<Category> categoryList = List.of(firstCategory, secondCategory);

        CategoryResponseDto firstCategoryResponseDto = convertCategoryToResponseDto(firstCategory);
        CategoryResponseDto secondCategoryResponseDto =
                convertCategoryToResponseDto(secondCategory);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(
                categoryList, pageable, categoryList.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(firstCategory)).thenReturn(firstCategoryResponseDto);
        when(categoryMapper.toDto(secondCategory)).thenReturn(secondCategoryResponseDto);

        List<CategoryResponseDto> expected =
                List.of(firstCategoryResponseDto, secondCategoryResponseDto);

        //When
        List<CategoryResponseDto> actual = categoryService.findAll(pageable);

        //Then
        assertEquals(expected, actual);
        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(firstCategory);
        verify(categoryMapper, times(1)).toDto(secondCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Test getById() method. Should return CategoryResponseDto by category id")
    void getById_GivenValidId_ShouldReturnSpecificCategoryResponseDto() {
        //Given
        Category firstCategory = createFirstCategory();
        CategoryResponseDto expected = convertCategoryToResponseDto(firstCategory);

        when(categoryRepository.findById(firstCategory.getId()))
                .thenReturn(Optional.of(firstCategory));
        when(categoryMapper.toDto(firstCategory)).thenReturn(expected);

        //When
        CategoryResponseDto actual = categoryService.getById(firstCategory.getId());

        //Then
        assertEquals(expected, actual);
        verify(categoryRepository, times(1)).findById(firstCategory.getId());
        verify(categoryMapper, times(1)).toDto(firstCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Test getById() method. Should display exception message")
    void getById_GivenInvalidId_ShouldThrowExceptionMessage() {
        //Given
        Long invalidCategoryId = 5L;
        String expected = "Can't find category by id " + invalidCategoryId;
        when(categoryRepository.findById(invalidCategoryId)).thenThrow(
                new EntityNotFoundException(expected)
        );

        //When
        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(invalidCategoryId)
        );

        //Then
        assertEquals(expected, entityNotFoundException.getMessage());
        assertEquals(EntityNotFoundException.class, entityNotFoundException.getClass());
    }

    @Test
    @DisplayName("Verify save() method. Should save category and return CategoryResponseDto")
    void save_ValidCategoryRequestDto_ShouldSaveBookAndReturnCategoryResponseDto() {
        //Given
        CategoryRequestDto categoryRequestDto = createCategoryRequestDto();
        Category firstCategory = createFirstCategory();
        CategoryResponseDto expected = convertCategoryToResponseDto(firstCategory);

        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(firstCategory);
        when(categoryRepository.save(firstCategory)).thenReturn(firstCategory);
        when(categoryMapper.toDto(firstCategory)).thenReturn(expected);

        //When
        CategoryResponseDto actual = categoryService.save(categoryRequestDto);

        //Then
        assertEquals(expected, actual);
        verify(categoryMapper, times(1)).toEntity(categoryRequestDto);
        verify(categoryRepository, times(1)).save(firstCategory);
        verify(categoryMapper, times(1)).toDto(firstCategory);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("Verify update() method. Should update category and return CategoryResponseDto")
    void update_ValidCategoryIdAndCategoryRequestDto_ShouldReturnUpdatedCategoryResponseDto() {
        //Given
        CategoryRequestDto categoryRequestDto = createCategoryRequestDto().setName("Fantasy");
        Long id = 1L;
        Category updatedCategory = new Category()
                .setName(categoryRequestDto.getName())
                .setDescription(categoryRequestDto.getDescription());
        CategoryResponseDto expected = convertCategoryToResponseDto(updatedCategory);

        when(categoryRepository.existsById(id)).thenReturn(true);
        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(updatedCategory);
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expected);

        //When
        CategoryResponseDto actual = categoryService.update(id, categoryRequestDto);

        //Then
        assertEquals(expected, actual);
        verify(categoryRepository, times(1)).existsById(id);
        verify(categoryMapper, times(1)).toEntity(categoryRequestDto);
        verify(categoryRepository, times(1)).save(updatedCategory);
        verify(categoryMapper, times(1)).toDto(updatedCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify update() method. Should display exception message")
    void update_InvalidCategoryId_ShouldThrowExceptionMessage() {
        //Given
        CategoryRequestDto categoryRequestDto = createCategoryRequestDto();
        Long invalidCategoryId = 5L;
        String expected = "Can't find category by id " + invalidCategoryId;
        when(categoryRepository.existsById(invalidCategoryId)).thenThrow(
                new EntityNotFoundException(expected)
        );

        //When
        EntityNotFoundException entityNotFoundException = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.update(invalidCategoryId, categoryRequestDto)
        );

        //Then
        assertEquals(expected, entityNotFoundException.getMessage());
        assertEquals(EntityNotFoundException.class, entityNotFoundException.getClass());
    }

    @Test
    @DisplayName("Verify deleteById() method. Should delete category by it's id")
    void deleteById_ValidCategoryId_ShouldDeleteCategory() {
        doNothing().when(categoryRepository).deleteById(anyLong());
        categoryService.deleteById(anyLong());
        verify(categoryRepository, times(1)).deleteById(anyLong());
    }

    private static Category createFirstCategory() {
        return new Category()
                .setId(1L)
                .setName("Novel")
                .setDescription("Category description");
    }

    private static Category createSecondCategory() {
        return new Category()
                .setId(2L)
                .setName("Detective")
                .setDescription("Detective description");
    }

    private static CategoryResponseDto convertCategoryToResponseDto(Category category) {
        return new CategoryResponseDto()
                .setId(category.getId())
                .setName(category.getName())
                .setDescription(category.getDescription());
    }

    private static CategoryRequestDto createCategoryRequestDto() {
        return new CategoryRequestDto()
                .setName("Novel")
                .setDescription("Category description");
    }
}

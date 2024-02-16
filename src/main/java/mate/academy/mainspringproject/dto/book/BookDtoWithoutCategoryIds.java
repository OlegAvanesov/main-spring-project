package mate.academy.mainspringproject.dto.book;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class BookDtoWithoutCategoryIds {
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false, unique = true)
    private String isbn;
    @Column(nullable = false)
    private BigDecimal price;
    private String description;
    private String coverImage;
}

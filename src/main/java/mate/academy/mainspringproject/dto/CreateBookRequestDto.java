package mate.academy.mainspringproject.dto;

import jakarta.persistence.Column;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
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

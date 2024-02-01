package mate.academy.mainspringproject.dto.book;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String author;
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "[\\d\\-]+", message = "You can use only numbers and dash")
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
}

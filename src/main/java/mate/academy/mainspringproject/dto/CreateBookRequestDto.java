package mate.academy.mainspringproject.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @Column(nullable = false)
    @NotNull
    private String title;
    @Column(nullable = false)
    @NotNull
    private String author;
    @Column(nullable = false, unique = true)
    @NotNull
    @Pattern(regexp = "[\\d\\-]+", message = "You can use only numbers and dash")
    private String isbn;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
}

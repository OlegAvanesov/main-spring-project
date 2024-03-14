package mate.academy.mainspringproject.dto.category;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotEmpty
    private String name;
    private String description;
}

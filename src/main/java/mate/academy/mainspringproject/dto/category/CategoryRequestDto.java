package mate.academy.mainspringproject.dto.category;

import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryRequestDto {
    @NotEmpty
    private String name;
    private String description;
}

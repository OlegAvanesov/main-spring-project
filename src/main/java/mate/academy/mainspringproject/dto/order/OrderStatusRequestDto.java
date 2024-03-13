package mate.academy.mainspringproject.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusRequestDto {
    @NotNull
    private String status;
}

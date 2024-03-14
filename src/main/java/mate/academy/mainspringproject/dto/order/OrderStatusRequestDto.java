package mate.academy.mainspringproject.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.mainspringproject.model.Order;

@Data
public class OrderStatusRequestDto {
    @NotNull
    private Order.Status status;
}

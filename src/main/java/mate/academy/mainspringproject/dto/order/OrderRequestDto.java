package mate.academy.mainspringproject.dto.order;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OrderRequestDto {
    @NotEmpty
    private String shippingAddress;
}

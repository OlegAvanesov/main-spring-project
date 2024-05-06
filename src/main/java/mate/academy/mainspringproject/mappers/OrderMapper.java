package mate.academy.mainspringproject.mappers;

import mate.academy.mainspringproject.config.MapperConfig;
import mate.academy.mainspringproject.dto.order.OrderRequestDto;
import mate.academy.mainspringproject.dto.order.OrderResponseDto;
import mate.academy.mainspringproject.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "orderItems",
            qualifiedByName = "toOrderItemResponseDtoSet"
    )
    OrderResponseDto toDto(Order order);

    Order toModel(OrderRequestDto requestDto);
}

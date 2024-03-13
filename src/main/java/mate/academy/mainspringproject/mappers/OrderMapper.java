package mate.academy.mainspringproject.mappers;

import mate.academy.mainspringproject.config.MapperConfig;
import mate.academy.mainspringproject.dto.order.OrderRequestDto;
import mate.academy.mainspringproject.dto.order.OrderResponseDto;
import mate.academy.mainspringproject.dto.orderitem.OrderItemResponseDto;
import mate.academy.mainspringproject.model.Order;
import mate.academy.mainspringproject.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderResponseDto toDto(Order order);

    Order toModel(OrderRequestDto requestDto);

    @AfterMapping
    default void setBookId(
            @MappingTarget OrderItemResponseDto orderItemResponseDto, OrderItem orderItem
    ) {
        orderItemResponseDto.setBookId(orderItem.getBook().getId());
    }

    @AfterMapping
    default void setUserId(
            @MappingTarget OrderResponseDto orderResponseDto, Order order
    ) {
        orderResponseDto.setUserId(order.getUser().getId());
    }
}

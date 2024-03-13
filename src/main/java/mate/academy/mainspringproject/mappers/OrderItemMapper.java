package mate.academy.mainspringproject.mappers;

import mate.academy.mainspringproject.config.MapperConfig;
import mate.academy.mainspringproject.dto.orderitem.OrderItemResponseDto;
import mate.academy.mainspringproject.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemResponseDto toDto(OrderItem orderItem);

    @AfterMapping
    default void setBookId(
            @MappingTarget OrderItemResponseDto orderItemResponseDto, OrderItem orderItem
    ) {
        orderItemResponseDto.setBookId(orderItem.getBook().getId());
    }
}

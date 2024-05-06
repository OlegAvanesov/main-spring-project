package mate.academy.mainspringproject.mappers;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.mainspringproject.config.MapperConfig;
import mate.academy.mainspringproject.dto.orderitem.OrderItemResponseDto;
import mate.academy.mainspringproject.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);

    @Named(value = "toOrderItemResponseDtoSet")
    default Set<OrderItemResponseDto> toOrderItemDtoSet(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}

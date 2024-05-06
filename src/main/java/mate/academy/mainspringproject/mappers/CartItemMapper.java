package mate.academy.mainspringproject.mappers;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.mainspringproject.config.MapperConfig;
import mate.academy.mainspringproject.dto.cartitem.CartItemRequestDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemResponseDto;
import mate.academy.mainspringproject.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItem toEntity(CartItemRequestDto requestDto);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toDto(CartItem cartItem);

    @Named(value = "toCartItemResponseDtoSet")
    default Set<CartItemResponseDto> toCartItemDtoSet(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}

package mate.academy.mainspringproject.mappers;

import mate.academy.mainspringproject.config.MapperConfig;
import mate.academy.mainspringproject.dto.cartitem.CartItemRequestDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemResponseDto;
import mate.academy.mainspringproject.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.mainspringproject.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItem toEntity(CartItemRequestDto requestDto);

    CartItemResponseDto toDto(CartItem cartItem);

    CartItemUpdateRequestDto toUpdatedDto(CartItem cartItem);
}

package mate.academy.mainspringproject.mappers;

import mate.academy.mainspringproject.config.MapperConfig;
import mate.academy.mainspringproject.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.mainspringproject.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCart toEntity(ShoppingCartResponseDto responseDto);

    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}

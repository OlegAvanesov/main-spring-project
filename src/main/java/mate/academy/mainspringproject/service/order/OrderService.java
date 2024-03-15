package mate.academy.mainspringproject.service.order;

import java.util.List;
import mate.academy.mainspringproject.dto.order.OrderRequestDto;
import mate.academy.mainspringproject.dto.order.OrderResponseDto;
import mate.academy.mainspringproject.dto.orderitem.OrderItemResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderResponseDto save(OrderRequestDto requestDto, Authentication authentication);

    List<OrderResponseDto> findAllUserOrders(Authentication authentication, Pageable pageable);

    List<OrderItemResponseDto> findAllOrderItemsFromOrder(Long orderId);

    OrderItemResponseDto findSpecificOrderItemInOrder(Long orderId, Long itemId);

    void changeOrderStatus(Long id, String status);
}

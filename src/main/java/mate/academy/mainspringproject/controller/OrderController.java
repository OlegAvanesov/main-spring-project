package mate.academy.mainspringproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.dto.order.OrderRequestDto;
import mate.academy.mainspringproject.dto.order.OrderResponseDto;
import mate.academy.mainspringproject.dto.order.OrderStatusRequestDto;
import mate.academy.mainspringproject.dto.orderitem.OrderItemResponseDto;
import mate.academy.mainspringproject.service.order.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RestController
@RequestMapping(value = "/api/orders")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Create an order",
            description = "Create a new order and save it into Database")
    public OrderResponseDto createOrder(
            @RequestBody @Valid OrderRequestDto requestDto, Authentication authentication
    ) {
        return orderService.save(requestDto, authentication);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get all orders", description = "Get all user's orders")
    public List<OrderResponseDto> getAllOrders(Authentication authentication, Pageable pageable) {
        return orderService.findAllUserOrders(authentication, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all order item from order",
            description = "Get all order items from specific order")
    public List<OrderItemResponseDto> getAllOrderItemFromOrder(@PathVariable("orderId") Long id) {
        return orderService.findAllOrderItemsFromOrder(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{id}")
    @Operation(summary = "Find an order item",
            description = "Find specific order item in specific order")
    public OrderItemResponseDto findSpecificOrderItemInOrder(
            @PathVariable("orderId") Long orderId, @PathVariable Long id
    ) {
        return orderService.findSpecificOrderItemInOrder(orderId, id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Change order's status", description = "Change specific order's status")
    public void changeOrderStatus(
            @PathVariable Long id, @RequestBody @Valid OrderStatusRequestDto requestDto
    ) {
        orderService.changeOrderStatus(id, requestDto.getStatus());
    }
}

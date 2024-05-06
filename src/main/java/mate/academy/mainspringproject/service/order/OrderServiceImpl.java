package mate.academy.mainspringproject.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.mainspringproject.dto.order.OrderRequestDto;
import mate.academy.mainspringproject.dto.order.OrderResponseDto;
import mate.academy.mainspringproject.dto.orderitem.OrderItemResponseDto;
import mate.academy.mainspringproject.exception.EntityNotFoundException;
import mate.academy.mainspringproject.mappers.OrderItemMapper;
import mate.academy.mainspringproject.mappers.OrderMapper;
import mate.academy.mainspringproject.model.CartItem;
import mate.academy.mainspringproject.model.Order;
import mate.academy.mainspringproject.model.OrderItem;
import mate.academy.mainspringproject.model.ShoppingCart;
import mate.academy.mainspringproject.model.User;
import mate.academy.mainspringproject.repository.order.OrderRepository;
import mate.academy.mainspringproject.repository.orderitem.OrderItemRepository;
import mate.academy.mainspringproject.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.mainspringproject.service.user.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderResponseDto save(OrderRequestDto requestDto, Authentication authentication) {
        Order order = orderMapper.toModel(requestDto);
        orderRepository.save(order);

        User authenticatedUser = (User) authentication.getPrincipal();
        User userFromDb = userService.findById(authenticatedUser.getId());
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userFromDb.getId());

        updateOrder(order, shoppingCart);

        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> findAllUserOrders(
            Authentication authentication, Pageable pageable
    ) {
        User authenticatedUser = (User) authentication.getPrincipal();
        User userFromDb = userService.findById(authenticatedUser.getId());
        List<Order> allUserOrders = orderRepository.findAllByUserId(userFromDb.getId(), pageable);
        return allUserOrders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemResponseDto> findAllOrderItemsFromOrder(Long id) {
        Order order = findOrderById(id);
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto findSpecificOrderItemInOrder(Long orderId, Long itemId) {
        OrderItem specificOrderItem = orderRepository.findSpecificOrderItem(orderId, itemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find OrderItem with this id: " + itemId
                ));
        return orderItemMapper.toDto(specificOrderItem);
    }

    @Override
    @Transactional
    public void changeOrderStatus(Long id, String status) {
        Order order = findOrderById(id);
        order.setStatus(Order.Status.valueOf(status));
    }

    private void updateOrder(Order order, ShoppingCart shoppingCart) {
        order.setUser(shoppingCart.getUser());
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItemList = shoppingCart.getCartItems().stream()
                .map(cartItem -> convertCartItemToOrderItem(cartItem, order))
                .toList();
        order.getOrderItems().addAll(orderItemList);

        BigDecimal total = order.getOrderItems().stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
    }

    @Transactional
    private OrderItem convertCartItemToOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(cartItem.getId());
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(orderItem.getBook().getPrice());
        return orderItemRepository.save(orderItem);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Can't find order with this id: " + id));
    }
}

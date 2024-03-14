package mate.academy.mainspringproject.repository.order;

import java.util.List;
import java.util.Optional;
import mate.academy.mainspringproject.model.Order;
import mate.academy.mainspringproject.model.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long id, Pageable pageable);

    @Query("SELECT oi FROM Order o JOIN o.orderItems oi WHERE o.id = :orderId AND oi.id = :itemId")
    Optional<OrderItem> findSpecificOrderItem(Long orderId, Long itemId);
}

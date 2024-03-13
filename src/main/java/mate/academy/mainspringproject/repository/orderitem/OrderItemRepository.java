package mate.academy.mainspringproject.repository.orderitem;

import mate.academy.mainspringproject.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

package mate.academy.mainspringproject.repository.order;

import java.util.List;
import mate.academy.mainspringproject.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("FROM Order o WHERE o.user.id = :id")
    List<Order> findAllByUserId(Long id, Pageable pageable);
}

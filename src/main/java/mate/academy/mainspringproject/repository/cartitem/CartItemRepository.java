package mate.academy.mainspringproject.repository.cartitem;

import mate.academy.mainspringproject.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

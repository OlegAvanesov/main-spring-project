package mate.academy.mainspringproject.repository.shoppingcart;

import mate.academy.mainspringproject.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("FROM ShoppingCart s WHERE s.user.id = :id")
    ShoppingCart findByUserId(Long id);
}

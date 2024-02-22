package mate.academy.mainspringproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;

@Entity
@Data
@Table(name = "cart_items")
@SQLDelete(sql = "UPDATE cart_items SET is_deleted = TRUE WHERE id = ?")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "shopping_cart", nullable = false)
    @ManyToOne
    private ShoppingCart shoppingCart;
    @JoinColumn(nullable = false)
    @ManyToOne
    private Book book;
    @Column(nullable = false)
    private int quantity;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}

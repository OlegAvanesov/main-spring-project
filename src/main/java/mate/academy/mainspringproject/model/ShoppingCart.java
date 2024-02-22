package mate.academy.mainspringproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Data
@Table(name = "shopping_carts")
@SQLDelete(sql = "UPDATE shopping_carts SET is_deleted = TRUE WHERE id = ?")
@SQLRestriction("is_deleted = FALSE")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;
    @Column(name = "cart_items")
    @OneToMany (mappedBy = "shoppingCart")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CartItem> cartItems = new HashSet<>();
    @Column(name = "is_deleted")
    private boolean isDeleted;
}

package mate.academy.mainspringproject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@EqualsAndHashCode
@SQLDelete(sql = "UPDATE cart_items SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Accessors(chain = true)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private ShoppingCart shoppingCart;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Book book;
    @Column(nullable = false)
    private int quantity;
    private boolean isDeleted;
}

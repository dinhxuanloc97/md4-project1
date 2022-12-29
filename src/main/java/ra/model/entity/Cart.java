package ra.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartId")
    private int cartId;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "totalPrice")
    private float totalPrice;
    @Column(name="cartStatus")
    private boolean cartStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="userId")
    private Users users;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "productId")
    private Product product;

}

package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="orderdetail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oderDetailId")
    private int oderDetailId;
    @Column(name="quantity")
    private int quantity;
    @Column(name="totalPrice")
    private float totalPrice;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name="userId")
    private Users users;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "productId")
    private Product product;
    @Column(name="orderStatus")
    private int orderStatus;
    @Column(name="createDate")
    private Date createDate;
    @Column(name="price")
    private float price;

}

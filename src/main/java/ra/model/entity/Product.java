package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductId")
    private int productId;
    @Column(name = "ProductName")
    private String productName;
    @Column(name = "ProductPrice")
    private float productPrice;
    @Column(name = "ProductQuantity")
    private int productQuantity;
    @Column(name = "ProductBirthOfDate")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date productBirthOfDate;
    @Column(name = "ProductStatus")
    private boolean productStatus;
    @ManyToOne
    @JoinColumn(name = "CatalogId")
//    @JsonIgnore
    private Catalog catalog;
    @OneToMany(mappedBy = "product")
    List<Image> listImage = new ArrayList<>();

}

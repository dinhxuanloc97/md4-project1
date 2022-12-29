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
    @Column(name = "productId")
    private int productId;
    @Column(name = "productName")
    private String productName;
    @Column(name = "productPrice")
    private float productPrice;
    @Column(name = "productQuantity")
    private int productQuantity;
    @Column(name = "productBirthOfDate")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date productBirthOfDate;
    @Column(name = "productStatus")
    private boolean productStatus;
    @ManyToOne
    @JoinColumn(name = "catalogId")
//    @JsonIgnore
    private Catalog catalog;
    @OneToMany(mappedBy = "product")
    List<Image> listImage = new ArrayList<>();

}

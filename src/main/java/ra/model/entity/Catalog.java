package ra.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "catalog")
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="catalogId")
    private int catalogId;

    @Column(name = "catalogName")
    private String catalogName;

    @Column(name= "catalogStatus")
    private Boolean catalogStatus;

//    @OneToMany(mappedBy = "catalog")
//    private List<Product> productList = new ArrayList<>();

}

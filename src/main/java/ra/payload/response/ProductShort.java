package ra.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.model.entity.Catalog;
import ra.model.entity.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductShort {
    private  int productId;
    private String productName;
    private float productPrice;
    private int productQuantity;
    private Date productBirthOfDate;
    private boolean productStatus;
    private Catalog catalog;
    private List<Image> listImage = new ArrayList<>();
}

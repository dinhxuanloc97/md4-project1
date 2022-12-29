package ra.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private  int productId;
    private String productName;
    private float productPrice;
    private int productQuantity;
    private Date productBirthOfDate;
    private boolean productStatus;
    private int catalogId;
    private List<String> listImage = new ArrayList<>();

}

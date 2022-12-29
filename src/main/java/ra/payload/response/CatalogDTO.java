package ra.payload.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.model.entity.Product;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class CatalogDTO {

    private int catalogId;

    private String catalogName;

    private Boolean catalogStatus;

    private List<Product> productList = new ArrayList<>();


}

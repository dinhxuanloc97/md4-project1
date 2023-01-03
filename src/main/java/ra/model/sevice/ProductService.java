package ra.model.sevice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product finById(int productId);
    Product saveOfUpdate(Product pro);
    void delete(int productId);
    List<Product> searchName(String productName);
    List<Product> seachProductByCatalogId(int catalogId);
    List<Product> sortProductByProductName(String direction);
    Page<Product> getPagging (Pageable pageablee);
    List<Product> listWishList(int userId);

}

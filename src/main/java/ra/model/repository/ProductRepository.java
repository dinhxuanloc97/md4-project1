package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByProductNameContaining(String productName);

    List<Product> findByCatalog_CatalogId(Integer catalogId);
    @Query(value ="select p.productId, p.productName, p.productBirthOfDate,p.productPrice, p.productQuantity,p.productStatus,p.catalogId" +
            " from  product p join wishlist w on p.productId = w.productId where w.userId = :uId",nativeQuery = true)
    List<Product> findAllWishList(@Param("uId")int userId);
}

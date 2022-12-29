package ra.model.sevice;

import ra.model.entity.Cart;

import javax.persistence.Entity;
import java.util.List;


public interface CartSevice {
    Cart insert(Cart cart);

    void delete(int cartId);

    Cart findById(int cartId);

    List<Cart> findAllUserId(int userId);


}

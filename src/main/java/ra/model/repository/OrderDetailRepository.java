package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ra.model.entity.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {

    List<OrderDetail> findByUsers_UserId(int id);
    List<OrderDetail> findByUsers_UserIdAndOrderStatus(int id,boolean status);

    @Query(value = "from OrderDetail o where o.orderStatus=1 ")
    List<OrderDetail> getAllOrderDetailswaitForConfirmation();
    @Query(value = "from OrderDetail o where o.orderStatus=2 ")
    List<OrderDetail> getAllOrderDetailSpreparingGoods();

    @Query(value = "from OrderDetail o where o.orderStatus=3")
    List<OrderDetail> getAllOrderDetailsDelivering();

    @Query(value = "from OrderDetail o where o.orderStatus=4 ")
    List<OrderDetail> getAllOrderDetailsFinish();

    @Query(value = "from OrderDetail o where o.orderStatus>=5")
    List<OrderDetail> getAllOrderDetailScancelOrder();

}

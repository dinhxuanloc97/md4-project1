package ra.model.sevice;

import ra.model.entity.OrderDetail;

import java.util.List;

public interface OrderDetailSevice {
    OrderDetail save(OrderDetail orderDetail);
    List<OrderDetail> getALl();
    List<OrderDetail> findAll(int id);
    List<OrderDetail> findAllByStatus(int id,boolean status);
    OrderDetail findById(int id);
    List<OrderDetail> getOrderForShop(int id);
    List<OrderDetail> findAllOrderByStatus(int id,boolean status);

    List<OrderDetail> getAllOrderDetailswaitForConfirmation();
    List<OrderDetail> getAllOrderDetailSpreparingGoods();
    List<OrderDetail> getAllOrderDetailsDelivering();
    List<OrderDetail> getAllOrderDetailsFinish();
    List<OrderDetail> getAllOrderDetailScancelOrder();
}

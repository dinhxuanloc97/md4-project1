package ra.model.seviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.OrderDetail;
import ra.model.repository.OrderDetailRepository;
import ra.model.sevice.OrderDetailSevice;

import java.util.List;

@Service
public class OrderDetailSeviceImp implements OrderDetailSevice {

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public List<OrderDetail> getALl() {
        return orderDetailRepository.findAll();
    }

    @Override
    public List<OrderDetail> findAll(int id) {
        return orderDetailRepository.findByUsers_UserId(id);
    }

    @Override
    public List<OrderDetail> findAllByStatus(int id, boolean status) {
        return null;
    }

    @Override
    public OrderDetail findById(int id) {
        return orderDetailRepository.findById(id).get();
    }

    @Override
    public List<OrderDetail> getOrderForShop(int id) {
        return null;
    }

    @Override
    public List<OrderDetail> findAllOrderByStatus(int id, boolean status) {
        return null;
    }

    @Override
    public List<OrderDetail> getAllOrderDetailswaitForConfirmation() {
        return orderDetailRepository.getAllOrderDetailswaitForConfirmation();
    }

    @Override
    public List<OrderDetail> getAllOrderDetailSpreparingGoods() {
        return orderDetailRepository.getAllOrderDetailSpreparingGoods();
    }

    @Override
    public List<OrderDetail> getAllOrderDetailsDelivering() {
        return orderDetailRepository.getAllOrderDetailsDelivering();
    }

    @Override
    public List<OrderDetail> getAllOrderDetailsFinish() {
        return orderDetailRepository.getAllOrderDetailsFinish();
    }

    @Override
    public List<OrderDetail> getAllOrderDetailScancelOrder() {
        return orderDetailRepository.getAllOrderDetailScancelOrder();
    }
}

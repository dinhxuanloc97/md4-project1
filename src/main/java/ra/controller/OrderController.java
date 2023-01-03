package ra.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.Cart;
import ra.model.entity.OrderDetail;
import ra.model.entity.Users;
import ra.model.sevice.CartSevice;
import ra.model.sevice.OrderDetailSevice;
import ra.model.sevice.ProductService;
import ra.model.sevice.UserService;
import ra.payload.request.OrderDetailRequest;
import ra.payload.response.OrderDetailResponse;
import ra.security.CustomUserDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/order")
public class OrderController {
    @Autowired
    private OrderDetailSevice orderDetailSevice;
    @Autowired
    private CartSevice cartSevice;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    

    @GetMapping("getAllOrder")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDetail> getAllOrder(){
        return orderDetailSevice.getALl();
    }

    @GetMapping("getAllOrderUS")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllOrderUS(){
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<OrderDetail> list = orderDetailSevice.findAll(customUserDetails.getUserId());
        List<OrderDetailResponse> listResponse = new ArrayList<>();
        for (OrderDetail orderDetail :list) {
            String status = "";
            if (orderDetail.getOrderStatus() == 1) {
                status = "Chờ xác nhận";
            } else if (orderDetail.getOrderStatus() == 2) {
                status = "Đang chuẩn bị hàng";
                } else if (orderDetail.getOrderStatus() == 3) {
                status = "Đang giao hàng";
            } else if (orderDetail.getOrderStatus() == 4) {
                status = "Giao hàng thành công";
            } else {
                status = "Đơn hàng đã huỷ";
            }
            OrderDetailResponse order = new OrderDetailResponse();
            order.setOrderId(orderDetail.getOderDetailId());
            order.setProductName(orderDetail.getProduct().getProductName());
            order.setPrice(orderDetail.getPrice());
            order.setQuantity(orderDetail.getQuantity());
            order.setCreateDate(orderDetail.getCreateDate());
            order.setTotalPrice(order.getTotalPrice());
//            order.setUserId(orderDetail.getUsers().getUserId());
            order.setOrderStatus(status);
            listResponse.add(order);
        }

        return ResponseEntity.ok(listResponse);
    }


    @PostMapping("addOrder")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> insertOrder(@RequestBody OrderDetailRequest orderDetailRequest) {
        boolean checkExit = true;
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userService.findByUserId(customUserDetails.getUserId());
        try {
            for (int id : orderDetailRequest.getListCart()) {
                OrderDetail order = new OrderDetail();
                Cart cart = cartSevice.findById(id);
                order.setProduct(cart.getProduct());
                order.setPrice(cart.getProduct().getProductPrice());
                order.setUsers(users);
                order.setQuantity(cart.getQuantity());
                order.setTotalPrice(order.getPrice() * order.getQuantity());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dateNow = new Date();
                String strNow = sdf.format(dateNow);
                order.setCreateDate(sdf.parse(strNow));
                order.setOrderStatus(1);
                order = orderDetailSevice.save(order);
            }
        } catch (Exception e) {
            checkExit = false;
            e.printStackTrace();
        }
        if (checkExit) {
            for (int id : orderDetailRequest.getListCart()) {
                cartSevice.delete(id);
            }
            return ResponseEntity.ok(" Đã mua hàng thành công");
        } else {
            return ResponseEntity.ok("Mua hàng thất bại!");
        }
    }

    @PutMapping("updateOrder/{orderDetailsId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateDetails(@RequestBody OrderDetail orderDetails, @PathVariable("orderDetailsId") int orderDetailsId) throws ParseException {
        OrderDetail orderDetailsUp = orderDetailSevice.findById(orderDetailsId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        orderDetailsUp.setCreateDate(sdf.parse(strNow));
        orderDetailsUp.setOrderStatus(orderDetails.getOrderStatus());
        orderDetailSevice.save(orderDetailsUp);
        return ResponseEntity.ok("Trạng thái đơn hàng cập nhập  thành công!");
    }
    @GetMapping("/getAllOrderDetailswaitForConfirmation")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDetailResponse> getAllOrderDetailswaitForConfirmation(){
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<OrderDetail> list = orderDetailSevice.findAll(customUserDetails.getUserId());
        List<OrderDetailResponse> listResponse = new ArrayList<>();
        for (OrderDetail orderDetail :list) {

            OrderDetailResponse order = new OrderDetailResponse();
            order.setOrderId(orderDetail.getOderDetailId());
            order.setProductName(orderDetail.getProduct().getProductName());
            order.setPrice(orderDetail.getPrice());
            order.setQuantity(orderDetail.getQuantity());
            order.setCreateDate(orderDetail.getCreateDate());
            order.setTotalPrice(order.getTotalPrice());
//            order.setUserId(orderDetail.getUsers().getUserId());
            order.setOrderStatus("Chờ xác nhận");
            listResponse.add(order);
        }
        return listResponse;
    }



}

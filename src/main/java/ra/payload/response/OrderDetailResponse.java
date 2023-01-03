package ra.payload.response;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDetailResponse {
    private int orderId;
    private String productName;
    private int quantity;
    private float price;
    private float totalPrice;
    private Date createDate;
    private String orderStatus;
    private int userId;
}

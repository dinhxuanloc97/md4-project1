package ra.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderDetailRequest {
    private List<Integer> listCart;
    private List<Integer> listQuantity;
}

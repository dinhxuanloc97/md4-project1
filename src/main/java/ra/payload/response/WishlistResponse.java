package ra.payload.response;

import lombok.Data;

@Data
public class WishlistResponse {
    private int wishlistId;
    private String productName;
    private int quantity;
    private float price;
}

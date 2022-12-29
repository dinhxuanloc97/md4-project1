package ra.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.Cart;
import ra.model.entity.Product;
import ra.model.repository.CatalogRepository;
import ra.model.sevice.CartSevice;
import ra.model.sevice.CatalogSevice;
import ra.model.sevice.ProductService;
import ra.model.sevice.UserService;
import ra.payload.request.CartRequest;
import ra.payload.response.CartResponse;
import ra.security.CustomUserDetails;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("api/v1/cart")

public class CartController {

    @Autowired
    private CatalogSevice catalogSevice;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartSevice cartSevice;

    @Autowired
    private UserService userService;

    @Autowired
    private CatalogRepository catalogRepository;

    //    @PreAuthorize("hasRole('USER')")

    @GetMapping("getAllCart")
    public ResponseEntity<?> getAllCart() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Cart> listCart = cartSevice.findAllUserId(customUserDetails.getUserId());
        List<CartResponse> listCartRespose = new ArrayList<>();
        for (Cart cart : listCart) {
            CartResponse cartRespose = new CartResponse();
            cartRespose.setCartId(cart.getCartId());
            cartRespose.setQuantity(cart.getQuantity());
            cartRespose.setPrice(cart.getProduct().getProductPrice());
            cartRespose.setTotalPrice(cart.getTotalPrice());
            cartRespose.setProductName(cart.getProduct().getProductName());
            listCartRespose.add(cartRespose);
        }
        return ResponseEntity.ok(listCartRespose);
    }

    @PostMapping("addCart")
    public ResponseEntity<?> addToCart(@RequestBody CartRequest cartRequest) {

        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean check =true;
        List<Cart> listCart = cartSevice.findAllUserId(customUserDetails.getUserId());
        Cart cart = new Cart();
        boolean checkexit = false;
        try {
            for (Cart cart1 :listCart) {
                if (cart1.getProduct().getProductId()==cartRequest.getProductId()){
                    cart = cart1;
                    checkexit = true;
                    break;
                }
            }
            if (checkexit){
                cart.setQuantity(cart.getQuantity()+cartRequest.getQuantity());
                cart = cartSevice.insert(cart);

            }else {
                cart.setQuantity(cartRequest.getQuantity());
                cart.setProduct(productService.finById(cartRequest.getProductId()));
                cart.setTotalPrice(cart.getProduct().getProductPrice()*cart.getQuantity());
                cart.setUsers(userService.findByUserId(customUserDetails.getUserId()));
                cart = cartSevice.insert(cart);
            }
        }catch (Exception e){
            check = false;
            e.printStackTrace();
        }
        if (check){

            return ResponseEntity.ok("Thêm sản phẩm vào giỏ hàng thành công!!!");
        }else {

            return ResponseEntity.ok("Thêm sản phẩm vào giỏ hàng thất bại!");
        }
    }

    @PutMapping("update/{cartId}")
    public ResponseEntity<?> updateCart(@RequestParam("productQuantity") int productQuantity, @PathVariable("cartId") int cartId) {
        try {
            Cart cart = cartSevice.findById(cartId);
            if (productQuantity>0) {
                cart.setQuantity(productQuantity);
                cart.setTotalPrice(cart.getProduct().getProductPrice()*cart.getQuantity());
                cartSevice.insert(cart);
            }else {
                cartSevice.delete(cartId);
            }
            return ResponseEntity.ok("Đã cập nhập thành công ");
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.ok("Chưa cập nhập được giỏ hàng");
        }
    }

    @DeleteMapping("delete/{cartId}")
    public ResponseEntity<?> deleteCart(@PathVariable("cartId") int cartId){
            cartSevice.delete(cartId);
            return ResponseEntity.ok("Đã xóa khỏi giỏ hàng");
    }

}

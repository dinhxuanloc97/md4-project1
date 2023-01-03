package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.payload.response.ProductShort;
import ra.payload.request.ProductRequest;
import ra.model.entity.Catalog;
import ra.model.entity.Image;
import ra.model.entity.Product;
import ra.model.sevice.CatalogSevice;
import ra.model.sevice.ImageSevice;
import ra.model.sevice.ProductService;
import ra.security.CustomUserDetails;

import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("api/v1/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CatalogSevice catalogSevice;

    @Autowired
    private ImageSevice imageSevice;

    @GetMapping
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<ProductShort> getAllProduct(){
        List<ProductShort> listProductShort = new ArrayList<>();
        List<Product> listProduct = productService.findAll();
        for (Product pro : listProduct){
            ProductShort productShort = new ProductShort();
            productShort.setProductId(pro.getProductId());
            productShort.setProductName(pro.getProductName());
            productShort.setProductPrice(pro.getProductPrice());
            productShort.setProductQuantity(pro.getProductQuantity());
            productShort.setProductBirthOfDate(pro.getProductBirthOfDate());
            productShort.setProductStatus(pro.isProductStatus());
            productShort.setCatalog(pro.getCatalog());
            productShort.getListImage().addAll(pro.getListImage());
            listProductShort.add(productShort);
        }
        return listProductShort;
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable("productId") int productId) {
        return productService.finById(productId);
    }

    @PostMapping("create")
        @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        try{
        Product pro = new Product();
        pro.setProductName(productRequest.getProductName());
        pro.setProductPrice(productRequest.getProductPrice());
        pro.setProductQuantity(productRequest.getProductQuantity());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        try {
            pro.setProductBirthOfDate(sdf.parse(strNow));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        pro.setProductStatus(productRequest.isProductStatus());
        Catalog catalog = catalogSevice.findById(productRequest.getCatalogId());
        pro.setCatalog(catalog);
        pro = productService.saveOfUpdate(pro);
        for (String str : productRequest.getListImage()) {
            Image image = new Image();
            image.setProduct(pro);
            image.setImageLink(str);
            image = imageSevice.saveOrUpdate(image);
            pro.getListImage().add(image);
        }
        return ResponseEntity.ok(pro);
    } catch (Exception e){
        e.printStackTrace();
        return ResponseEntity.ok("Có lỗi trong quá trình xử lý vui lòng thử lại!");
    }
    }

    @PutMapping("update/{productId}")
        @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") int productId, @RequestBody ProductRequest productRequest) {
        try {
            Product productUpdate = productService.finById(productId);
            productUpdate.setProductName(productRequest.getProductName());
            productUpdate.setProductPrice(productRequest.getProductPrice());
            productUpdate.setProductQuantity(productRequest.getProductQuantity());
            productUpdate.setProductBirthOfDate(productRequest.getProductBirthOfDate());
            productUpdate.setProductStatus(productRequest.isProductStatus());
            Catalog catalog = catalogSevice.findById(productRequest.getCatalogId());
            productUpdate.setCatalog(catalog);
            for (Image image : productUpdate.getListImage()) {
                imageSevice.deleteById(image.getImageId());
            }
            for (String str : productRequest.getListImage()) {
                Image image = new Image();
                image.setProduct(productUpdate);
                image.setImageLink(str);
                image = imageSevice.saveOrUpdate(image);
                productUpdate.getListImage().add(image);
            }
            return ResponseEntity.ok("Cập Nhập Rất Thành Công ");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Chưa Cập Nhập Được ok");
        }
    }

    @DeleteMapping("/delete/{producId}")
        @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteproduct(@PathVariable("producId") int productId) {
        try{
            Product product  = productService.finById(productId);
            for (Image image:product.getListImage()){
                imageSevice.deleteById(image.getImageId());
            }
            productService.delete(productId);
            return ResponseEntity.ok("Đã xóa thành công ");
        }catch (Exception e){
            return ResponseEntity.ok("Chưa xóa được kiểm tra lại ");
        }
    }

    @GetMapping("/search")
    public List<Product> searchName(@RequestParam("productName") String productName) {
        return productService.searchName(productName);
    }

    @GetMapping("/sortByName")
        @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Product>> sortProductByProductName(@RequestParam("direction") String direction) {
        List<Product> listProduct = productService.sortProductByProductName(direction);
        return new ResponseEntity<>(listProduct, HttpStatus.OK);
    }

    @GetMapping("/getPagging")
        @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPagging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> pageProduct = productService.getPagging(pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("products", pageProduct.getContent());
        data.put("total", pageProduct.getSize());
        data.put("totalItems", pageProduct.getTotalElements());
        data.put("totalPages", pageProduct.getTotalPages());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/getPaggingAndSortByName")
        @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPaggingAndSortByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam String direction) {
        Sort.Order order;
        if (direction.equals("asc")) {
            order = new Sort.Order(Sort.Direction.ASC, "productName");
        } else {
            order = new Sort.Order(Sort.Direction.DESC, "productName");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<Product> pageProduct = productService.getPagging(pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("products", pageProduct.getContent());
        data.put("total", pageProduct.getSize());
        data.put("totalItems", pageProduct.getTotalElements());
        data.put("totalPages", pageProduct.getTotalPages());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("getAllWishlist")
    @PreAuthorize("hasRole('USER')")
    public List<ProductShort> getAllWishlist(){
        List<ProductShort> listProductShort = new ArrayList<>();
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Product> listProduct = productService.listWishList(customUserDetails.getUserId());
        for (Product pro : listProduct){
            ProductShort productShort = new ProductShort();
            productShort.setProductId(pro.getProductId());
            productShort.setProductName(pro.getProductName());
            productShort.setProductPrice(pro.getProductPrice());
            productShort.setProductQuantity(pro.getProductQuantity());
            productShort.setProductBirthOfDate(pro.getProductBirthOfDate());
            productShort.setProductStatus(pro.isProductStatus());
            productShort.setCatalog(pro.getCatalog());
            productShort.getListImage().addAll(pro.getListImage());
            listProductShort.add(productShort);
        }
        return listProductShort;
    }

}

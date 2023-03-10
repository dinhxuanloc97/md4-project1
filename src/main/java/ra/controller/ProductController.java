package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.payload.response.ProductShort;
import ra.payload.request.ProductRequest;
import ra.model.entity.Catalog;
import ra.model.entity.Image;
import ra.model.entity.Product;
import ra.model.sevice.CatalogSevice;
import ra.model.sevice.ImageSevice;
import ra.model.sevice.ProductService;

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
        return ResponseEntity.ok("C?? l???i trong qu?? tr??nh x??? l?? vui l??ng th??? l???i!");
    }
    }

    @PutMapping("/{productId}")
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
            return ResponseEntity.ok("C???p Nh???p R???t Th??nh C??ng ");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Ch??a C???p Nh???p ???????c ok");
        }
    }

    @DeleteMapping("/delete/{producId}")
    public ResponseEntity<?> deleteproduct(@PathVariable("producId") int productId) {
        try{
            Product product  = productService.finById(productId);
            for (Image image:product.getListImage()){
                imageSevice.deleteById(image.getImageId());
            }
            productService.delete(productId);
            return ResponseEntity.ok("???? x??a th??nh c??ng ");
        }catch (Exception e){
            return ResponseEntity.ok("Ch??a x??a ???????c ki???m tra l???i ");
        }

    }

    @GetMapping("/search")
    public List<Product> searchName(@RequestParam("productName") String productName) {
        return productService.searchName(productName);
    }

    @GetMapping("/sortByName")
    public ResponseEntity<List<Product>> sortProductByProductName(@RequestParam("direction") String direction) {
        List<Product> listProduct = productService.sortProductByProductName(direction);
        return new ResponseEntity<>(listProduct, HttpStatus.OK);
    }

    @GetMapping("/getPagging")
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

}

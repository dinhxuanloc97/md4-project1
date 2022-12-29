package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.payload.response.CatalogDTO;
import ra.model.entity.Catalog;
import ra.model.sevice.CatalogSevice;
import ra.model.sevice.ProductService;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("api/v1/catalog")
public class CatalogController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CatalogSevice catalogSevice;

    @GetMapping
    public List<Catalog> getAllCatalog() {
        return catalogSevice.findAll();
    }

    @GetMapping("/{catalogId}")
    public CatalogDTO getCatalogId(@PathVariable("catalogId") int catalogId){
      CatalogDTO catalogDTO = new CatalogDTO();
      Catalog catalog = catalogSevice.findById(catalogId);
      catalogDTO.setCatalogId(catalog.getCatalogId());
      catalogDTO.setCatalogName(catalog.getCatalogName());
      catalogDTO.setCatalogStatus(catalog.getCatalogStatus());
      catalogDTO.setProductList(productService.seachProductByCatalogId(catalogId));
      return catalogDTO;
    }

    @PostMapping("create")
    public Catalog createStudent(@RequestBody Catalog catalog){
        return catalogSevice.saveorUpdate(catalog);
    }

    @PutMapping("/{catalogId}")
    public Catalog updateCatalog(@PathVariable("catalogId") int catalogId,@RequestBody Catalog catalog){
        Catalog catalogUpdate = catalogSevice.findById(catalogId);
        catalogUpdate.setCatalogName(catalog.getCatalogName());
        catalogUpdate.setCatalogStatus(catalog.getCatalogStatus());

       return   catalogSevice.saveorUpdate(catalogUpdate);
    }

    @DeleteMapping("/delete/{catalogId}")
    public void DeleteCatalog(@PathVariable("catalogId") int catalogId){
        catalogSevice.delete(catalogId);
    }

    @GetMapping("/search")
    public List<Catalog> searchByName(@RequestParam("catalogName") String catalogName){
    return catalogSevice.searchByName(catalogName);
    }

}

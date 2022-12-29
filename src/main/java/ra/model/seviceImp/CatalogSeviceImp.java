package ra.model.seviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Catalog;
import ra.model.repository.CatalogRepository;
import ra.model.sevice.CatalogSevice;


import java.util.List;

@Service
public class CatalogSeviceImp implements CatalogSevice {

    @Autowired
     private CatalogRepository catalogRepository;


    @Override
    public List<Catalog> findAll() {
        return catalogRepository.findAll();
    }

    @Override
    public Catalog findById(int catalogId) {
        return catalogRepository.findById(catalogId).get();
    }

    @Override
    public Catalog saveorUpdate(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    @Override
    public void delete(int catlogId) {
        catalogRepository.deleteById(catlogId);
    }

    @Override
    public List<Catalog> searchByName(String catalogName) {
        return catalogRepository.findByCatalogNameContaining(catalogName);
    }
}

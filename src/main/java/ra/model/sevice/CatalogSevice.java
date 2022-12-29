package ra.model.sevice;



import ra.model.entity.Catalog;

import java.util.List;

public interface CatalogSevice {
    List<Catalog> findAll();

    Catalog findById(int catalogId);

    Catalog saveorUpdate(Catalog catalog);

    void delete(int catlogId);

    List<Catalog> searchByName(String catalogName);


}

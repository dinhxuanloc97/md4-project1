package ra.model.sevice;

import ra.model.entity.Image;

public interface ImageSevice {
    Image saveOrUpdate(Image image);
    void deleteById(int id);
}

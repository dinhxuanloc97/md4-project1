package ra.model.seviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Image;
import ra.model.repository.ImageRepository;
import ra.model.sevice.ImageSevice;

@Service
public class ImageImp implements ImageSevice {
    @Autowired
    private ImageRepository imageRepository;
    @Override
    public Image saveOrUpdate(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void deleteById(int id) {
    imageRepository.deleteById(id);
    }
}

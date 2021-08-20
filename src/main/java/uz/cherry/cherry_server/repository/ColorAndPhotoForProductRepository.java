package uz.cherry.cherry_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.cherry.cherry_server.entity.Attachment;
import uz.cherry.cherry_server.entity.Color;
import uz.cherry.cherry_server.entity.ColorAndPhotoForProduct;
import uz.cherry.cherry_server.entity.Product;

import java.util.List;

public interface ColorAndPhotoForProductRepository extends JpaRepository<ColorAndPhotoForProduct, Integer> {


    boolean existsByColorAndProductAndActive(Color color, Product product, boolean active);

    List<ColorAndPhotoForProduct> getByProductIdAndActive(Integer product_id, boolean active);

    @Query(value = "select count(*) > 0 from  color_and_photo_for_product as c join color_and_photo_for_product_attachments as a " +
            " on c.id=a.color_and_photo_for_product_id where a.attachments_id=:attachmentId" +
            " and c.active=:active", nativeQuery = true)
    boolean existsByAttachmentAndActive(Integer attachmentId, boolean active);


}

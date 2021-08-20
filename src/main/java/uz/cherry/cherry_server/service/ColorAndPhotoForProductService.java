package uz.cherry.cherry_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cherry.cherry_server.entity.Attachment;
import uz.cherry.cherry_server.entity.Color;
import uz.cherry.cherry_server.entity.ColorAndPhotoForProduct;
import uz.cherry.cherry_server.entity.Product;
import uz.cherry.cherry_server.payload.ColorAndPhoto;
import uz.cherry.cherry_server.payload.ColorAndPhotoForProductDto;
import uz.cherry.cherry_server.payload.Result;
import uz.cherry.cherry_server.repository.AttachmentRepository;
import uz.cherry.cherry_server.repository.ColorAndPhotoForProductRepository;
import uz.cherry.cherry_server.repository.ColorRepository;
import uz.cherry.cherry_server.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ColorAndPhotoForProductService {

    @Autowired
    ColorAndPhotoForProductRepository colorAndPhotoForProductRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    ColorRepository colorRepository;


    public Result add(ColorAndPhotoForProductDto colorAndPhotoForProductDto) {
        Optional<Product> optionalProduct = productRepository.findById(colorAndPhotoForProductDto.getProductId());
        if (!optionalProduct.isPresent()) return new Result("Such product id not exist!", false);

        for (int i = 0; i < colorAndPhotoForProductDto.getColorAndPhotos().size(); i++) {
            Result result = templateValidation(colorAndPhotoForProductDto, optionalProduct.get(), i);
            if (!result.isSuccess()) return result;

            List<Object> objectList = (List<Object>) result.getObject();
            Color color = (Color) objectList.get(0);
            List<Attachment> attachmentList = (List<Attachment>) objectList.get(1);

            ColorAndPhotoForProduct colorAndPhotoForProduct = new ColorAndPhotoForProduct();
            colorAndPhotoForProduct.setProduct(optionalProduct.get());
            colorAndPhotoForProduct.setColor(color);
            colorAndPhotoForProduct.setAttachments(attachmentList);
            colorAndPhotoForProductRepository.save(colorAndPhotoForProduct);
        }
        return new Result("New color and images successfully added for this product!", true);
    }


    public Result getByProductId(Integer productId) {
        List<ColorAndPhotoForProduct> getByProductId = colorAndPhotoForProductRepository.getByProductIdAndActive(productId, true);
        if (getByProductId.size() == 0)
            return new Result("No color and image has been added to this product yet!", false);
        return new Result(getByProductId, true);
    }


    /* Agar productga qo'shilgan color va img ni o'zgartirmoqchi bo'lsa, img lar o'zgartirilmaydida, o'zgaradigan imglar yangi img dek qo'shilib,
     * ularni id lari keladi , agar product uchun oldin 2 ta rang va imglar qo'shilgan bo'lsa keyin o'zgartirishda 3 ta rang va img
     * qo'shmoqchi bo'lsa 3-color va img yangidan qo'shiladi, agar 1ta color va img ni o'chirib tashamoqchi bo'lsa o'zini 2 ta img va color idan
     * bittasi active i false qilinadi  */
    @Transactional
    public Result edit(Integer productId, ColorAndPhotoForProductDto colorAndPhotoForProductDto) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) return new Result("Such product id not exist!", false);

        List<ColorAndPhotoForProduct> colorAndPhotoForProducts = colorAndPhotoForProductRepository.getByProductIdAndActive(productId, true);
        if (colorAndPhotoForProducts.size() == 0)
            return new Result("No color and image has been added to this product yet!", false);

        /* Agar oldin 2 ta color va shunga mos img qo'shgan, endi 2 ta va undan  ko'p color va shu colorlarga mos img qo'shayotgan bo'lsa  */
        if (colorAndPhotoForProducts.size() <= colorAndPhotoForProductDto.getColorAndPhotos().size()) {

            for (int i = 0; i < colorAndPhotoForProductDto.getColorAndPhotos().size(); i++) {
                Result result = templateValidation(colorAndPhotoForProductDto, optionalProduct.get(), i);
                if (!result.isSuccess()) return result;

                List<Object> objectList = (List<Object>) result.getObject();
                Color color = (Color) objectList.get(0);
                List<Attachment> attachmentList = (List<Attachment>) objectList.get(1);


                if (colorAndPhotoForProducts.size() > i) {
                    ColorAndPhotoForProduct colorAndPhotoForProduct = colorAndPhotoForProducts.get(i);
                    colorAndPhotoForProduct.setColor(color);
                    colorAndPhotoForProduct.setAttachments(attachmentList);
                    colorAndPhotoForProductRepository.save(colorAndPhotoForProduct);
                } else {
                    ColorAndPhotoForProduct colorAndPhotoForProduct = new ColorAndPhotoForProduct();
                    colorAndPhotoForProduct.setProduct(optionalProduct.get());
                    colorAndPhotoForProduct.setColor(color);
                    colorAndPhotoForProduct.setAttachments(attachmentList);
                    colorAndPhotoForProductRepository.save(colorAndPhotoForProduct);
                }
            }
        }

        /* Agar oldin 2 ta color va shunga mos img qo'shgan, endi 2 tadan kam color va shu colorlarga mos img qo'shayotgan bo'lsa  */
        if (colorAndPhotoForProducts.size() > colorAndPhotoForProductDto.getColorAndPhotos().size()) {

            for (int i = 0; i < colorAndPhotoForProducts.size(); i++) {

                if (colorAndPhotoForProductDto.getColorAndPhotos().size() > i) {

                    Result result = templateValidation(colorAndPhotoForProductDto, optionalProduct.get(), i);
                    if (!result.isSuccess()) return result;

                    List<Object> objectList = (List<Object>) result.getObject();
                    Color color = (Color) objectList.get(0);
                    List<Attachment> attachmentList = (List<Attachment>) objectList.get(1);

                    ColorAndPhotoForProduct colorAndPhotoForProduct = colorAndPhotoForProducts.get(i);
                    colorAndPhotoForProduct.setColor(color);
                    colorAndPhotoForProduct.setAttachments(attachmentList);
                    colorAndPhotoForProductRepository.save(colorAndPhotoForProduct);
                } else {
                    ColorAndPhotoForProduct colorAndPhotoForProduct = colorAndPhotoForProducts.get(i);
                    colorAndPhotoForProduct.setActive(false);
                    colorAndPhotoForProductRepository.save(colorAndPhotoForProduct);
                }
            }
        }
        return new Result("Given product successfully edited.", true);
    }

    /*  For validation yani colorAndPhotoForProductDto da malumotlar to'g'ri kelayotganini tekshirib beradigan metod  */
    private Result templateValidation(ColorAndPhotoForProductDto colorAndPhotoForProductDto, Product product, int i) {
        ColorAndPhoto colorAndPhoto = colorAndPhotoForProductDto.getColorAndPhotos().get(i);
        if (colorAndPhoto.getColorId() == null) return new Result("Color must not be empty!", false);
        Optional<Color> optionalColor = colorRepository.findById(colorAndPhoto.getColorId());
        if (!optionalColor.isPresent()) return new Result("Such color id not exist!", false);

        if (colorAndPhoto.getAttachmentsId() == null) return new Result("File must not be empty!", false);
        List<Attachment> attachmentList = attachmentRepository.findAllById(colorAndPhoto.getAttachmentsId());
        if (attachmentList.size() == 0) return new Result("Please, send me at least one picture!", false);

        boolean existsByColorAndProductAndActive =
                colorAndPhotoForProductRepository.existsByColorAndProductAndActive(optionalColor.get(), product, true);
        if (existsByColorAndProductAndActive)
            return new Result("This color already added for this product!", false);

        for (Attachment attachment : attachmentList) {
            boolean existsByAttachmentsAndActive = colorAndPhotoForProductRepository.
                    existsByAttachmentAndActive(attachment.getId(), true);
            if (existsByAttachmentsAndActive) return new Result("This photos belongs to another product!", false);
        }

        List<Object> colorAndAttachment = new ArrayList<>();
        colorAndAttachment.add(optionalColor.get());
        colorAndAttachment.add(attachmentList);
        return new Result(colorAndAttachment, true);
    }


    public Result delete(Integer productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) return new Result("Such product id not exist!", false);

        List<ColorAndPhotoForProduct> colorAndPhotoForProducts = colorAndPhotoForProductRepository.getByProductIdAndActive(productId, true);
        if (colorAndPhotoForProducts.size() == 0)
            return new Result("No color and image has been added to this product yet!", false);
        for (ColorAndPhotoForProduct colorAndPhotoForProduct : colorAndPhotoForProducts) {
            colorAndPhotoForProduct.setActive(false);
            colorAndPhotoForProductRepository.save(colorAndPhotoForProduct);
        }
        return new Result("Given product successfully deleted.", true);
    }


}
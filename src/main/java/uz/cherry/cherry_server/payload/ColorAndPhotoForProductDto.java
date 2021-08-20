package uz.cherry.cherry_server.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ColorAndPhotoForProductDto {

    @NotNull(message = "Color and photo must not be empty!")
    List<ColorAndPhoto> colorAndPhotos;

    @NotNull(message = "Product id must not be empty!")
    private Integer productId;

}

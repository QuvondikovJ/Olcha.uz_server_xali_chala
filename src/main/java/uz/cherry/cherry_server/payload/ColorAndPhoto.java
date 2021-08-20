package uz.cherry.cherry_server.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ColorAndPhoto {

    @NotNull(message = "Color id must not be empty!")
    private Integer colorId;

    @NotNull(message = "File id must not be empty!")
    private List<Integer> attachmentsId;

}

package uz.cherry.cherry_server.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto {
    @NotNull(message = "Category name must not be empty!")
    private String name;

    private Integer parentCategoryId;

    @NotNull(message = "Category url must not be empty!")
    private String url;

    private List<Integer> attachmentId;
    private String text;
}

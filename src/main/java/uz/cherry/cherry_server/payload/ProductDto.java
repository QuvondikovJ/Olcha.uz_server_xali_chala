package uz.cherry.cherry_server.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductDto {

    @NotNull(message = "Product name must not be empty!")
    private String name;

    @NotNull(message = "Product manufacturer must not be empty!")
    private Integer manufacturerId;

    @NotNull(message = "Product category must not be empty!")
    private Integer categoryId;

    @NotNull(message = "Product price must not be empty!")
    private Double price;

    private Integer productId; /* Agar sovg'a qo'yolgan bo'lsa, shu sovg'ani id si keladi */
    private Double discount;
    private Double installmentPlan;
    private String aboutProduct;


}

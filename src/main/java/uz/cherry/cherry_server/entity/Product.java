package uz.cherry.cherry_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.cherry.cherry_server.entity.template.General;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product extends General {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Manufacturer manufacturer;

    @Column(nullable = false)
    private boolean isFasterDelivery;


    /* Agar sovg'a qo'yilgan bo'lsa qanday id li sovg'a qo'yilgan, agar sovg'a qo'yilmagan bo'lsa null bo'ladi  */
    @ManyToOne
    private Product product;

    @Column(nullable = false)
    private Double price;

    private Double discount;

    private Double installmentPlan;

    /* Agar product haqida qo'shimcha malumot bo'lsa shu columnga joylashadi */
    private String aboutProduct;

    @ManyToOne
    private Category category;

}

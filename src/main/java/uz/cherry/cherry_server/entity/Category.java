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
public class Category extends General {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Category parentCategory;

    @Column(nullable = false)
    private String url;

    /* Grand category va father category da img lari bo'ladi, child categoryni img bo'lmaydi*/
    @OneToOne
    private Attachment attachment;

    private String text;

}

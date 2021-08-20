package uz.cherry.cherry_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.cherry.cherry_server.entity.template.General;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ColorAndPhotoForProduct extends General {

    @OneToOne
    private Color color;

    @OneToMany
    private List<Attachment> attachments;

    @ManyToOne
    private Product product;
}

package uz.cherry.cherry_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.cherry.cherry_server.entity.template.General;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CharacteristicName extends General {

    private String characteristicKey;

}

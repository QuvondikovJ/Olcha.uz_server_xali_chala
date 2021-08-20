package uz.cherry.cherry_server.payload;

import lombok.Data;

import java.util.List;

@Data
public class CharDepartmentDto {

    private List<String> characteristicNames;
    private Integer productId;

}

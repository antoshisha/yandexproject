package ru.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.entity.ShopUnit;

@Getter
@Setter
@ToString
public class ShopUnitImportDTO {
    private String id;
    private String name;
    private ShopUnit parentId;
//    private String parentId;
    private ShopUnit.ShopUnitType type;
    private Integer price;

}

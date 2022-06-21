package ru.dto;

import lombok.Getter;
import lombok.Setter;
import ru.entity.ShopUnit;

@Getter
@Setter
public class ShopUnitImportDTO {
    private String id;
    private String name;
    private String parentUid;
    private ShopUnit.ShopUnitType type;
    private Integer price;

}

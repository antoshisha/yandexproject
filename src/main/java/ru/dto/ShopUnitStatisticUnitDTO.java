package ru.dto;

import lombok.Getter;
import lombok.Setter;
import ru.entity.ShopUnit;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ShopUnitStatisticUnitDTO {
    private String id;
    private String name;
    private String parentId;
    private ShopUnit.ShopUnitType type;
    private Integer price;
    private OffsetDateTime date;
}

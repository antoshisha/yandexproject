package ru.util;

import org.springframework.stereotype.Component;
import ru.dto.ShopUnitImportDTO;
import ru.entity.ShopUnit;

import java.util.Date;

@Component
public class DTOConverter {

    public ShopUnit convertShopUnitImportDTOToShopUnit(ShopUnitImportDTO unitImportDTO) {
        ShopUnit unit = new ShopUnit();
        unit.setId(unitImportDTO.getId());
        unit.setName(unitImportDTO.getName());
        unit.setParentId(unitImportDTO.getParentId());
        unit.setUpdateDate(new Date());
        unit.setType(unitImportDTO.getType());
        unit.setPrice(unitImportDTO.getPrice());
        return unit;
    }
}

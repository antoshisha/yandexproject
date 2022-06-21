package ru.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dto.ShopUnitImportDTO;
import ru.dto.ShopUnitImportRequestDTO;
import ru.entity.ShopUnit;
import ru.exception.ShopUnitVerifyException;
import ru.repository.ShopUnitRepository;
import ru.util.DTOConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShopUnitService {
    @Autowired
    ShopUnitRepository shopUnitRepository;
    @Autowired
    DTOConverter dtoConverter;

    @Transactional
    public void importUnit(ShopUnitImportRequestDTO shopUnitImportRequestDTO) {
        List<ShopUnitImportDTO> shopUnitImportDTOList = shopUnitImportRequestDTO.getItems();
        List<ShopUnit> shopUnitsForUpdate = new ArrayList<>();
        if (!shopUnitImportDTOList.isEmpty()) {
            validateShopUnits(shopUnitImportDTOList);
            for (int i = 0; i < shopUnitImportDTOList.size(); i++) {
                ShopUnit unitForImport = dtoConverter.convertShopUnitImportDTOToShopUnit(shopUnitImportDTOList.get(i));
                Optional<ShopUnit> unit = Optional.ofNullable(shopUnitRepository.findById(unitForImport.getId()));
                if (unit.isPresent()) {
                    if (unit.get().getType() == unitForImport.getType()) {
                        shopUnitsForUpdate.add(unitForImport);
                    } else {
                        System.out.println("some");
                        throw new ShopUnitVerifyException("Validation Failed");
                    }
                } else {
                    shopUnitsForUpdate.add(unitForImport);
                }

            }
            shopUnitRepository.saveAll(shopUnitsForUpdate);
        }

    }

    private static void validateShopUnits(List<ShopUnitImportDTO> shopUnitImportDTOList) {
        for (int i = 0; i < shopUnitImportDTOList.size(); i++) {
            if (!isValidShopUnit(shopUnitImportDTOList.get(i))) {
                throw new ShopUnitVerifyException("Validation Failed");
            }
        }
    }
    private static boolean isValidShopUnit(ShopUnitImportDTO shopUnitImportDTO) {
        boolean result = false;

        String id = shopUnitImportDTO.getId();
        String name = shopUnitImportDTO.getName();
        ShopUnit.ShopUnitType type = shopUnitImportDTO.getType();
        Integer price = shopUnitImportDTO.getPrice();
        System.out.println(id);
        System.out.println(name);
        System.out.println(type);
        System.out.println(price);
        if (type == ShopUnit.ShopUnitType.CATEGORY) {
            if (id != null && name != null && price == null) {
                result = true;
            }
        }
        if (type == ShopUnit.ShopUnitType.OFFER) {
            if (id != null && name != null && price != null) {
                result = true;
            }
        }

        return result;
    }
}

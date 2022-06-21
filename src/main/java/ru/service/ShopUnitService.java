package ru.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dto.ShopUnitImportDTO;
import ru.dto.ShopUnitImportRequestDTO;
import ru.entity.ShopUnit;
import ru.exception.ShopUnitNotFoundException;
import ru.exception.ShopUnitVerifyException;
import ru.repository.ShopUnitRepository;
import ru.util.DTOConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
                        throw new ShopUnitVerifyException("Validation Failed");
                    }
                } else {
                    shopUnitsForUpdate.add(unitForImport);
                }

            }
//            for (ShopUnit x : shopUnitsForUpdate) {
//                if (x.getParentId() != null) {
//                    updateParent(x);
//                }
//            }
            shopUnitRepository.saveAll(shopUnitsForUpdate);
        }

    }

//    private void updateParent(ShopUnit shopUnit) {
//        System.out.println("############updateParent###########");
//        ShopUnit parent = shopUnitRepository.findById(shopUnit.getParentId());
//        if (parent != null) {
//            parent.setUpdateDate(new Date());
//            List<ShopUnit> childrens = parent.getChildren();
//            childrens.add(shopUnit);
//            parent.setChildren(childrens);
//            shopUnitRepository.save(parent);
//            if (parent.getParentId() != null) {
//                updateParent(parent);
//            }
//        }
//
//    }
//    @Transactional
    public void deleteUnit(String id) {
        if (!isValidUuid(id)) {
            throw new ShopUnitVerifyException("Validation Failed");
        }
        ShopUnit shopUnit = shopUnitRepository.findById(id);
        if (shopUnit == null){
            throw new ShopUnitNotFoundException("Item not found");
        }
        shopUnitRepository.delete(shopUnit);
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
        if (type == ShopUnit.ShopUnitType.CATEGORY) {
            if (id != null && name != null && price == null && isValidUuid(id)) {
                result = true;
            }
        }
        if (type == ShopUnit.ShopUnitType.OFFER) {
            if (id != null && name != null && price != null && isValidUuid(id)) {
                result = true;
            }
        }
        return result;
    }

    private static boolean isValidUuid(String id) {
        boolean isValidUuid = Pattern.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", id);
        return isValidUuid;
    }
}

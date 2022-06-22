package ru.service;

import lombok.extern.slf4j.Slf4j;
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

import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ShopUnitService {
    @Autowired
    ShopUnitRepository shopUnitRepository;
    @Autowired
    DTOConverter dtoConverter;

    @Transactional
    public void importUnit(ShopUnitImportRequestDTO shopUnitImportRequestDTO) {
        List<ShopUnitImportDTO> shopUnitImportDTOList = shopUnitImportRequestDTO.getItems();
        if (!shopUnitImportDTOList.isEmpty()) {
            validateShopUnits(shopUnitImportDTOList);
            for (int i = 0; i < shopUnitImportDTOList.size(); i++) {
                ShopUnit unitForImport = dtoConverter.convertShopUnitImportDTOToShopUnit(shopUnitImportDTOList.get(i));
                Optional<ShopUnit> unit = Optional.ofNullable(shopUnitRepository.findById(unitForImport.getId()));
                if (unit.isPresent() && unit.get().getType() != null) {
                    if (unit.get().getType() != unitForImport.getType()) {
                        log.error("ShopUnitVerifyException in import unit");
                        throw new ShopUnitVerifyException("Validation Failed");
                    }
                } else {
                    updateNode(unitForImport);
                }

            }
        }

    }

    @Transactional
    public void deleteUnit(String id) {
        if (!isValidUuid(id)) {
            log.error("ShopUnitVerifyException in delete unit");
            throw new ShopUnitVerifyException("Validation Failed");
        }
        ShopUnit shopUnit = shopUnitRepository.findById(id);
        if (shopUnit == null) {
            log.error("ShopUnitNotFoundException in delete unit; shopUnit = NULL");
            throw new ShopUnitNotFoundException("Item not found");
        }
        shopUnitRepository.delete(shopUnit);
    }

    @Transactional
    public ShopUnit getNode(String id) {
        if (!isValidUuid(id)) {
            throw new ShopUnitVerifyException("Validation Failed");
        }
        ShopUnit shopUnit = shopUnitRepository.findById(id);
        if (shopUnit == null) {
            throw new ShopUnitNotFoundException("Item not found");
        }
        return processNodeForResponse(shopUnit);
    }

    private  void updateNode(ShopUnit unitForImport) {
        if (unitForImport.getParentId() != null) {
            ShopUnit parentShopUnit = shopUnitRepository.findById(unitForImport.getParentId().getId());
            if (parentShopUnit == null) {
                throw new ShopUnitVerifyException("Validation Failed! Parent not found!");
            }
            if (parentShopUnit.getType() != ShopUnit.ShopUnitType.CATEGORY) {
                throw new ShopUnitVerifyException("Validation Failed! Parent of item can be only Category type!");
            }
            List<ShopUnit> childrens = parentShopUnit.getChildren();
            parentShopUnit.setUpdateDate(new Date());
            childrens.add(unitForImport);
            parentShopUnit.setChildren(childrens);
            shopUnitRepository.save(parentShopUnit);
            if (parentShopUnit.getParentId() != null) {
                updateNode(parentShopUnit);
            }
        }
        shopUnitRepository.save(unitForImport);
    }

    private static void validateShopUnits(List<ShopUnitImportDTO> shopUnitImportDTOList) {
        Set<String> idSet = new HashSet<>();
        for (int i = 0; i < shopUnitImportDTOList.size(); i++) {
            if (idSet.contains(shopUnitImportDTOList.get(i).getId())) {
                throw new ShopUnitVerifyException("Validation Failed");
            }
            if (!isValidShopUnit(shopUnitImportDTOList.get(i))) {
                log.error("ShopUnitVerifyException in validateShopUnits unit");
                throw new ShopUnitVerifyException("Validation Failed");
            }
            idSet.add(shopUnitImportDTOList.get(i).getId());
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
            if (id != null && name != null && price != null && price >= 0 && isValidUuid(id)) {
                result = true;
            }
        }
        return result;
    }

    private static boolean isValidUuid(String id) {
        boolean isValidUuid = Pattern.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", id);
        return isValidUuid;
    }

    private ShopUnit processNodeForResponse(ShopUnit shopUnit) {
        if (shopUnit.getChildren().isEmpty()) {
            if (shopUnit.getType() == ShopUnit.ShopUnitType.OFFER) {
                shopUnit.setChildren(null);
            }
        } else {
            shopUnit.getChildren().forEach(this::processNodeForResponse);
        }
        if (shopUnit.getType() == ShopUnit.ShopUnitType.CATEGORY) {
            Integer totalPrice = (int)shopUnit.getChildren().stream().mapToInt(ShopUnit::getPrice).average().getAsDouble();
            shopUnit.setPrice(totalPrice);
        }
        return shopUnit;
    }
}

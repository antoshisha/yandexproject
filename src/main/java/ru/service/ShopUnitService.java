package ru.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dto.ShopUnitImportDTO;
import ru.dto.ShopUnitImportRequestDTO;
import ru.dto.ShopUnitStatisticResponseDTO;
import ru.dto.ShopUnitStatisticUnitDTO;
import ru.entity.LogEventEntity;
import ru.entity.ShopUnit;
import ru.exception.ShopUnitNotFoundException;
import ru.exception.ShopUnitVerifyException;
import ru.repository.ShopUnitRepository;
import ru.util.DTOConverter;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ShopUnitService {
    @Autowired
    ShopUnitRepository shopUnitRepository;
    @Autowired
    DTOConverter dtoConverter;
    @Autowired
    LogEventService logEventService;

    public void importUnits(ShopUnitImportRequestDTO shopUnitImportRequestDTO) {
        List<ShopUnitImportDTO> shopUnitImportDTOList = shopUnitImportRequestDTO.getItems();
        OffsetDateTime dateTime = shopUnitImportRequestDTO.getUpdateDate();
        if (!shopUnitImportDTOList.isEmpty()) {
            validateShopUnits(shopUnitImportDTOList);
            List<ShopUnit> units = dtoConverter.convertShopUnitImportDTOsToShopUnitList(shopUnitImportDTOList, dateTime);
            for (int i = 0; i < units.size(); i++) {
                updateNode(units.get(i), false);
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
        logEventService.deleteLogEventsByShopUnitId(shopUnit.getId());
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
        return shopUnit;
    }

    @Transactional
    public void updateNode(ShopUnit unitForImport, boolean updatePrice) {
        Optional<ShopUnit> unit = Optional.ofNullable(shopUnitRepository.findById(unitForImport.getId()));
        if (unit.isPresent() && unit.get().getType() != null) {
            if (unit.get().getType() != unitForImport.getType()) {
                log.error("ShopUnitVerifyException in import unit");
                throw new ShopUnitVerifyException("Validation Failed");
            }
            if (unit.get().getPrice() != unitForImport.getPrice() && unitForImport.getPrice() != null) {
                logEventService.createLogChangePriceEvent(unitForImport, LogEventEntity.LogEventType.PRICE_UPDATE);
                updatePrice = true;
            }
        }
        if (unitForImport.getParent() != null) {
            ShopUnit parentShopUnit = shopUnitRepository.findById(unitForImport.getParent().getId());
            if (parentShopUnit == null || parentShopUnit.getType() != ShopUnit.ShopUnitType.CATEGORY) {
                throw new ShopUnitVerifyException("Validation Failed!");
            }
            shopUnitRepository.save(unitForImport);
            parentShopUnit.setDate(OffsetDateTime.parse(unitForImport.getDate()));
            if (updatePrice) {
                logEventService.createLogChangePriceEvent(parentShopUnit, LogEventEntity.LogEventType.PRICE_UPDATE);
            }
            shopUnitRepository.save(parentShopUnit);
            if (parentShopUnit.getParent() != null) {
                updateNode(parentShopUnit, updatePrice);
            }
        }
        shopUnitRepository.save(unitForImport);
    }

    public ShopUnitStatisticResponseDTO getUpdatedNodesInThe24Hours(OffsetDateTime dateTime) {
        List<LogEventEntity> logEventInThe24Hours = logEventService.getLogEventsUpdatedInThe24Hours(dateTime);
        List<ShopUnitStatisticUnitDTO> statUnits = dtoConverter.convertLogEventsListToStatisticUnitDTOs(logEventInThe24Hours);
        ShopUnitStatisticResponseDTO responseDTO = new ShopUnitStatisticResponseDTO();
        responseDTO.setItems(statUnits);
        return responseDTO;
    }

    public ShopUnitStatisticResponseDTO getStatisticForNodeByPeriod(String shopUnitId, OffsetDateTime start, OffsetDateTime end) {
        List<LogEventEntity> events = logEventService.getLogEventsForShopUnitBetween(shopUnitId, start, end);
        List<ShopUnitStatisticUnitDTO> statUnits = dtoConverter.convertLogEventsListToStatisticUnitDTOs(events);
        ShopUnitStatisticResponseDTO responseDTO = new ShopUnitStatisticResponseDTO();
        responseDTO.setItems(statUnits);
        return responseDTO;
    }

    public ShopUnitStatisticResponseDTO getAllStatisticForNode(String shopUnitId) {
        List<LogEventEntity> events = logEventService.getAllLogEventsForNode(shopUnitId);
        List<ShopUnitStatisticUnitDTO> statUnits = dtoConverter.convertLogEventsListToStatisticUnitDTOs(events);
        ShopUnitStatisticResponseDTO responseDTO = new ShopUnitStatisticResponseDTO();
        responseDTO.setItems(statUnits);
        return responseDTO;
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

}

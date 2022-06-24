package ru.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.dto.ShopUnitImportDTO;
import ru.dto.ShopUnitStatisticUnitDTO;
import ru.entity.LogEventEntity;
import ru.entity.ShopUnit;
import ru.repository.ShopUnitRepository;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOConverter {

    @Autowired
    ShopUnitRepository shopUnitRepository;

    public ShopUnit convertShopUnitImportDTOToShopUnit(ShopUnitImportDTO unitImportDTO, OffsetDateTime dateTime) {
        ShopUnit unit = new ShopUnit();
        unit.setId(unitImportDTO.getId());
        unit.setName(unitImportDTO.getName());
        unit.setParentId(unitImportDTO.getParentId());
        unit.setUpdateDate(dateTime);
        unit.setType(unitImportDTO.getType());
        unit.setPrice(unitImportDTO.getPrice());
        return unit;
    }

    public List<ShopUnit> convertShopUnitImportDTOsToShopUnitList(List<ShopUnitImportDTO> shopUnitImportDTOList, OffsetDateTime dateTime) {
        List<ShopUnit> shopUnits = new ArrayList<>();
        shopUnitImportDTOList.forEach(x -> shopUnits.add(convertShopUnitImportDTOToShopUnit(x, dateTime)));
        return shopUnits;
    }

    public List<ShopUnitStatisticUnitDTO> convertLogEventsListToStatisticUnitDTOs(List<LogEventEntity> logEventsList){
        return logEventsList.stream().map(this::convertLogEventsToShopUnitStatisticUnitDTO).toList();
    }

    public ShopUnitStatisticUnitDTO convertLogEventsToShopUnitStatisticUnitDTO(LogEventEntity logEvent) {
        ShopUnitStatisticUnitDTO statisticUnitDTO = new ShopUnitStatisticUnitDTO();
        statisticUnitDTO.setId(logEvent.getShopUnitId());
        statisticUnitDTO.setName(logEvent.getName());
        statisticUnitDTO.setParentId(logEvent.getParentId());
        statisticUnitDTO.setType(logEvent.getType());
        statisticUnitDTO.setPrice(logEvent.getPrice());
        statisticUnitDTO.setDate(logEvent.getCreationDate());
        return statisticUnitDTO;
    }
}

package ru.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dto.ShopUnitImportRequestDTO;
import ru.dto.ShopUnitStatisticResponseDTO;
import ru.entity.LogEventEntity;
import ru.entity.ShopUnit;
import ru.exception.ShopUnitVerifyException;
import ru.repository.LogEventRepository;
import ru.service.LogEventService;
import ru.service.ShopUnitService;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.List;


@RestController
public class ShopUnitController {

    @Autowired
    ShopUnitService shopUnitService;
    @Autowired
    LogEventRepository logEventRepository;

    @PostMapping("/imports")
    public ResponseEntity importUnit(@RequestBody ShopUnitImportRequestDTO shopUnitImportRequestDTO) {
        shopUnitService.importUnits(shopUnitImportRequestDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUnit(@PathVariable String id) {
        shopUnitService.deleteUnit(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<ShopUnit> getNodes(@PathVariable String id) {
        ShopUnit node = shopUnitService.getNode(id);
        return new ResponseEntity<>(node, HttpStatus.OK);
    }

    @GetMapping("/node/{id}/statistic")
    public ResponseEntity<ShopUnitStatisticResponseDTO> getStatistics(@PathVariable String id,
                                                               @RequestParam(required = false) String dateStart,
                                                               @RequestParam(required = false) String dateEnd) throws ParseException {
        ShopUnitStatisticResponseDTO responseDTO;
        if (dateStart != null && dateEnd != null){
            OffsetDateTime start = OffsetDateTime.parse(dateStart);
            OffsetDateTime end = OffsetDateTime.parse(dateEnd);
            responseDTO = shopUnitService.getStatisticForNodeByPeriod(id, start, end);
        } else {
            responseDTO = shopUnitService.getAllStatisticForNode(id);
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/sales")
    public ResponseEntity<ShopUnitStatisticResponseDTO> getUpdatedNodesInThe24Hours(@RequestParam String date) {
        if (date != null) {
            OffsetDateTime dateTime = OffsetDateTime.parse(date);
            ShopUnitStatisticResponseDTO responseDTO = shopUnitService.getUpdatedNodesInThe24Hours(dateTime);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } else {
            throw new ShopUnitVerifyException("Validation Failed!");
        }
    }

    @GetMapping("/allStat")
    public List<LogEventEntity> getAllStat(){
        return logEventRepository.findAll();
    }
}

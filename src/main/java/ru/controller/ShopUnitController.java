package ru.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dto.ShopUnitImportRequestDTO;
import ru.entity.LogEventEntity;
import ru.entity.ShopUnit;
import ru.repository.LogEventRepository;
import ru.service.LogEventService;
import ru.service.ShopUnitService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
public class ShopUnitController {

    @Autowired
    ShopUnitService shopUnitService;
    @Autowired
    LogEventService logEventService;
    @Autowired
    LogEventRepository logEventRepository;

    @PostMapping("/imports")
    public ResponseEntity importUnit(@RequestBody ShopUnitImportRequestDTO shopUnitImportRequestDTO) {
//        shopUnitImportRequestDTO.getItems().forEach(System.out::println);
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
    public ResponseEntity< List<LogEventEntity>> getStatistics(@PathVariable String id,
                                                        @RequestParam String dateStart,
                                                        @RequestParam String dateEnd) throws ParseException {
        List<LogEventEntity> logEventEntityList;
        OffsetDateTime start = OffsetDateTime.parse(dateStart);
        OffsetDateTime end = OffsetDateTime.parse(dateEnd);
        logEventEntityList = logEventService.getLogEventsForShopUnitBetween(id, new Date(start.toInstant().toEpochMilli()), new Date(end.toInstant().toEpochMilli()));
        return new ResponseEntity<>(logEventEntityList, HttpStatus.OK);
    }

    @GetMapping("/allStat")
    public List<LogEventEntity> getAllStat(){
        return logEventRepository.findAll();
    }
}

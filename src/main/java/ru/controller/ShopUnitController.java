package ru.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dto.ShopUnitImportRequestDTO;
import ru.entity.ShopUnit;
import ru.repository.ShopUnitRepository;
import ru.service.ShopUnitService;

@RestController
public class ShopUnitController {

    @Autowired
    ShopUnitService shopUnitService;

    @Autowired
    ShopUnitRepository shopUnitRepository;

    @PostMapping("/imports")
    public ResponseEntity importUnit(@RequestBody ShopUnitImportRequestDTO shopUnitImportRequestDTO) {
        shopUnitImportRequestDTO.getItems().forEach(System.out::println);
        shopUnitService.importUnit(shopUnitImportRequestDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUnit(@PathVariable String id) {
        shopUnitService.deleteUnit(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/test/{id}")
    public ShopUnit get(@PathVariable String id) {
        return shopUnitRepository.findById(id);
    }
}

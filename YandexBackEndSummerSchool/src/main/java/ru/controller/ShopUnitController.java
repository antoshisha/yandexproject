package ru.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.dto.ShopUnitImportRequestDTO;
import ru.service.ShopUnitService;

@RestController
public class ShopUnitController {

    @Autowired
    ShopUnitService shopUnitService;

    @PostMapping("/imports")
    public ResponseEntity importUnit(@RequestBody ShopUnitImportRequestDTO shopUnitImportRequestDTO) {
        System.out.println(shopUnitImportRequestDTO);
        shopUnitService.importUnit(shopUnitImportRequestDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}

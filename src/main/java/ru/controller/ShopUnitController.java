package ru.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dto.ShopUnitImportRequestDTO;
import ru.entity.ShopUnit;
import ru.service.ShopUnitService;


@RestController
public class ShopUnitController {

    @Autowired
    ShopUnitService shopUnitService;

    @PostMapping("/imports")
    public ResponseEntity importUnit(@RequestBody ShopUnitImportRequestDTO shopUnitImportRequestDTO) {
//        shopUnitImportRequestDTO.getItems().forEach(System.out::println);
        shopUnitService.importUnit(shopUnitImportRequestDTO);
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

//    @GetMapping("/sales")
//    public ResponseE

//    public ShopUnit setChildrenToNull(ShopUnit shopUnit) {
//        if (shopUnit.getChildren().isEmpty()) {
//            if (shopUnit.getType() == ShopUnit.ShopUnitType.OFFER) {
//                shopUnit.setChildren(null);
//            }
//        } else {
//            shopUnit.getChildren().forEach(this::setChildrenToNull);
//        }
//        if (shopUnit.getType() == ShopUnit.ShopUnitType.CATEGORY) {
//            Integer totalPrice = (int)shopUnit.getChildren().stream().mapToInt(ShopUnit::getPrice).average().getAsDouble();
//            shopUnit.setPrice(totalPrice);
//        }
//        return shopUnit;
//    }
}

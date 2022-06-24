package ru.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShopUnitStatisticResponseDTO {
    private List<ShopUnitStatisticUnitDTO> items;
}

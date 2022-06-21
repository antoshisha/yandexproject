package ru.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class ShopUnitImportRequestDTO {
    private List<ShopUnitImportDTO> items;
    private Date updateDate;
}

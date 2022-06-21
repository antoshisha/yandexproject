package ru.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "shop_unit")
public class ShopUnit {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "lastUpdated")
    private Date lastUpdated;
    @Column(name = "parentUid")
    private String parentUid;
    @Column(name = "type")
    private ShopUnitType type;
    @Column(name = "price")
    private Integer price;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentUid")
    private List<ShopUnit> children;


    public enum ShopUnitType {
        OFFER, CATEGORY;
    }

}

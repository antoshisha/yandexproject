package ru.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Getter
@Data
@Entity
@ToString
@Table(name = "shop_unit")
//@DynamicUpdate
public class ShopUnit {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastUpdated")
    private Date updateDate;

//    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "FK_PARENT_ID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ShopUnit parentId;

//    private String parentId;

    @Column(name = "type")
    private ShopUnitType type;

    @Column(name = "price")
    private Integer price;

//    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentId")
//    @JoinColumn(name = "parentUid")
//    private List<ShopUnit> children = type == ShopUnitType.OFFER ? null : new ArrayList<>();
    private List<ShopUnit> children = new ArrayList<>();


    public ShopUnit() {
    }

    public ShopUnit(String id) {
        this.id = id;
    }
//
//    public ShopUnit(String id, String name, Date updateDate, ShopUnit parentId, ShopUnitType type, Integer price, List<ShopUnit> children) {
//        this.id = id;
//        this.name = name;
//        this.updateDate = updateDate;
//        this.parentId = parentId;
//        this.type = type;
//        this.price = price;
//        this.children = children;
//    }

    public enum ShopUnitType {
        OFFER, CATEGORY;
    }

}

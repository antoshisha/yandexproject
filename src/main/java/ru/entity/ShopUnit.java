package ru.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Getter
//@Data
@Entity
//@ToString
@Table(name = "shop_unit")
//@DynamicUpdate
public class ShopUnit {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastUpdated")
    private OffsetDateTime updateDate;

    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.PERSIST})
//    @ManyToOne()
    @JoinColumn(name = "FK_PARENT_ID")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ShopUnit parentId;

//    private String parentId;

    @Column(name = "type")
    private ShopUnitType type;

    @Column(name = "price")
    private Integer price;

    @JsonManagedReference
//    @Fetch(FetchMode.JOIN)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentId", orphanRemoval = true)
//    @JoinColumn(name = "parentUid")
//    private List<ShopUnit> children = type == ShopUnitType.OFFER ? null : new ArrayList<>();
    private List<ShopUnit> children = new ArrayList<>();


    public ShopUnit() {
    }

    public ShopUnit(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "ShopUnit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", updateDate=" + updateDate +
                ", type=" + type +
                ", price=" + price +
                ", children=" + children +
                '}';
    }

    public enum ShopUnitType {
        OFFER, CATEGORY;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public ShopUnit getParentId() {
        return parentId;
    }

    public void setParentId(ShopUnit parentId) {
        this.parentId = parentId;
    }

    public ShopUnitType getType() {
        return type;
    }

    public void setType(ShopUnitType type) {
        this.type = type;
    }

    public Integer getPrice() {
        if (type == ShopUnitType.CATEGORY && !children.isEmpty()) {
            List<Integer> priceList = new ArrayList<>();
            for (ShopUnit unit : children) {
                if (unit.getPrice() != null) priceList.add(unit.getPrice());
            }
            return (int) priceList.stream().mapToInt(x -> x).average().getAsDouble();
        }
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<ShopUnit> getChildren() {
        if (type == ShopUnitType.OFFER) {
            return null;
        }
        return children;
    }

    public void setChildren(List<ShopUnit> children) {
        this.children = children;
    }
}

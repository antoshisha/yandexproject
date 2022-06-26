package ru.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shop_unit")
public class ShopUnit {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastUpdated")
    private OffsetDateTime date;

    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.PERSIST})
//    @ManyToOne()
    @JoinColumn(name = "FK_PARENT_ID")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ShopUnit parent;
    @Transient
    private String parentId;

    @Column(name = "type")
    private ShopUnitType type;

    @Column(name = "price")
    private Integer price;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", orphanRemoval = true)
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
                ", updateDate=" + date +
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

    public String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        return date.withOffsetSameInstant(ZoneOffset.UTC).format(dtf);
    }

    public void setDate(OffsetDateTime updateDate) {
        this.date = updateDate;
    }

    public ShopUnit getParent() {
        return parent;
    }

    public void setParent(ShopUnit parentId) {
        this.parent = parentId;
    }

    public String getParentId() {
        if (parent != null && parent.getId() != null) {
            return parent.getId();
        } else {
            return null;
        }
    }

    public void setParentId(String parentId) {
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
            List<Integer> priceList = getPricesForTree(this);
            if (!priceList.isEmpty()) {
                return (int) priceList.stream().mapToInt(x -> x).average().getAsDouble();
            }
        }
        return price;
    }

    private List<Integer> getPricesForTree(ShopUnit shopUnit) {
        List<Integer> priceList = new ArrayList<>();
        for (ShopUnit unit : shopUnit.getChildren()) {
            if (unit.getType() == ShopUnitType.OFFER && unit.getPrice() != null){
                priceList.add(unit.getPrice());
            } else if (unit.getType() == ShopUnitType.CATEGORY && unit.getChildren() != null && !unit.getChildren().isEmpty()){
                priceList.addAll(getPricesForTree(unit));
            }
        }
        return priceList;
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

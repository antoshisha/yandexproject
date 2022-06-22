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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Getter
@Data
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
    private Date updateDate;

    //    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "FK_PARENT_ID")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ShopUnit parentId;

//    private String parentId;

    @Column(name = "type")
    private ShopUnitType type;

    @Column(name = "price")
    private Integer price;

    //    @JsonManagedReference
//    @Fetch(FetchMode.JOIN)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentId")
//    @JoinColumn(name = "parentUid")
//    private List<ShopUnit> children = type == ShopUnitType.OFFER ? null : new ArrayList<>();
    private List<ShopUnit> children = new ArrayList<>();


    public ShopUnit() {
    }

    public ShopUnit(String id) {
        this.id = id;
    }

    @PrePersist
    @PreUpdate
    public void processAveragePrice(){
        if (this.getType() == ShopUnitType.CATEGORY) {
            List<Integer> prices = new ArrayList<>();
            System.out.println("CATEGORY: " + this.getName());
            if (this.getChildren() != null && !this.getChildren().isEmpty()){
                for (ShopUnit x :  this.getChildren()) {
                    if (x.getPrice() != null) {
                        prices.add(x.getPrice());
                    }
                }
            }
            int sum = 0;
            for (Integer i: prices) {
                sum+=i;
            }
            if (!prices.isEmpty()){
                int avg = (int) prices.stream().mapToInt(x -> x).average().orElse(0);
                this.setPrice(avg);
            }

        } else {
            this.setChildren(null);
        }
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

}

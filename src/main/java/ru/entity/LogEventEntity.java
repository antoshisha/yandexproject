package ru.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Date;

@Data
@Entity
@ToString
@Table(name = "event_log")
public class LogEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String shopUnitId;
    private String name;
    private String parentId;
    private ShopUnit.ShopUnitType type;
    private Integer price;
    private LogEventType logEventType;
    private OffsetDateTime creationDate;


    public enum LogEventType {
        PRICE_UPDATE, NAME_UPDATE
    }
}

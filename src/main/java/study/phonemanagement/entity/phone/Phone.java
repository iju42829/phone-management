package study.phonemanagement.entity.phone;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import study.phonemanagement.entity.BaseEntity;
import study.phonemanagement.exception.phone.PhoneStockShortageException;

import java.time.LocalDateTime;

import static study.phonemanagement.common.ErrorCode.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Phone extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Manufacturer manufacturer;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Storage storage;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String color;

    private LocalDateTime deletedAt;

    @Version
    private Long version;

    @Builder
    private Phone(String name, Manufacturer manufacturer, Storage storage, Status status, Integer price, Integer quantity, String color) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.storage = storage;
        this.status = status;
        this.price = price;
        this.quantity = quantity;
        this.color = color;
    }

    public void update(String name, Manufacturer manufacturer, Storage storage, Status status, Integer price, Integer quantity, String color) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.storage = storage;
        this.status = status;
        this.price = price;
        this.quantity = quantity;
        this.color = color;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void reduceQuantity(Integer quantity) {
        int updatedQuantity = this.quantity - quantity;

        if (updatedQuantity < 0) {
            throw new PhoneStockShortageException(PHONE_STOCK_SHORTAGE);
        }

        this.quantity = updatedQuantity;

        if (updatedQuantity == 0) {
            this.status = Status.OUT_OF_STOCK;
        }
    }

    public void addQuantity(Integer quantity) {
        this.quantity += quantity;
    }
}

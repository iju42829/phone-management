package study.phonemanagement.service.phone.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import study.phonemanagement.entity.phone.Manufacturer;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
@AllArgsConstructor
public class PhoneResponse {
    private Long id;

    private String name;

    private Manufacturer manufacturer;

    private String storage;

    private String status;

    private Integer price;

    private Integer quantity;

    private String color;

    private LocalDateTime createdDate;
}

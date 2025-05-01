package study.phonemanagement.service.phone.response;

import lombok.*;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Status;
import study.phonemanagement.entity.phone.Storage;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailPhoneResponse {
    private Long id;

    private String name;

    private Manufacturer manufacturer;

    private Storage storage;

    private Status status;

    private Integer price;

    private Integer quantity;

    private String color;

    private String createdBy;

    private String lastModifiedBy;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}

package study.phonemanagement.service.phone.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Status;
import study.phonemanagement.entity.phone.Storage;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
@AllArgsConstructor
public class ListPhoneResponse {
    private Long id;

    private String name;

    private Manufacturer manufacturer;

    private Storage storage;

    private Status status;

    private Integer price;

    private Integer quantity;

    private String color;

    private LocalDateTime createdDate;
}

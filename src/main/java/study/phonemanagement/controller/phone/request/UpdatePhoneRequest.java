package study.phonemanagement.controller.phone.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Status;
import study.phonemanagement.entity.phone.Storage;

@Getter @Setter
public class UpdatePhoneRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Manufacturer manufacturer;

    @NotNull
    private Storage storage;

    @NotNull
    private Status status;

    @Positive
    private Integer price;

    @Positive
    private Integer quantity;

    @NotBlank
    private String color;
}

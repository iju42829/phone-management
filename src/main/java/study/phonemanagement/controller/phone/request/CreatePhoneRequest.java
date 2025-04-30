package study.phonemanagement.controller.phone.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Status;
import study.phonemanagement.entity.phone.Storage;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePhoneRequest {
    @NotBlank
    private String name;

    @NotNull
    private Manufacturer manufacturer;

    @NotNull
    private Storage storage;

    @NotNull
    private Status status;

    @Positive
    @NotNull
    private Integer price;

    @Positive
    @NotNull
    private Integer quantity;

    @NotBlank
    private String color;
}

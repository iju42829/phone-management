package study.phonemanagement.controller.order.request;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateOrderDeliveryRequest {
    private String city;
    private String street;
    private String zipcode;
    private String detail;

    public static CreateOrderDeliveryRequest createOrderDeliveryRequest(String city, String street, String zipcode, String detail) {
        return CreateOrderDeliveryRequest.builder()
                .city(city)
                .street(street)
                .zipcode(zipcode)
                .detail(detail)
                .build();
    }
}

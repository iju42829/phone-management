package study.phonemanagement.service.cart.response;

import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartResponse {
    private Long cartId;
    private Long phoneId;
    private String name;
    private String manufacturer;
    private String storage;
    private int price;
    private int count;
}

package study.phonemanagement.service.user.response;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressResponse {
    private String city;
    private String street;
    private String zipcode;
    private String detail;
}

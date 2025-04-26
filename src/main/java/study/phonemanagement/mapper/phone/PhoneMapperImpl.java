package study.phonemanagement.mapper.phone;

import org.springframework.stereotype.Component;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.service.phone.response.PhoneResponse;

@Component
public class PhoneMapperImpl implements PhoneMapper {

    @Override
    public PhoneResponse toPhoneResponse(Phone phone) {
        if (phone == null) {
            return null;
        }

        return PhoneResponse.builder()
                .id(phone.getId())
                .name(phone.getName())
                .manufacturer(phone.getManufacturer())
                .storage(phone.getStorage().getDescription())
                .status(phone.getStatus().getDescription())
                .price(phone.getPrice())
                .quantity(phone.getQuantity())
                .color(phone.getColor())
                .build();
    }
}

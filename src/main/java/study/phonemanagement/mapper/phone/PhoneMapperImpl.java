package study.phonemanagement.mapper.phone;

import org.springframework.stereotype.Component;
import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
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
                .storage(phone.getStorage())
                .status(phone.getStatus())
                .price(phone.getPrice())
                .quantity(phone.getQuantity())
                .color(phone.getColor())
                .createdDate(phone.getCreatedDate())
                .build();
    }

    @Override
    public Phone toPhone(CreatePhoneRequest createPhoneRequest) {
        if (createPhoneRequest == null) {
            return null;
        }

        return Phone.builder()
                .name(createPhoneRequest.getName())
                .manufacturer(createPhoneRequest.getManufacturer())
                .storage(createPhoneRequest.getStorage())
                .status(createPhoneRequest.getStatus())
                .price(createPhoneRequest.getPrice())
                .quantity(createPhoneRequest.getQuantity())
                .color(createPhoneRequest.getColor())
                .build();
    }
}

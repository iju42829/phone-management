package study.phonemanagement.mapper.phone;

import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.service.phone.response.PhoneResponse;

public interface PhoneMapper {
    PhoneResponse toPhoneResponse(Phone phone);
    Phone toPhone(CreatePhoneRequest createPhoneRequest);
}

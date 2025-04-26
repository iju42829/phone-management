package study.phonemanagement.mapper.phone;

import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.service.phone.response.PhoneResponse;

public interface PhoneMapper {
    PhoneResponse toPhoneResponse(Phone phone);
}

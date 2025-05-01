package study.phonemanagement.mapper.phone;

import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.service.phone.response.DetailPhoneResponse;
import study.phonemanagement.service.phone.response.ListPhoneResponse;
import study.phonemanagement.service.phone.response.UpdatePhoneResponse;

public interface PhoneMapper {
    ListPhoneResponse toPhoneListResponse(Phone phone);
    UpdatePhoneResponse toUpdatePhoneResponse(Phone phone);
    DetailPhoneResponse toDetailPhoneResponse(Phone phone);
    Phone toPhone(CreatePhoneRequest createPhoneRequest);
}

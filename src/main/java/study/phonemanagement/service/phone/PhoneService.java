package study.phonemanagement.service.phone;

import org.springframework.data.domain.Page;
import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.service.phone.response.PhoneResponse;

public interface PhoneService {
    Page<PhoneResponse> getAllPhones(String searchWord, Manufacturer manufacturer, Integer pageNumber, Integer pageSize);

    Long createPhone(CreatePhoneRequest createPhoneRequest);

    void deletePhone(Long phoneId);

    PhoneResponse getPhone(Long phoneId);
}

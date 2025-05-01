package study.phonemanagement.service.phone;

import org.springframework.data.domain.Page;
import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.controller.phone.request.UpdatePhoneRequest;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.service.phone.response.DetailPhoneResponse;
import study.phonemanagement.service.phone.response.ListPhoneResponse;
import study.phonemanagement.service.phone.response.UpdatePhoneResponse;

public interface PhoneService {
    Page<ListPhoneResponse> getAllPhones(String searchWord, Manufacturer manufacturer, Integer pageNumber, Integer pageSize);

    Long createPhone(CreatePhoneRequest createPhoneRequest);

    void deletePhone(Long phoneId);

    UpdatePhoneResponse getPhoneForUpdate(Long phoneId);

    void update(Long phoneId, UpdatePhoneRequest updatePhoneRequest);

    DetailPhoneResponse getPhoneForDetail(Long phoneId);
}

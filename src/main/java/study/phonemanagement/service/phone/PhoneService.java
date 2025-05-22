package study.phonemanagement.service.phone;

import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.controller.phone.request.UpdatePhoneRequest;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Status;
import study.phonemanagement.service.phone.response.CachedListPhoneResponse;
import study.phonemanagement.service.phone.response.DetailPhoneResponse;
import study.phonemanagement.service.phone.response.UpdatePhoneResponse;

public interface PhoneService {
    CachedListPhoneResponse getAllPhones(Status status, String searchWord, Manufacturer manufacturer, Integer pageNumber, Integer pageSize);

    Long createPhone(CreatePhoneRequest createPhoneRequest);

    void deletePhone(Long phoneId);

    UpdatePhoneResponse getPhoneForUpdate(Long phoneId);

    void update(Long phoneId, UpdatePhoneRequest updatePhoneRequest);

    DetailPhoneResponse getPhoneForDetail(Long phoneId);
}

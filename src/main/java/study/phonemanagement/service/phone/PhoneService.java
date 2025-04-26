package study.phonemanagement.service.phone;

import study.phonemanagement.service.phone.response.PhoneResponse;

import java.util.List;

public interface PhoneService {
    List<PhoneResponse> getAllPhones();
}

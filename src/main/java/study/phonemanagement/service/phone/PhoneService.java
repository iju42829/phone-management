package study.phonemanagement.service.phone;

import org.springframework.data.domain.Page;
import study.phonemanagement.service.phone.response.PhoneResponse;

public interface PhoneService {
    Page<PhoneResponse> getAllPhones(Integer pageNumber, Integer pageSize);
}

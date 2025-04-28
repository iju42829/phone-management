package study.phonemanagement.repository.phone;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;

public interface PhoneRepositoryCustom {
    Page<Phone> findAllPhone(String searchWord, Manufacturer manufacturer, Pageable pageable);
}

package study.phonemanagement.service.phone;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.mapper.phone.PhoneMapper;
import study.phonemanagement.repository.phone.PhoneRepository;
import study.phonemanagement.service.phone.response.PhoneResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;

    @Override
    public Page<PhoneResponse> getAllPhones(String searchWord, Manufacturer manufacturer, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "createdDate")
        );

        return phoneRepository.findAllPhone(searchWord, manufacturer, pageable)
                .map(phoneMapper::toPhoneResponse);
    }
}

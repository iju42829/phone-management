package study.phonemanagement.service.phone;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.exception.phone.PhoneNotFoundException;
import study.phonemanagement.mapper.phone.PhoneMapper;
import study.phonemanagement.repository.phone.PhoneRepository;
import study.phonemanagement.service.phone.response.PhoneResponse;

import static study.phonemanagement.common.ErrorCode.*;

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

    @Override
    public Long createPhone(CreatePhoneRequest createPhoneRequest) {
        Phone phone = phoneMapper.toPhone(createPhoneRequest);

        phoneRepository.save(phone);

        return phone.getId();
    }

    @Override
    public void deletePhone(Long phoneId) {
        Phone phone = phoneRepository.findById(phoneId).orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND.getMessage()));

        phone.delete();
    }

    @Override
    public PhoneResponse getPhone(Long phoneId) {
        return phoneRepository.findById(phoneId)
                .map(phoneMapper::toPhoneResponse)
                .orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND.getMessage()));
    }
}

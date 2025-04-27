package study.phonemanagement.service.phone;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import study.phonemanagement.mapper.phone.PhoneMapper;
import study.phonemanagement.repository.PhoneRepository;
import study.phonemanagement.service.phone.response.PhoneResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;

    @Override
    public Page<PhoneResponse> getAllPhones(String searchWord, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "createdDate")
        );

        if (searchWord != null) {
            return phoneRepository.findAllByNameStartingWith(searchWord, pageable)
                    .map(phoneMapper::toPhoneResponse);
        }

        return phoneRepository.findAll(pageable)
                .map(phoneMapper::toPhoneResponse);
    }
}

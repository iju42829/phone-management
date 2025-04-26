package study.phonemanagement.service.phone;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.phonemanagement.mapper.phone.PhoneMapper;
import study.phonemanagement.repository.PhoneRepository;
import study.phonemanagement.service.phone.response.PhoneResponse;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;

    @Override
    public List<PhoneResponse> getAllPhones() {
        return phoneRepository.findAll().stream()
                .map(phoneMapper::toPhoneResponse)
                .toList();
    }
}

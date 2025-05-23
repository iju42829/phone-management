package study.phonemanagement.service.phone;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.controller.phone.request.UpdatePhoneRequest;
import study.phonemanagement.entity.cart.Cart;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.phone.Status;
import study.phonemanagement.exception.phone.PhoneNotFoundException;
import study.phonemanagement.mapper.phone.PhoneMapper;
import study.phonemanagement.repository.order.CartRepository;
import study.phonemanagement.repository.phone.PhoneRepository;
import study.phonemanagement.service.phone.response.CachedListPhoneResponse;
import study.phonemanagement.service.phone.response.DetailPhoneResponse;
import study.phonemanagement.service.phone.response.ListPhoneResponse;
import study.phonemanagement.service.phone.response.UpdatePhoneResponse;

import java.util.List;

import static study.phonemanagement.common.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;
    private final CartRepository cartRepository;
    private final PhoneMapper phoneMapper;

    @Override
    @Cacheable(cacheNames = "getPhones",
            key = "'phones:search:' + (#searchWord != null ? #searchWord : '')" +
                    " + ':manufacturer:' + (#manufacturer != null ? #manufacturer.name() : '')" +
                    " + ':page:' + #pageNumber + ':size:' + #pageSize",
            cacheManager = "cacheManager")
    public CachedListPhoneResponse getAllPhones(Status status, String searchWord, Manufacturer manufacturer, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "createdDate")
        );

        Page<ListPhoneResponse> page = phoneRepository.findAllPhone(status, searchWord, manufacturer, pageable)
                .map(phoneMapper::toPhoneListResponse);

        return phoneMapper.toCachedListPhoneResponse(page) ;
    }

    @Override
    @Cacheable(cacheNames = "getAdminPhones",
            key = "'phones:search:' + (#searchWord != null ? #searchWord : '')" +
                    " + ':manufacturer:' + (#manufacturer != null ? #manufacturer.name() : '')" +
                    " + ':page:' + #pageNumber + ':size:' + #pageSize",
            cacheManager = "cacheManager")
    public CachedListPhoneResponse getAllPhones(String searchWord, Manufacturer manufacturer, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "createdDate")
        );

        Page<ListPhoneResponse> page = phoneRepository.findAllPhone(null, searchWord, manufacturer, pageable)
                .map(phoneMapper::toPhoneListResponse);

        return phoneMapper.toCachedListPhoneResponse(page) ;
    }

    @Override
    public Long createPhone(CreatePhoneRequest createPhoneRequest) {
        Phone phone = phoneMapper.toPhone(createPhoneRequest);

        phoneRepository.save(phone);

        return phone.getId();
    }

    @Override
    public void deletePhone(Long phoneId) {
        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND));

        List<Cart> cart = cartRepository.findAllByPhone(phone);
        cartRepository.deleteAll(cart);

        phone.delete();
    }

    @Override
    public UpdatePhoneResponse getPhoneForUpdate(Long phoneId) {
        return phoneRepository.findByIdAndDeletedAtIsNull(phoneId)
                .map(phoneMapper::toUpdatePhoneResponse)
                .orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND));
    }

    @Override
    public void update(Long phoneId, UpdatePhoneRequest updatePhoneRequest) {
        Phone phone = phoneRepository
                .findById(phoneId)
                .orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND));

        phone.update(
                updatePhoneRequest.getName(),
                updatePhoneRequest.getManufacturer(),
                updatePhoneRequest.getStorage(),
                updatePhoneRequest.getStatus(),
                updatePhoneRequest.getPrice(),
                updatePhoneRequest.getQuantity(),
                updatePhoneRequest.getColor()
        );
    }

    @Override
    public DetailPhoneResponse getPhoneForDetail(Long phoneId) {
        return phoneRepository.findByIdAndDeletedAtIsNull(phoneId)
                .map(phoneMapper::toDetailPhoneResponse)
                .orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND));
    }
}

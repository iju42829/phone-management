package study.phonemanagement.mapper.phone;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.service.phone.response.CachedListPhoneResponse;
import study.phonemanagement.service.phone.response.DetailPhoneResponse;
import study.phonemanagement.service.phone.response.ListPhoneResponse;
import study.phonemanagement.service.phone.response.UpdatePhoneResponse;

@Component
public class PhoneMapperImpl implements PhoneMapper {

    @Override
    public ListPhoneResponse toPhoneListResponse(Phone phone) {
        if (phone == null) {
            return null;
        }

        return ListPhoneResponse.builder()
                .id(phone.getId())
                .name(phone.getName())
                .manufacturer(phone.getManufacturer())
                .storage(phone.getStorage())
                .status(phone.getStatus())
                .price(phone.getPrice())
                .quantity(phone.getQuantity())
                .color(phone.getColor())
                .createdDate(phone.getCreatedDate())
                .build();
    }

    @Override
    public UpdatePhoneResponse toUpdatePhoneResponse(Phone phone) {
        if (phone == null) {
            return null;
        }

        return UpdatePhoneResponse.builder()
                .name(phone.getName())
                .manufacturer(phone.getManufacturer())
                .storage(phone.getStorage())
                .status(phone.getStatus())
                .price(phone.getPrice())
                .quantity(phone.getQuantity())
                .color(phone.getColor())
                .build();
    }

    @Override
    public DetailPhoneResponse toDetailPhoneResponse(Phone phone) {
        if (phone == null) {
            return null;
        }

        return DetailPhoneResponse.builder()
                .id(phone.getId())
                .name(phone.getName())
                .manufacturer(phone.getManufacturer())
                .storage(phone.getStorage())
                .status(phone.getStatus())
                .price(phone.getPrice())
                .quantity(phone.getQuantity())
                .color(phone.getColor())
                .createdBy(phone.getCreatedBy())
                .lastModifiedBy(phone.getLastModifiedBy())
                .createdDate(phone.getCreatedDate())
                .lastModifiedDate(phone.getLastModifiedDate())
                .build();
    }

    @Override
    public Phone toPhone(CreatePhoneRequest createPhoneRequest) {
        if (createPhoneRequest == null) {
            return null;
        }

        return Phone.builder()
                .name(createPhoneRequest.getName())
                .manufacturer(createPhoneRequest.getManufacturer())
                .storage(createPhoneRequest.getStorage())
                .status(createPhoneRequest.getStatus())
                .price(createPhoneRequest.getPrice())
                .quantity(createPhoneRequest.getQuantity())
                .color(createPhoneRequest.getColor())
                .build();
    }

    @Override
    public CachedListPhoneResponse toCachedListPhoneResponse(Page<ListPhoneResponse> page) {
        if (page == null) {
            return null;
        }

        return CachedListPhoneResponse.builder()
                .content(page.getContent())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .build();
    }
}

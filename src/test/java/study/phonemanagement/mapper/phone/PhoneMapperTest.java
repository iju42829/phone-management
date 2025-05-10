package study.phonemanagement.mapper.phone;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.phone.Status;
import study.phonemanagement.entity.phone.Storage;
import study.phonemanagement.service.phone.response.DetailPhoneResponse;
import study.phonemanagement.service.phone.response.ListPhoneResponse;
import study.phonemanagement.service.phone.response.UpdatePhoneResponse;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class PhoneMapperTest extends IntegrationTestSupport {

    @Autowired
    private PhoneMapper phoneMapper;

    @DisplayName("CreatePhoneRequest를 Phone으로 변환한다.")
    @Test
    void toPhone() {
        // given
        CreatePhoneRequest createPhoneRequest = CreatePhoneRequest.builder()
                .name("testPhone")
                .manufacturer(Manufacturer.SAMSUNG)
                .storage(Storage.STORAGE_128)
                .status(Status.AVAILABLE)
                .price(10000)
                .quantity(10)
                .color("testColor")
                .build();

        // when
        Phone phone = phoneMapper.toPhone(createPhoneRequest);

        // then
        assertThat(phone)
                .extracting(
                        Phone::getName, Phone::getManufacturer, Phone::getStorage, Phone::getStatus,
                        Phone::getPrice, Phone::getQuantity, Phone::getColor
                )
                .containsExactly(
                        createPhoneRequest.getName(), createPhoneRequest.getManufacturer(), createPhoneRequest.getStorage(), createPhoneRequest.getStatus(),
                        createPhoneRequest.getPrice(), createPhoneRequest.getQuantity(), createPhoneRequest.getColor()
                );
    }

    @DisplayName("CreatePhoneRequest가 null이면 null을 반환한다.")
    @Test
    void toPhoneWithNull() {
        // given - when
        Phone phone = phoneMapper.toPhone(null);

        // then
        assertThat(phone).isNull();
    }

    @DisplayName("Phone을 PhoneListResponse 으로 변환한다.")
    @Test
    void toPhoneListResponse() {
        // given
        Phone phone = Phone.builder()
                .name("testPhone")
                .manufacturer(Manufacturer.SAMSUNG)
                .storage(Storage.STORAGE_128)
                .status(Status.AVAILABLE)
                .price(10000)
                .quantity(10)
                .color("testColor")
                .build();

        // when
        ListPhoneResponse listPhoneResponse = phoneMapper.toPhoneListResponse(phone);

        // then
        assertThat(listPhoneResponse.getCreatedDate()).isNull();

        assertThat(listPhoneResponse)
                .extracting(
                        ListPhoneResponse::getName, ListPhoneResponse::getManufacturer, ListPhoneResponse::getStorage,
                        ListPhoneResponse::getStatus, ListPhoneResponse::getPrice, ListPhoneResponse::getQuantity, ListPhoneResponse::getColor
                )
                .containsExactly(
                        phone.getName(), phone.getManufacturer(), phone.getStorage(),
                        phone.getStatus(), phone.getPrice(), phone.getQuantity(), phone.getColor()
                );
    }

    @DisplayName("Phone이 null이면 null을 반환한다.")
    @Test
    void toPhoneListResponseWithNull() {
        // given - when
        ListPhoneResponse phone = phoneMapper.toPhoneListResponse(null);

        // then
        assertThat(phone).isNull();
    }

    @DisplayName("Phone을 UpdatePhoneResponse 으로 변환한다.")
    @Test
    void toUpdatePhoneResponse() {
        // given
        Phone phone = Phone.builder()
                .name("testPhone")
                .manufacturer(Manufacturer.SAMSUNG)
                .storage(Storage.STORAGE_128)
                .status(Status.AVAILABLE)
                .price(10000)
                .quantity(10)
                .color("testColor")
                .build();

        // when
        UpdatePhoneResponse updatePhoneResponse = phoneMapper.toUpdatePhoneResponse(phone);

        // then
        assertThat(updatePhoneResponse)
                .extracting(
                        UpdatePhoneResponse::getName, UpdatePhoneResponse::getManufacturer, UpdatePhoneResponse::getStorage, UpdatePhoneResponse::getStatus,
                        UpdatePhoneResponse::getPrice, UpdatePhoneResponse::getQuantity, UpdatePhoneResponse::getColor)
                .containsExactly(
                    phone.getName(), phone.getManufacturer(), phone.getStorage(), phone.getStatus(),
                    phone.getPrice(), phone.getQuantity(), phone.getColor()
                );
    }

    @DisplayName("Phone이 null이면 null을 반환한다.")
    @Test
    void toUpdatePhoneResponseWithNull() {
        // given - when
        UpdatePhoneResponse phone = phoneMapper.toUpdatePhoneResponse(null);

        // then
        assertThat(phone).isNull();
    }

    @DisplayName("Phone을 DetailPhoneResponse 으로 변환한다.")
    @Test
    void toDetailPhoneResponse() {
        // given
        Phone phone = Phone.builder()
                .name("testPhone")
                .manufacturer(Manufacturer.SAMSUNG)
                .storage(Storage.STORAGE_128)
                .status(Status.AVAILABLE)
                .price(10000)
                .quantity(10)
                .color("testColor")
                .build();

        // when
        DetailPhoneResponse detailPhoneResponse = phoneMapper.toDetailPhoneResponse(phone);

        // then
        assertThat(detailPhoneResponse)
                .extracting(
                        DetailPhoneResponse::getName, DetailPhoneResponse::getManufacturer, DetailPhoneResponse::getStorage, DetailPhoneResponse::getStatus,
                        DetailPhoneResponse::getPrice, DetailPhoneResponse::getQuantity, DetailPhoneResponse::getColor)
                .containsExactly(
                        phone.getName(), phone.getManufacturer(), phone.getStorage(), phone.getStatus(),
                        phone.getPrice(), phone.getQuantity(), phone.getColor()
                );

        assertThat(detailPhoneResponse.getCreatedBy()).isNull();
        assertThat(detailPhoneResponse.getLastModifiedBy()).isNull();
        assertThat(detailPhoneResponse.getCreatedDate()).isNull();
        assertThat(detailPhoneResponse.getLastModifiedDate()).isNull();
    }

    @DisplayName("Phone이 null이면 null을 반환한다.")
    @Test
    void toDetailPhoneResponseWithNull() {
        // given - when
        DetailPhoneResponse phone = phoneMapper.toDetailPhoneResponse(null);

        // then
        assertThat(phone).isNull();
    }
}

package study.phonemanagement.mapper.phone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.service.phone.response.CachedListPhoneResponse;
import study.phonemanagement.service.phone.response.DetailPhoneResponse;
import study.phonemanagement.service.phone.response.ListPhoneResponse;
import study.phonemanagement.service.phone.response.UpdatePhoneResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;

class PhoneMapperTest extends IntegrationTestSupport {

    @Autowired
    private PhoneMapper phoneMapper;

    @DisplayName("CreatePhoneRequest를 Phone으로 변환한다.")
    @Test
    void toPhone() {
        // given
        CreatePhoneRequest createPhoneRequest = CreatePhoneRequest.builder()
                .name("testPhone")
                .manufacturer(SAMSUNG)
                .storage(STORAGE_128)
                .status(AVAILABLE)
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
                .manufacturer(SAMSUNG)
                .storage(STORAGE_128)
                .status(AVAILABLE)
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
                .manufacturer(SAMSUNG)
                .storage(STORAGE_128)
                .status(AVAILABLE)
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
                .manufacturer(SAMSUNG)
                .storage(STORAGE_128)
                .status(AVAILABLE)
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

    @DisplayName("ListPhoneResponse 페이지 객체를 CachedListPhoneResponse로 변환한다.")
    @Test
    void toCachedListPhoneResponse() {
        // given
        ListPhoneResponse listPhoneResponse = ListPhoneResponse.builder()
                .id(1L)
                .name("testPhone")
                .manufacturer(SAMSUNG)
                .storage(STORAGE_128)
                .status(AVAILABLE)
                .price(10000)
                .quantity(10)
                .color("testColor")
                .createdDate(LocalDateTime.now())
                .build();

        Page<ListPhoneResponse> page = new PageImpl<>(
                List.of(listPhoneResponse),
                PageRequest.of(0, 1),
                1);

        // when
        CachedListPhoneResponse cachedListPhoneResponse = phoneMapper.toCachedListPhoneResponse(page);

        // then
        assertThat(cachedListPhoneResponse)
                .extracting(CachedListPhoneResponse::getPageSize, CachedListPhoneResponse::getPageNumber, CachedListPhoneResponse::getTotalElements)
                .containsExactly(1, 1, 1L);

        assertThat(cachedListPhoneResponse.getContent())
                .extracting(ListPhoneResponse::getId, ListPhoneResponse::getName, ListPhoneResponse::getManufacturer, ListPhoneResponse::getStorage,
                        ListPhoneResponse::getStatus, ListPhoneResponse::getPrice, ListPhoneResponse::getQuantity, ListPhoneResponse::getColor)
                .containsExactly(tuple(
                        listPhoneResponse.getId(), listPhoneResponse.getName(), listPhoneResponse.getManufacturer(), listPhoneResponse.getStorage(),
                        listPhoneResponse.getStatus(), listPhoneResponse.getPrice(), listPhoneResponse.getQuantity(), listPhoneResponse.getColor())
                );

        assertThat(cachedListPhoneResponse.getContent().get(0).getCreatedDate()).isNotNull();
    }

    @DisplayName("ListPhoneResponse 페이지 객체가 null이면 null을 반환한다.")
    @Test
    void toCachedListPhoneResponseWithNull() {
        // given - when
        CachedListPhoneResponse cachedListPhoneResponse = phoneMapper.toCachedListPhoneResponse(null);

        // then
        assertThat(cachedListPhoneResponse).isNull();
    }

}

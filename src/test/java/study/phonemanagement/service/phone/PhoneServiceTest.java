package study.phonemanagement.service.phone;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.controller.phone.request.UpdatePhoneRequest;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.phone.Status;
import study.phonemanagement.entity.phone.Storage;
import study.phonemanagement.exception.phone.PhoneNotFoundException;
import study.phonemanagement.repository.phone.PhoneRepository;
import study.phonemanagement.service.phone.response.CachedListPhoneResponse;
import study.phonemanagement.service.phone.response.DetailPhoneResponse;
import study.phonemanagement.service.phone.response.ListPhoneResponse;
import study.phonemanagement.service.phone.response.UpdatePhoneResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static study.phonemanagement.common.ErrorCode.PHONE_NOT_FOUND;
import static study.phonemanagement.entity.phone.Manufacturer.*;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;

class PhoneServiceTest extends IntegrationTestSupport {

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private PhoneRepository phoneRepository;

    @AfterEach
    void tearDown() {
        phoneRepository.deleteAllInBatch();
    }

    @DisplayName("휴대폰 등록을 성공하면 휴대폰 정보가 저장된다.")
    @Test
    void createPhone() {
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
        Long savedPhoneId = phoneService.createPhone(createPhoneRequest);

        // then
        Phone phone = phoneRepository.findById(savedPhoneId).orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND));

        assertThat(phone)
                .extracting(Phone::getName, Phone::getManufacturer, Phone::getStorage,
                        Phone::getStatus, Phone::getPrice, Phone::getQuantity, Phone::getColor)

                .containsExactly(createPhoneRequest.getName(), createPhoneRequest.getManufacturer(), createPhoneRequest.getStorage(),
                        createPhoneRequest.getStatus(), createPhoneRequest.getPrice(), createPhoneRequest.getQuantity(), createPhoneRequest.getColor());
    }

    @DisplayName("휴대폰 등록 시 모든 필드에 값이 있어야 한다.")
    @Test
    void createPhoneNullField() {
        // given
        CreatePhoneRequest createPhoneRequest = new CreatePhoneRequest();

        // when - then
        assertThatThrownBy(() -> phoneService.createPhone(createPhoneRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Transactional
    @DisplayName("휴대폰 삭제 시 실제 삭제 대신 소프트 삭제가 수행된다.")
    @Test
    void deletePhone() {
        // given
        Phone phone = createPhoneEntity("testPhone", SAMSUNG, STORAGE_128, AVAILABLE, 10000, 10, "testColor");

        phoneRepository.save(phone);

        // when
        phoneService.deletePhone(phone.getId());

        // then
        assertThat(phone.getDeletedAt()).isNotNull();
    }

    @DisplayName("등록되지 않은 휴대폰을 제거시 예외가 발생한다.")
    @Test
    void deletePhoneNotRegistered() {
        assertThatThrownBy(() -> phoneService.deletePhone(1L))
                .isInstanceOf(PhoneNotFoundException.class)
                .hasMessage(PHONE_NOT_FOUND.getMessage());
    }

    @DisplayName("휴대폰 ID가 존재하면 UpdatePhoneResponse를 반환한다")
    @Test
    void getPhoneForUpdate() {
        // given
        Phone phone = createPhoneEntity("testPhone", SAMSUNG, STORAGE_128, AVAILABLE, 10000, 10, "testColor");

        phoneRepository.save(phone);

        // when
        UpdatePhoneResponse updatePhoneResponse = phoneService.getPhoneForUpdate(phone.getId());

        // then
        assertThat(updatePhoneResponse)
                .extracting(UpdatePhoneResponse::getName, UpdatePhoneResponse::getManufacturer, UpdatePhoneResponse::getStorage, UpdatePhoneResponse::getStatus,
                        UpdatePhoneResponse::getPrice, UpdatePhoneResponse::getQuantity, UpdatePhoneResponse::getColor)
                .containsExactly(phone.getName(), phone.getManufacturer(), phone.getStorage(),
                        phone.getStatus(), phone.getPrice(), phone.getQuantity(), phone.getColor());
    }

    @DisplayName("휴대폰 ID가 존재하지 않으면 PhoneNotFoundException 예외가 발생한다.")
    @Test
    void getPhoneForUpdateNotFound() {
        // given - when - then
        assertThatThrownBy(() -> phoneService.getPhoneForUpdate(1L))
                .isInstanceOf(PhoneNotFoundException.class)
                .hasMessage(PHONE_NOT_FOUND.getMessage());

    }

    @DisplayName("휴대폰 수정을 성공하면 수정된 휴대폰 정보가 저장된다.")
    @Test
    void updatePhone() {
        // given
        Phone phone = createPhoneEntity("testPhone", SAMSUNG, STORAGE_128, AVAILABLE, 10000, 10, "testColor");

        phoneRepository.save(phone);

        // when
        UpdatePhoneRequest updatePhoneRequest = UpdatePhoneRequest.builder()
                .name("testPhone1")
                .manufacturer(SAMSUNG)
                .storage(STORAGE_128)
                .status(AVAILABLE)
                .price(10000)
                .quantity(10)
                .color("testColor")
                .build();

        phoneService.update(phone.getId(), updatePhoneRequest);

        // then
        Phone findPhone = phoneRepository.findById(phone.getId())
                .orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND));

        assertThat(findPhone)
                .extracting(Phone::getName, Phone::getManufacturer, Phone::getStorage,
                        Phone::getStatus, Phone::getPrice, Phone::getQuantity, Phone::getColor)
                .containsExactly(updatePhoneRequest.getName(), updatePhoneRequest.getManufacturer(), updatePhoneRequest.getStorage(),
                        updatePhoneRequest.getStatus(), updatePhoneRequest.getPrice(), updatePhoneRequest.getQuantity(), updatePhoneRequest.getColor());
    }

    @DisplayName("등록되지 않은 휴대폰을 수정하면 예외가 발생한다.")
    @Test
    void updatePhoneNotFound() {
        // given - when - then
        assertThatThrownBy(() -> phoneService.update(1L, new UpdatePhoneRequest()))
                .isInstanceOf(PhoneNotFoundException.class)
                .hasMessage(PHONE_NOT_FOUND.getMessage());
    }

    @DisplayName("휴대폰을 이름, 제조사 기준으로 조회후 최신순으로 정렬 후 반환한다.")
    @Test
    void getAllPhones() {
        // given
        Phone phone1 = createPhoneEntity("testAPPLEPhone1", APPLE, STORAGE_128, AVAILABLE, 10000, 10, "testColor");
        Phone phone2 = createPhoneEntity("testSAMUNGPhone2", SAMSUNG, STORAGE_128, AVAILABLE, 10000, 10, "testColor");
        Phone phone3 = createPhoneEntity("testLGPhone3", LG, STORAGE_128, AVAILABLE, 10000, 10, "testColor");

        phoneRepository.saveAll(List.of(phone1, phone2, phone3));

        // when
        CachedListPhoneResponse page = phoneService.getAllPhones(AVAILABLE, "testAPPLE", APPLE, 1, 2);

        // then
        assertThat(page.getPageNumber()).isEqualTo(1);
        assertThat(page.getPageSize()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(1);

        assertThat(page.getContent())
                .extracting(ListPhoneResponse::getName, ListPhoneResponse::getManufacturer, ListPhoneResponse::getStorage,
                        ListPhoneResponse::getStatus, ListPhoneResponse::getPrice, ListPhoneResponse::getQuantity, ListPhoneResponse::getColor)
                .containsExactlyInAnyOrder(
                        tuple(phone1.getName(), phone1.getManufacturer(), phone1.getStorage(), phone1.getStatus(), phone1.getPrice(), phone1.getQuantity(), phone1.getColor())
                );
    }

    @DisplayName("휴대폰을 이름, 제조사 기준으로 조회후 최신순으로 정렬 후 반환한다.")
    @Test
    void getAllPhonesNoStatus() {
        // given
        Phone phone1 = createPhoneEntity("testAPPLEPhone1", APPLE, STORAGE_128, AVAILABLE, 10000, 10, "testColor");
        Phone phone2 = createPhoneEntity("testSAMUNGPhone2", SAMSUNG, STORAGE_128, AVAILABLE, 10000, 10, "testColor");
        Phone phone3 = createPhoneEntity("testLGPhone3", LG, STORAGE_128, AVAILABLE, 10000, 10, "testColor");

        phoneRepository.saveAll(List.of(phone1, phone2, phone3));

        // when
        CachedListPhoneResponse page = phoneService.getAllPhones("testAPPLE", APPLE, 1, 2);

        // then
        assertThat(page.getPageNumber()).isEqualTo(1);
        assertThat(page.getPageSize()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(1);

        assertThat(page.getContent())
                .extracting(ListPhoneResponse::getName, ListPhoneResponse::getManufacturer, ListPhoneResponse::getStorage,
                        ListPhoneResponse::getStatus, ListPhoneResponse::getPrice, ListPhoneResponse::getQuantity, ListPhoneResponse::getColor)
                .containsExactlyInAnyOrder(
                        tuple(phone1.getName(), phone1.getManufacturer(), phone1.getStorage(), phone1.getStatus(), phone1.getPrice(), phone1.getQuantity(), phone1.getColor())
                );
    }

    @DisplayName("휴대폰을 이름, 제조사 조건이 없다면 휴대폰을 최신순으로 정렬 후 반환한다.")
    @Test
    void getAllPhonesWithNoSearchWordAndNoManufacturer() {
        // given
        Phone phone1 = createPhoneEntity("testAPPLEPhone1", APPLE, STORAGE_128, AVAILABLE, 10000, 10, "testColor");
        Phone phone2 = createPhoneEntity("testSAMUNGPhone2", SAMSUNG, STORAGE_128, AVAILABLE, 10000, 10, "testColor");
        Phone phone3 = createPhoneEntity("testLGPhone3", LG, STORAGE_128, AVAILABLE, 10000, 10, "testColor");

        phoneRepository.saveAll(List.of(phone1, phone2, phone3));

        // when
        CachedListPhoneResponse page = phoneService.getAllPhones(AVAILABLE,null, null, 1, 3);

        // then
        assertThat(page.getPageNumber()).isEqualTo(1);
        assertThat(page.getPageSize()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(3);

        assertThat(page.getContent())
                .extracting(ListPhoneResponse::getName, ListPhoneResponse::getManufacturer, ListPhoneResponse::getStorage,
                        ListPhoneResponse::getStatus, ListPhoneResponse::getPrice, ListPhoneResponse::getQuantity, ListPhoneResponse::getColor)
                .contains(
                        tuple(phone1.getName(), phone1.getManufacturer(), phone1.getStorage(), phone1.getStatus(), phone1.getPrice(), phone1.getQuantity(), phone1.getColor()),
                        tuple(phone2.getName(), phone2.getManufacturer(), phone2.getStorage(), phone2.getStatus(), phone2.getPrice(), phone2.getQuantity(), phone2.getColor()),
                        tuple(phone3.getName(), phone3.getManufacturer(), phone3.getStorage(), phone3.getStatus(), phone3.getPrice(), phone3.getQuantity(), phone3.getColor())

                );
    }

    private Phone createPhoneEntity(String name, Manufacturer manufacturer, Storage storage, Status status, int price, int quantity, String color) {
        return Phone.builder()
                .name(name)
                .manufacturer(manufacturer)
                .storage(storage)
                .status(status)
                .price(price)
                .quantity(quantity)
                .color(color)
                .build();
    }

    @DisplayName("존재하는 ID로 호출하면 휴대폰 상세 정보를 가져온다")
    @Test
    void getPhoneForDetail() {
        // given
        Phone phone = createPhoneEntity("testPhone", SAMSUNG, STORAGE_128, AVAILABLE, 10000, 10, "testColor");

        phoneRepository.save(phone);

        // when
        DetailPhoneResponse phoneResponse = phoneService.getPhoneForDetail(phone.getId());

        // then
        assertThat(phoneResponse)
                .extracting(DetailPhoneResponse::getName, DetailPhoneResponse::getManufacturer, DetailPhoneResponse::getStorage,
                        DetailPhoneResponse::getStatus, DetailPhoneResponse::getPrice, DetailPhoneResponse::getQuantity, DetailPhoneResponse::getColor)
                .containsExactly(phone.getName(), phone.getManufacturer(), phone.getStorage(),
                        phone.getStatus(), phone.getPrice(), phone.getQuantity(), phone.getColor());
    }

    @DisplayName("존재하지 않는 ID로 호출하면 예외가 발생한다.")
    @Test
    void getPhoneForDetailNotFound() {
        // given - when - then
        assertThatThrownBy(() -> phoneService.getPhoneForDetail(1L))
                .isInstanceOf(PhoneNotFoundException.class)
                .hasMessage(PHONE_NOT_FOUND.getMessage());
    }
}


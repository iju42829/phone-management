package study.phonemanagement.repository.phone;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import study.phonemanagement.IntegrationTestSupport;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.entity.phone.Phone;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static study.phonemanagement.entity.phone.Manufacturer.APPLE;
import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;

class PhoneRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private PhoneRepository phoneRepository;

    @AfterEach
    void tearDown() {
        phoneRepository.deleteAllInBatch();
    }

    @DisplayName("검색어와 제조사로 휴대폰 목록 조회 후 등록일 기준으로 내림차순 정렬합니다.")
    @Test
    void findAllPhone() {
        // given
        Phone phone1 = createTestPhoneEntity("testSamsungPhone1", SAMSUNG);
        Phone phone2 = createTestPhoneEntity("testSamsungPhone2", SAMSUNG);
        Phone phone3 = createTestPhoneEntity("testApplePhone", APPLE);

        phoneRepository.saveAll(List.of(phone1, phone2, phone3));

        // when
        Page<Phone> page = phoneRepository.findAllPhone("testSamsungPhone", SAMSUNG, PageRequest.of(0, 3));

        // then
        assertThat(page.getNumberOfElements()).isEqualTo(2);
        assertThat(page.getContent())
                .extracting(Phone::getName, Phone::getManufacturer, Phone::getStorage, Phone::getStatus, Phone::getPrice, Phone::getQuantity, Phone::getColor)
                .containsExactlyInAnyOrder(
                        tuple(phone1.getName(), phone1.getManufacturer(), phone1.getStorage(), phone1.getStatus(), phone1.getPrice(), phone1.getQuantity(), phone1.getColor()),
                        tuple(phone2.getName(), phone2.getManufacturer(), phone2.getStorage(), phone2.getStatus(), phone2.getPrice(), phone2.getQuantity(), phone2.getColor())
                );
    }

    @DisplayName("검색어와 제조사 정보가 없다면 전체 휴대폰 목록 조회 후 등록일 기준으로 내림차순 정렬합니다.")
    @Test
    void findAllPhoneWithNoSearchWordAndNoManufacturer() {
        // given
        Phone phone1 = createTestPhoneEntity("testSamsungPhone1", SAMSUNG);
        Phone phone2 = createTestPhoneEntity("testSamsungPhone2", SAMSUNG);
        Phone phone3 = createTestPhoneEntity("testApplePhone", APPLE);

        phoneRepository.saveAll(List.of(phone1, phone2, phone3));

        // when
        Page<Phone> page = phoneRepository.findAllPhone(null, null, PageRequest.of(0, 3));

        // then
        assertThat(page.getNumberOfElements()).isEqualTo(3);
        assertThat(page.getContent())
                .extracting(Phone::getName, Phone::getManufacturer, Phone::getStorage, Phone::getStatus, Phone::getPrice, Phone::getQuantity, Phone::getColor)
                .containsExactlyInAnyOrder(
                        tuple(phone1.getName(), phone1.getManufacturer(), phone1.getStorage(), phone1.getStatus(), phone1.getPrice(), phone1.getQuantity(), phone1.getColor()),
                        tuple(phone2.getName(), phone2.getManufacturer(), phone2.getStorage(), phone2.getStatus(), phone2.getPrice(), phone2.getQuantity(), phone2.getColor()),
                        tuple(phone3.getName(), phone3.getManufacturer(), phone3.getStorage(), phone3.getStatus(), phone3.getPrice(), phone3.getQuantity(), phone3.getColor())
                );
    }

    private Phone createTestPhoneEntity(String name, Manufacturer manufacturer) {
        return Phone.builder()
                .name(name)
                .manufacturer(manufacturer)
                .storage(STORAGE_128)
                .status(AVAILABLE)
                .price(200000)
                .quantity(10)
                .color("testColor")
                .build();
    }

}

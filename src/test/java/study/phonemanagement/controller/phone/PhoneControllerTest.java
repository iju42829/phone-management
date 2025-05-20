package study.phonemanagement.controller.phone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import study.phonemanagement.ControllerTestSupport;
import study.phonemanagement.service.phone.response.CachedListPhoneResponse;
import study.phonemanagement.service.phone.response.ListPhoneResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;


class PhoneControllerTest extends ControllerTestSupport {

    @DisplayName("휴대폰 목록 페이지에 접근하면 phone 뷰를 반환합니다.")
    @Test
    @WithMockUser
    void phoneMainPage() throws Exception {
        // given
        List<ListPhoneResponse> content = List.of(
                new ListPhoneResponse(
                        1L, "Galaxy", SAMSUNG, STORAGE_128,
                        AVAILABLE, 10000, 5, "Black", LocalDateTime.now()
                )
        );
        CachedListPhoneResponse cachedPage = new CachedListPhoneResponse(content, 1, 20, (long) content.size());
        Page<ListPhoneResponse> expectedPage = new PageImpl<>(
                cachedPage.getContent(),
                PageRequest.of(cachedPage.getPageNumber() - 1, cachedPage.getPageSize()),
                cachedPage.getTotalElements()
        );

        when(phoneService.getAllPhones(any(), any(), anyInt(), anyInt())).thenReturn(cachedPage);

        // when - then
        mockMvc.perform(get("/phones"))
                .andExpect(status().isOk())
                .andExpect(view().name("phones/phone"))
                .andExpect(model().attributeExists("phones"))
                .andExpect(model().attribute("page", expectedPage))
                .andExpect(model().attributeExists("pageNumbers"))
                .andExpect(model().attribute("startPage", 1))
                .andExpect(model().attribute("endPage", 1))
                .andExpect(model().attribute("manufacturer", ""));

        verify(phoneService, times(1))
                .getAllPhones(any(), any(), anyInt(), anyInt());
    }

    @DisplayName("manufacturer를 requestParam으로 받으면 모델에 해당 데이터를 함께 반환한다")
    @Test
    @WithMockUser
    void phoneMainPageWithManufacturer() throws Exception {
        // given
        List<ListPhoneResponse> content = List.of(
                new ListPhoneResponse(
                        1L, "Galaxy", SAMSUNG, STORAGE_128,
                        AVAILABLE, 10000, 5, "Black", LocalDateTime.now()
                )
        );
        CachedListPhoneResponse cachedPage = new CachedListPhoneResponse(content, 1, 20, (long) content.size());
        Page<ListPhoneResponse> expectedPage = new PageImpl<>(
                cachedPage.getContent(),
                PageRequest.of(cachedPage.getPageNumber() - 1, cachedPage.getPageSize()),
                cachedPage.getTotalElements()
        );

        when(phoneService.getAllPhones(any(), any(), anyInt(), anyInt())).thenReturn(cachedPage);

        // when - then
        mockMvc.perform(get("/phones").param("manufacturer", SAMSUNG.name()) )
                .andExpect(status().isOk())
                .andExpect(view().name("phones/phone"))
                .andExpect(model().attributeExists("phones"))
                .andExpect(model().attribute("page", expectedPage))
                .andExpect(model().attributeExists("pageNumbers"))
                .andExpect(model().attribute("startPage", 1))
                .andExpect(model().attribute("endPage", 1))
                .andExpect(model().attribute("manufacturer", SAMSUNG.name()));

        verify(phoneService, times(1))
                .getAllPhones(any(), any(), anyInt(), anyInt());
    }
}

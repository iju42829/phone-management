package study.phonemanagement.controller.phone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import study.phonemanagement.ControllerTestSupport;
import study.phonemanagement.controller.phone.request.CreatePhoneRequest;
import study.phonemanagement.controller.phone.request.UpdatePhoneRequest;
import study.phonemanagement.entity.phone.Manufacturer;
import study.phonemanagement.service.phone.response.CachedListPhoneResponse;
import study.phonemanagement.service.phone.response.ListPhoneResponse;
import study.phonemanagement.service.phone.response.UpdatePhoneResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static study.phonemanagement.entity.phone.Manufacturer.SAMSUNG;
import static study.phonemanagement.entity.phone.Status.AVAILABLE;
import static study.phonemanagement.entity.phone.Storage.STORAGE_128;

class AdminPhoneControllerTest extends ControllerTestSupport {

    @DisplayName("휴대폰 관리자 페이지에 접근하면 adminPhone 뷰를 반환합니다.")
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void phoneMainPage() throws Exception {
        // given
        List<ListPhoneResponse> content = List.of(
                new ListPhoneResponse(1L, "Galaxy", Manufacturer.SAMSUNG, STORAGE_128, AVAILABLE, 10000, 5, "Black", LocalDateTime.now())
        );
        CachedListPhoneResponse cachedPage = new CachedListPhoneResponse(content, 1, 20, (long) content.size());

        Page<ListPhoneResponse> expectedPage = new PageImpl<>(
                cachedPage.getContent(),
                PageRequest.of(cachedPage.getPageNumber() - 1, cachedPage.getPageSize()),
                cachedPage.getTotalElements()
        );

        // stubbing
        when(phoneService.getAllPhones(any(), any(), any(Integer.class), any(Integer.class))).thenReturn(cachedPage);

        // when - then
        mockMvc.perform(get("/admin/phones"))
                .andExpect(status().isOk())
                .andExpect(view().name("phones/adminPhone"))
                .andExpect(model().attributeExists("phones"))
                .andExpect(model().attribute("page", expectedPage))
                .andExpect(model().attributeExists("pageNumbers"))
                .andExpect(model().attribute("startPage", 1))
                .andExpect(model().attribute("endPage", 1))
                .andExpect(model().attribute("manufacturer", ""));

        verify(phoneService, times(1))
                .getAllPhones(any(), any(), any(Integer.class), any(Integer.class));
    }

    @DisplayName("manufacturer를 requestParam으로 받으면 모델에 해당 데이터를 함께 반환한다")
    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void phoneMainPageWithManufacturer() throws Exception {
        // given
        List<ListPhoneResponse> content = List.of(
                new ListPhoneResponse(1L, "Galaxy", Manufacturer.SAMSUNG, STORAGE_128, AVAILABLE, 10000, 5, "Black", LocalDateTime.now())
        );
        CachedListPhoneResponse cachedPage = new CachedListPhoneResponse(content, 1, 20, (long) content.size());

        Page<ListPhoneResponse> expectedPage = new PageImpl<>(
                cachedPage.getContent(),
                PageRequest.of(cachedPage.getPageNumber() - 1, cachedPage.getPageSize()),
                cachedPage.getTotalElements()
        );

        // stubbing
        when(phoneService.getAllPhones(any(), any(), any(Integer.class), any(Integer.class))).thenReturn(cachedPage);

        // when - then
        mockMvc.perform(get("/admin/phones").param("manufacturer", SAMSUNG.name()))
                .andExpect(status().isOk())
                .andExpect(view().name("phones/adminPhone"))
                .andExpect(model().attributeExists("phones"))
                .andExpect(model().attribute("page", expectedPage))
                .andExpect(model().attributeExists("pageNumbers"))
                .andExpect(model().attribute("startPage", 1))
                .andExpect(model().attribute("endPage", 1))
                .andExpect(model().attribute("manufacturer", SAMSUNG.name()));

        verify(phoneService, times(1))
                .getAllPhones(any(), any(), any(Integer.class), any(Integer.class));
    }


    @DisplayName("휴대폰 등록 페이지에 접근하면 adminPhoneCreate 뷰를 반환한다.")
    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    void phoneCreatePage() throws Exception {
        mockMvc.perform(get("/admin/phones/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("phones/adminPhoneCreate"))
                .andExpect(model().attributeExists("phone"));
    }

    @DisplayName("휴대폰 등록 성공 시 휴대폰 관리 목록 페이지로 이동합니다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addPhone() throws Exception {
        // given - when
        mockMvc.perform(
                        post("/admin/phones")
                                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                                .with(csrf())
                                .param("name",         "test")
                                .param("manufacturer", "SAMSUNG")
                                .param("storage",      "STORAGE_128")
                                .param("status",       "AVAILABLE")
                                .param("price",        "10000")
                                .param("quantity",     "5")
                                .param("color",        "testColor")
                )

                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/phones"));

        verify(phoneService, times(1))
                .createPhone(any(CreatePhoneRequest.class));
    }

    @DisplayName("휴대폰 등록 실패시 휴대폰 등록 폼을 반환합니다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addPhoneNoFields() throws Exception {
        // given - when
        mockMvc.perform(
                        post("/admin/phones")
                                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                                .with(csrf())
                )

                // then
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("phone", 7))
                .andExpect(view().name("phones/adminPhoneCreate"));

        verify(phoneService, times(0))
                .createPhone(any(CreatePhoneRequest.class));
    }

    @DisplayName("휴대폰 삭제 후 휴대폰 관리 목록 페이지로 리다이렉트합니다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void removePhone() throws Exception {
        // given - when
        mockMvc.perform(delete("/admin/phones/{phoneId}", 1L)
                        .with(csrf()))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/phones"));

        verify(phoneService, times(1))
                .deletePhone(anyLong());
    }

    @DisplayName("휴대폰 수정 페이지에 접근하면 adminPhoneEdit 뷰를 반환한다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void editPhonePage() throws Exception {
        // given
        UpdatePhoneResponse updatePhoneResponse = UpdatePhoneResponse.builder()
                .name("Galaxy")
                .manufacturer(SAMSUNG)
                .storage(STORAGE_128)
                .status(AVAILABLE)
                .price(10000)
                .quantity(5)
                .color("testColor")
                .build();

        when(phoneService.getPhoneForUpdate(anyLong())).thenReturn(updatePhoneResponse);

        // when - then
        mockMvc.perform(get("/admin/phones/{phoneId}/edit", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("phones/adminPhoneEdit"))
                .andExpect(model().attribute("phoneId", 1L))
                .andExpect(model().attributeExists("phone"));
    }

    @DisplayName("휴대폰 수정 실패시 휴대폰 수정 폼을 반환합니다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void editPhoneNoFields() throws Exception {
        // given - when
        mockMvc.perform(
                        patch("/admin/phones/{phoneId}", 1L)
                                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                                .with(csrf())
                )

                // then
                .andExpect(status().isOk())
                .andExpect(model().attributeErrorCount("phone", 7));

        verify(phoneService, times(0))
                .update(anyLong(), any(UpdatePhoneRequest.class));
    }

    @DisplayName("휴대폰 수정 성공 시 휴대폰 관리 목록 페이지로 리다이렉트합니다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void editPhone() throws Exception {
        // given - when
        mockMvc.perform(
                        patch("/admin/phones/{phoneId}", 1L)
                                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                                .with(csrf())
                                .param("name",         "test")
                                .param("manufacturer", "SAMSUNG")
                                .param("storage",      "STORAGE_128")
                                .param("status",       "AVAILABLE")
                                .param("price",        "10000")
                                .param("quantity",     "5")
                                .param("color",        "testColor")
                )

                // then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/phones"));

        verify(phoneService, times(1))
                .update(anyLong(), any(UpdatePhoneRequest.class));
    }
}

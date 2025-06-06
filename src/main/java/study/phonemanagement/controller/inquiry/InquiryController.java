package study.phonemanagement.controller.inquiry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.phonemanagement.controller.inquiry.request.CreateInquiryRequest;
import study.phonemanagement.service.inquiry.InquiryService;
import study.phonemanagement.service.inquiry.response.DetailInquiryResponse;
import study.phonemanagement.service.user.CustomUserDetails;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping
    public String inquiryUserPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        List<DetailInquiryResponse> inquiryList = inquiryService.getInquiryList(customUserDetails);

        model.addAttribute("inquiries", inquiryList);

        return "inquiry/InquiryUser";
    }

    @GetMapping("/{phoneId}")
    public String createInquiryPage(@PathVariable Long phoneId, Model model) {
        model.addAttribute("createInquiryRequest", new CreateInquiryRequest());
        model.addAttribute("phoneId", phoneId);

        return "inquiry/createInquiry";
    }

    @PostMapping("/{phoneId}")
    public String createInquiry(@PathVariable Long phoneId,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                @Validated @ModelAttribute CreateInquiryRequest createInquiryRequest,
                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("bindingResult: {}", bindingResult);

            return "inquiry/createInquiry";
        }

        inquiryService.createInquiry(phoneId, createInquiryRequest, customUserDetails);

        return "redirect:/phones";
    }
}

package study.phonemanagement.controller.inquiry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.phonemanagement.controller.inquiry.request.ReplyInquiryRequest;
import study.phonemanagement.service.inquiry.InquiryService;
import study.phonemanagement.service.inquiry.response.DetailInquiryResponse;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/inquiries")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final InquiryService inquiryService;

    @GetMapping
    public String inquiryAdminPage(Model model) {
        List<DetailInquiryResponse> inquiryList = inquiryService.getInquiryList();

        model.addAttribute("inquiries", inquiryList);
        model.addAttribute("replyInquiryRequest", new ReplyInquiryRequest());

        return "inquiry/InquiryAdmin";
    }

    @PostMapping("/{inquiryId}")
    public String replyInquiry(@PathVariable Long inquiryId,
                               @Validated @ModelAttribute ReplyInquiryRequest replyInquiryRequest,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("bindingResult: {}", bindingResult);

            return "inquiry/InquiryAdmin";
        }

        inquiryService.replyInquiry(inquiryId, replyInquiryRequest);

        return "redirect:/admin/inquiries";
    }
}

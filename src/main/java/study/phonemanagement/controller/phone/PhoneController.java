package study.phonemanagement.controller.phone;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.phonemanagement.service.phone.PhoneService;
import study.phonemanagement.service.phone.response.PhoneResponse;

import java.util.List;

@Controller
@RequestMapping("/phones")
@RequiredArgsConstructor
public class PhoneController {

    private final PhoneService phoneService;

    @GetMapping
    public String phoneMainPage(Model model) {
        List<PhoneResponse> phones = phoneService.getAllPhones();
        model.addAttribute("phones", phones);

        return "phones/phone";
    }
}

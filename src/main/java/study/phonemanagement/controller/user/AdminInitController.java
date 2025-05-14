package study.phonemanagement.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.service.user.AdminUserService;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminInitController {

    private final AdminUserService adminUserService;

    @GetMapping("/signup")
    public String joinPage(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());
        return "users/adminJoin";
    }

    @PostMapping("/signup")
    public String join(@Validated @ModelAttribute CreateUserRequest createUserRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult: {}", bindingResult);
            return "users/join";
        }

        adminUserService.createAdminUser(createUserRequest);

        return "redirect:/admin/login";
    }
}

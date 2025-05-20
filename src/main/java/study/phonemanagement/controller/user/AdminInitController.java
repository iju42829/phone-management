package study.phonemanagement.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.controller.user.validator.CreateUserRequestValidator;
import study.phonemanagement.service.user.AdminUserService;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminInitController {

    private final AdminUserService adminUserService;
    private final CreateUserRequestValidator creteUserRequestValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(creteUserRequestValidator);
    }

    @GetMapping("/signup")
    public String joinPage(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());
        return "users/adminJoin";
    }

    @PostMapping("/signup")
    public String join(@Validated @ModelAttribute CreateUserRequest createUserRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult: {}", bindingResult);
            return "users/adminJoin";
        }

        adminUserService.createAdminUser(createUserRequest);

        return "redirect:/admin/login";
    }
}

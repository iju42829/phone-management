package study.phonemanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping
    public String testUser() {
        return "main";
    }

    @GetMapping("/admin")
    public String testAdmin() {
        return "admin";
    }
}

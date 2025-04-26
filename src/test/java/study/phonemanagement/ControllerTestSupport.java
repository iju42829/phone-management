package study.phonemanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import study.phonemanagement.config.SecurityConfiguration;
import study.phonemanagement.controller.user.AuthController;
import study.phonemanagement.controller.user.UserController;
import study.phonemanagement.controller.user.validator.CreateUserRequestValidator;
import study.phonemanagement.service.user.UserService;

@WebMvcTest(controllers = {
        UserController.class,
        AuthController.class
})
@Import({
        CreateUserRequestValidator.class,
        SecurityConfiguration.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    UserService userService;
}

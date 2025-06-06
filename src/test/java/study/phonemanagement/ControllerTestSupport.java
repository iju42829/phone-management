package study.phonemanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import study.phonemanagement.config.SecurityConfiguration;
import study.phonemanagement.controller.cart.CartController;
import study.phonemanagement.controller.inquiry.AdminInquiryController;
import study.phonemanagement.controller.inquiry.InquiryController;
import study.phonemanagement.controller.order.AdminDeliveryController;
import study.phonemanagement.controller.order.AdminOrderController;
import study.phonemanagement.controller.order.OrderCartController;
import study.phonemanagement.controller.order.OrderController;
import study.phonemanagement.controller.phone.AdminPhoneController;
import study.phonemanagement.controller.phone.PhoneController;
import study.phonemanagement.controller.user.AdminAuthController;
import study.phonemanagement.controller.user.AdminInitController;
import study.phonemanagement.controller.user.AuthController;
import study.phonemanagement.controller.user.UserController;
import study.phonemanagement.controller.user.validator.CreateUserRequestValidator;
import study.phonemanagement.repository.UserRepository;
import study.phonemanagement.service.cart.CartService;
import study.phonemanagement.service.facade.OrderCartFacade;
import study.phonemanagement.service.inquiry.InquiryService;
import study.phonemanagement.service.order.DeliveryService;
import study.phonemanagement.service.order.OrderService;
import study.phonemanagement.service.phone.PhoneService;
import study.phonemanagement.service.user.AdminUserService;
import study.phonemanagement.service.user.UserService;

@WebMvcTest(controllers = {
        UserController.class,
        AuthController.class,
        PhoneController.class,
        AdminPhoneController.class,
        AdminInitController.class,
        AdminAuthController.class,
        AuthController.class,
        AdminInquiryController.class,
        InquiryController.class,
        AdminDeliveryController.class,
        AdminOrderController.class,
        OrderController.class,
        OrderCartController.class,
        CartController.class
})
@Import({
        CreateUserRequestValidator.class,
        SecurityConfiguration.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected UserService userService;

    @MockitoBean
    protected PhoneService phoneService;

    @MockitoBean
    protected AdminUserService adminUserService;

    @MockitoBean
    protected InquiryService inquiryService;

    @MockitoBean
    protected DeliveryService deliveryService;

    @MockitoBean
    protected OrderService orderService;

    @MockitoBean
    protected UserRepository userRepository;

    @MockitoBean
    protected CartService cartService;

    @MockitoBean
    protected OrderCartFacade orderCartFacade;
}

package study.phonemanagement.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.phonemanagement.controller.order.request.CreateOrderDeliveryRequest;
import study.phonemanagement.controller.order.request.CreateOrderPhoneRequest;
import study.phonemanagement.controller.order.request.CreateOrderRequest;
import study.phonemanagement.service.order.OrderService;
import study.phonemanagement.service.order.response.OrderListResponse;
import study.phonemanagement.service.phone.PhoneService;
import study.phonemanagement.service.phone.response.DetailPhoneResponse;
import study.phonemanagement.service.user.CustomUserDetails;
import study.phonemanagement.service.user.UserService;
import study.phonemanagement.service.user.response.AddressResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PhoneService phoneService;
    private final UserService userService;

    @GetMapping("/{phoneId}")
    public String orderPage(@PathVariable Long phoneId,
                            @AuthenticationPrincipal CustomUserDetails customUserDetails,
                            Model model) {
        DetailPhoneResponse detailPhoneResponse = phoneService.getPhoneForDetail(phoneId);
        AddressResponse addressResponse = userService.getUserAddress(customUserDetails);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setDelivery(CreateOrderDeliveryRequest
                .createOrderDeliveryRequest(addressResponse.getCity(), addressResponse.getCity(), addressResponse.getZipcode(), addressResponse.getDetail()));
        List<CreateOrderPhoneRequest> list = new ArrayList<>();

        CreateOrderPhoneRequest createOrderPhoneRequest1 = new CreateOrderPhoneRequest();
        createOrderPhoneRequest1.setPhoneId(detailPhoneResponse.getId());
        createOrderPhoneRequest1.setCount(1);
        list.add(createOrderPhoneRequest1);

        createOrderRequest.setCreateOrderPhoneRequestList(list);

        model.addAttribute("phone", List.of(detailPhoneResponse));
        model.addAttribute("order", createOrderRequest);

        return "order/orderPhone";
    }

    @PostMapping
    public String addOrder(@ModelAttribute("order") CreateOrderRequest createOrderRequest, @AuthenticationPrincipal CustomUserDetails user) {
        orderService.createOrder(createOrderRequest, user);

        return "redirect:/phones";
    }

    @GetMapping
    public String getOrderList(@RequestParam(defaultValue = "1") Integer pageNumber,
                               @RequestParam(defaultValue = "20") Integer pageSize,
                               @AuthenticationPrincipal CustomUserDetails user, Model model) {
        Page<OrderListResponse> orders = orderService.getOrders(user, pageNumber, pageSize);
        int blockSize = 10;
        int current   = orders.getNumber() + 1;
        int totalPages = orders.getTotalPages();

        int startPage = ((current - 1) / blockSize) * blockSize + 1;
        int endPage   = Math.min(startPage + blockSize - 1, totalPages);

        List<Integer> pageNumbers = IntStream
                .rangeClosed(startPage, endPage)
                .boxed()
                .toList();

        model.addAttribute("orders",      orders.getContent());
        model.addAttribute("page",        orders);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("startPage",   startPage);
        model.addAttribute("endPage",     endPage);

        return "order/list";
    }
}

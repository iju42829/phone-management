package study.phonemanagement.controller.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.phonemanagement.service.order.OrderService;
import study.phonemanagement.service.order.response.OrderListResponse;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public String getOrderList(@RequestParam(required = false) String username,
                               @RequestParam(defaultValue = "1") Integer pageNumber,
                               @RequestParam(defaultValue = "20") Integer pageSize,
                               Model model) {

        Page<OrderListResponse> orders = orderService.getOrdersByUsername(username, pageNumber, pageSize);

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

        return "order/adminList";
    }
}

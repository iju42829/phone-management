package study.phonemanagement.service.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import study.phonemanagement.controller.cart.request.CreateCartOrderPhoneRequest;
import study.phonemanagement.controller.cart.request.CreateCartOrderRequest;
import study.phonemanagement.controller.order.request.CreateOrderPhoneRequest;
import study.phonemanagement.controller.order.request.CreateOrderRequest;
import study.phonemanagement.entity.order.Delivery;
import study.phonemanagement.entity.order.Order;
import study.phonemanagement.entity.order.OrderPhone;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.order.OrderOptimisticLockingException;
import study.phonemanagement.exception.phone.PhoneNotFoundException;
import study.phonemanagement.exception.user.UserNotFoundException;
import study.phonemanagement.mapper.order.OrderPhoneMapper;
import study.phonemanagement.repository.order.OrderRepository;
import study.phonemanagement.repository.UserRepository;
import study.phonemanagement.repository.phone.PhoneRepository;
import study.phonemanagement.service.order.response.OrderListResponse;
import study.phonemanagement.service.order.response.OrderPhoneDetailResponse;
import study.phonemanagement.service.user.CustomUserDetails;

import java.util.ArrayList;
import java.util.List;

import static study.phonemanagement.common.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;

    private final OrderPhoneMapper orderPhoneMapper;

    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 10,
            backoff = @Backoff(delay = 1500)
    )
    @Override
    public Long createOrder(CreateOrderRequest createOrderRequest, CustomUserDetails customUserDetails) {

        List<OrderPhone> orderPhoneList = new ArrayList<>();

        for (CreateOrderPhoneRequest createOrderPhoneRequest : createOrderRequest.getCreateOrderPhoneRequestList()) {
            Phone phone = phoneRepository
                    .findById(createOrderPhoneRequest.getPhoneId()).
                    orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND));

            OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, createOrderPhoneRequest.getCount(), phone.getPrice());

            orderPhoneList.add(orderPhone);
        }

        User user = userRepository
                .findByUsername(customUserDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Delivery delivery = Delivery.createDelivery(
                createOrderRequest.getDelivery().getCity(),
                createOrderRequest.getDelivery().getStreet(),
                createOrderRequest.getDelivery().getZipcode(),
                createOrderRequest.getDelivery().getDetail()
        );

        Order order = Order.createOrder(user, delivery, orderPhoneList);

        orderRepository.save(order);

        return order.getId();
    }

    @Recover
    public Long recover(ObjectOptimisticLockingFailureException ex, CreateOrderRequest createOrderRequest, CustomUserDetails customUserDetails) {
        Long phoneId = createOrderRequest.getCreateOrderPhoneRequestList().get(0).getPhoneId();

        throw new OrderOptimisticLockingException(ex, ORDER_CONCURRENCY_FAILURE, phoneId);
    }

    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 10,
            backoff = @Backoff(delay = 1500)
    )
    @Override
    public Long createOrderByCart(CreateCartOrderRequest createCartOrderRequest, CustomUserDetails customUserDetails) {
        List<OrderPhone> orderPhoneList = new ArrayList<>();

        for (CreateCartOrderPhoneRequest createCartOrderPhoneRequest : createCartOrderRequest.getCreateCartOrderPhoneRequests()) {
            Phone phone = phoneRepository
                    .findById(createCartOrderPhoneRequest.getPhoneId()).
                    orElseThrow(() -> new PhoneNotFoundException(PHONE_NOT_FOUND));

            OrderPhone orderPhone = OrderPhone.createOrderPhone(phone, createCartOrderPhoneRequest.getCount(), phone.getPrice());

            orderPhoneList.add(orderPhone);
        }

        User user = userRepository
                    .findByUsername(customUserDetails.getUsername())
                    .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Delivery delivery = Delivery.createDelivery(
                createCartOrderRequest.getDelivery().getCity(),
                createCartOrderRequest.getDelivery().getStreet(),
                createCartOrderRequest.getDelivery().getZipcode(),
                createCartOrderRequest.getDelivery().getDetail()
        );

        Order order = Order.createOrder(user, delivery, orderPhoneList);

        orderRepository.save(order);

        return order.getId();
    }

    @Recover
    public Long recover(ObjectOptimisticLockingFailureException ex, CreateCartOrderRequest createOrderRequest, CustomUserDetails customUserDetails) {
        throw new OrderOptimisticLockingException(ex, ORDER_CONCURRENCY_FAILURE, null);
    }

    @Override
    public Page<OrderListResponse> getOrders(CustomUserDetails customUserDetails, Integer pageNumber, Integer pageSize) {
        User user = userRepository.findByUsername(customUserDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "createdDate")
        );


        Page<Order> orders = orderRepository.findAllByUser(user, pageable);

        return orders.map(order -> {
            List<OrderPhoneDetailResponse> phoneDetailResponses = order.getOrderPhones().stream()
                    .map(orderPhoneMapper::toOrderPhoneDetailResponse)
                    .toList();

            return OrderListResponse.builder()
                    .orderId(order.getId())
                    .orderedAt(order.getCreatedDate())
                    .orderStatus(order.getStatus().name())
                    .deliveryStatus(order.getDelivery().getStatus().name())
                    .totalAmount(order.getTotalPrice())
                    .orderPhoneDetailResponseList(phoneDetailResponses)
                    .build();
        });
    }

    @Override
    public Page<OrderListResponse> getOrdersByUsername(String username, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "createdDate")
        );

        Page<Order> orders = orderRepository.findAllOrderByUsername(username, pageable);

        return orders.map(order -> {
            List<OrderPhoneDetailResponse> phoneDetailResponses = order.getOrderPhones().stream()
                    .map(orderPhoneMapper::toOrderPhoneDetailResponse)
                    .toList();

            return OrderListResponse.builder()
                    .orderId(order.getId())
                    .orderedAt(order.getCreatedDate())
                    .orderStatus(order.getStatus().name())
                    .deliveryStatus(order.getDelivery().getStatus().name())
                    .totalAmount(order.getTotalPrice())
                    .orderPhoneDetailResponseList(phoneDetailResponses)
                    .build();
        });
    }
}

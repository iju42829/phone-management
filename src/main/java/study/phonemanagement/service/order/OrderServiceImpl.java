package study.phonemanagement.service.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
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
import study.phonemanagement.repository.OrderRepository;
import study.phonemanagement.repository.UserRepository;
import study.phonemanagement.repository.phone.PhoneRepository;
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
}

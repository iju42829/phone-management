package study.phonemanagement.advice.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import study.phonemanagement.controller.order.OrderController;
import study.phonemanagement.exception.phone.PhoneNotFoundException;
import study.phonemanagement.exception.user.UserNotFoundException;

@Slf4j
@ControllerAdvice(assignableTypes = {OrderController.class})
public class OrderControllerAdvice {

    @ExceptionHandler(PhoneNotFoundException.class)
    public ModelAndView handlePhoneNotFoundException(PhoneNotFoundException e) {
        log.warn("[OrderController - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(UserNotFoundException e) {
        log.warn("[OrderController - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }
}

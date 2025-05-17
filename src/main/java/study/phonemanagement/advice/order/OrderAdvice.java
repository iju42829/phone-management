package study.phonemanagement.advice.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import study.phonemanagement.exception.order.*;

@Slf4j
@ControllerAdvice
public class OrderAdvice {

    @ExceptionHandler(OrderCancelForbiddenException.class)
    public ModelAndView handleOrderCancelForbiddenException(OrderCancelForbiddenException e) {
        log.warn("[Order - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }

    @ExceptionHandler(OrderCannotBeCancelledException.class)
    public ModelAndView handleOrderCannotBeCancelledException(OrderCannotBeCancelledException e) {
        log.warn("[Order - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ModelAndView handleOrderNotFoundException(OrderNotFoundException e) {
        log.warn("[Order - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }

    @ExceptionHandler(OrderOptimisticLockingException.class)
    public ModelAndView handleOrderOptimisticLockingException(OrderOptimisticLockingException e) {
        log.warn("[Order - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }

    @ExceptionHandler(OrderPhoneNotFoundException.class)
    public ModelAndView handleOrderPhoneNotFoundException(OrderPhoneNotFoundException e) {
        log.warn("[Order - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }
}

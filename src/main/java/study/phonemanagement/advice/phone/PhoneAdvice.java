package study.phonemanagement.advice.phone;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import study.phonemanagement.exception.phone.PhoneNotFoundException;
import study.phonemanagement.exception.phone.PhoneStockShortageException;

@Slf4j
@ControllerAdvice
public class PhoneAdvice {
    @ExceptionHandler(PhoneNotFoundException.class)
    public ModelAndView handlePhoneNotFoundException(PhoneNotFoundException e) {
        log.warn("[Phone - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }

    @ExceptionHandler(PhoneStockShortageException.class)
    public ModelAndView handlePhoneStockShortageException(PhoneStockShortageException e) {
        log.warn("[Phone - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }
}

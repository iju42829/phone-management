package study.phonemanagement.advice.phone;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import study.phonemanagement.controller.phone.AdminPhoneController;
import study.phonemanagement.exception.phone.PhoneNotFoundException;

@Slf4j
@ControllerAdvice(assignableTypes = {AdminPhoneController.class})
public class AdminPhoneControllerAdvice {

    @ExceptionHandler(PhoneNotFoundException.class)
    public ModelAndView handlePhoneNotFoundException(PhoneNotFoundException e) {
        log.warn("[AdminPhoneController - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }
}

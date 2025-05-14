package study.phonemanagement.advice.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import study.phonemanagement.controller.user.AdminInitController;
import study.phonemanagement.exception.user.AlreadyExistsAdminException;

@Slf4j
@ControllerAdvice(assignableTypes = {AdminInitController.class})
public class AdminInitControllerAdvice {

    @ExceptionHandler(AlreadyExistsAdminException.class)
    public ModelAndView handleAlreadyExistsAdminException(AlreadyExistsAdminException e) {
        log.warn("[AdminInitController - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }
}

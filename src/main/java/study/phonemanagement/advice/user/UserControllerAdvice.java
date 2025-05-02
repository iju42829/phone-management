package study.phonemanagement.advice.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import study.phonemanagement.controller.user.UserController;
import study.phonemanagement.exception.user.UserNotFoundException;

@Slf4j
@ControllerAdvice(assignableTypes = {UserController.class})
public class UserControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(UserNotFoundException e) {
        log.warn("[UserController - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }
}

package study.phonemanagement.advice.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import study.phonemanagement.exception.user.AlreadyExistsAdminException;
import study.phonemanagement.exception.user.AlreadyExistsEmailException;
import study.phonemanagement.exception.user.AlreadyExistsUsernameException;
import study.phonemanagement.exception.user.UserNotFoundException;

@Slf4j
@ControllerAdvice
public class UserAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(UserNotFoundException e) {
        log.warn("[User - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }

    @ExceptionHandler(AlreadyExistsAdminException.class)
    public ModelAndView handleAlreadyExistsAdminException(AlreadyExistsAdminException e) {
        log.warn("[User - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }

    @ExceptionHandler(AlreadyExistsEmailException.class)
    public ModelAndView handleAlreadyExistsEmailException(AlreadyExistsEmailException e) {
        log.warn("[User - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }

    @ExceptionHandler(AlreadyExistsUsernameException.class)
    public ModelAndView handleAlreadyExistsUsernameException(AlreadyExistsUsernameException e) {
        log.warn("[User - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }
}

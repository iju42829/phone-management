package study.phonemanagement.advice.Inquiry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import study.phonemanagement.exception.Inquiry.InquiryNotFoundException;

@Slf4j
@ControllerAdvice
public class InquiryAdvice {

    @ExceptionHandler(InquiryNotFoundException.class)
    public ModelAndView handleInquiryNotFoundException(InquiryNotFoundException e) {
        log.warn("[Inquiry - exceptionHandle] ex", e);

        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(e.getErrorCode().getHttpStatus());
        mav.addObject("message", e.getErrorCode().getMessage());
        mav.addObject("redirectUrl", e.getErrorCode().getRedirectUrl());

        return mav;
    }
}

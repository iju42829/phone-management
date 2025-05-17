package study.phonemanagement.exception.Inquiry;

import lombok.Getter;
import study.phonemanagement.common.ErrorCode;

import java.util.NoSuchElementException;

@Getter
public class InquiryNotFoundException extends NoSuchElementException {

    private final ErrorCode errorCode;

    public InquiryNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

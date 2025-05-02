package study.phonemanagement.exception.phone;

import lombok.Getter;
import study.phonemanagement.common.ErrorCode;

import java.util.NoSuchElementException;

@Getter
public class PhoneNotFoundException extends NoSuchElementException {

    private final ErrorCode errorCode;

    public PhoneNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

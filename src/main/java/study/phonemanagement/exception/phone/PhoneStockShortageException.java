package study.phonemanagement.exception.phone;

import lombok.Getter;
import study.phonemanagement.common.ErrorCode;

@Getter
public class PhoneStockShortageException extends RuntimeException{

    private final ErrorCode errorCode;

    public PhoneStockShortageException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

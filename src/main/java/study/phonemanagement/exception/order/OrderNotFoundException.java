package study.phonemanagement.exception.order;

import lombok.Getter;
import study.phonemanagement.common.ErrorCode;

import java.util.NoSuchElementException;

@Getter
public class OrderNotFoundException extends NoSuchElementException {
    private final ErrorCode errorCode;

    public OrderNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

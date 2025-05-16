package study.phonemanagement.exception.order;

import lombok.Getter;
import study.phonemanagement.common.ErrorCode;

@Getter
public class OrderCannotBeCancelledException extends IllegalStateException {

    private final ErrorCode errorCode;

    public OrderCannotBeCancelledException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

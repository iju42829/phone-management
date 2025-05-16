package study.phonemanagement.exception.order;

import lombok.Getter;
import study.phonemanagement.common.ErrorCode;

@Getter
public class OrderCancelForbiddenException extends IllegalStateException{

    private final ErrorCode errorCode;

    public OrderCancelForbiddenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

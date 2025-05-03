package study.phonemanagement.exception.order;

import lombok.Getter;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import study.phonemanagement.common.ErrorCode;

@Getter
public class OrderOptimisticLockingException extends ObjectOptimisticLockingFailureException {
    private final ErrorCode errorCode;
    private final Long phoneId;


    public OrderOptimisticLockingException(Throwable cause, ErrorCode errorCode, Long phoneId) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.phoneId = phoneId;
    }
}

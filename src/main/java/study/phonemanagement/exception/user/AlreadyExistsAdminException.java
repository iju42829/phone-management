package study.phonemanagement.exception.user;

import lombok.Getter;
import study.phonemanagement.common.ErrorCode;

@Getter
public class AlreadyExistsAdminException extends IllegalStateException {

    private final ErrorCode errorCode;

    public AlreadyExistsAdminException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

package study.phonemanagement.exception.user;

import lombok.Getter;
import study.phonemanagement.common.ErrorCode;

@Getter
public class AlreadyExistsEmailException extends IllegalStateException {
    private final ErrorCode errorCode;

    public AlreadyExistsEmailException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

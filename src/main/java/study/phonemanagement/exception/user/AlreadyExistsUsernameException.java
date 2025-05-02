package study.phonemanagement.exception.user;

import lombok.Getter;
import study.phonemanagement.common.ErrorCode;

@Getter
public class AlreadyExistsUsernameException extends IllegalStateException {
    private final ErrorCode errorCode;

    public AlreadyExistsUsernameException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

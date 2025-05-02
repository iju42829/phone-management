package study.phonemanagement.exception.user;

import lombok.Getter;
import study.phonemanagement.common.ErrorCode;

import java.util.NoSuchElementException;

@Getter
public class UserNotFoundException extends NoSuchElementException {

    private final ErrorCode errorCode;

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}

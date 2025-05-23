package study.phonemanagement.exception.cart;

import lombok.Getter;
import study.phonemanagement.common.ErrorCode;

import java.util.NoSuchElementException;

@Getter
public class CartNotFoundException extends NoSuchElementException {
    private final ErrorCode errorCode;

    public CartNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

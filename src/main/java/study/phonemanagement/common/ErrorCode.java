package study.phonemanagement.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // USER
    USER_DUPLICATE_USERNAME(HttpStatus.CONFLICT,"이미 사용 중인 아이디입니다.", "/users/join"),
    USER_DUPLICATE_EMAIL(HttpStatus.CONFLICT,"이미 사용 중인 이메일입니다.", "/users/join"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다.", "/users/login"),

    // PHONE
    PHONE_NOT_FOUND(HttpStatus.NOT_FOUND,"휴대폰을 찾을 수 없습니다.", "/phones");

    private final HttpStatus httpStatus;
    private final String message;
    private final String redirectUrl;
}

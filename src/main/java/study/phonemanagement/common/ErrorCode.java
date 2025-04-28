package study.phonemanagement.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // USER
    USER_DUPLICATE_USERNAME("이미 사용 중인 아이디입니다."),
    USER_DUPLICATE_EMAIL("이미 사용 중인 이메일입니다."),
    USER_NOT_FOUND("해당 사용자를 찾을 수 없습니다."),

    // PHONE
    PHONE_NOT_FOUND("휴대폰을 찾을 수 없습니다.");

    private final String message;
}

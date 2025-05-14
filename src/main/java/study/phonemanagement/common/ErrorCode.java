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
    ADMIN_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 관리자가 존재합니다.", "/admin/login"),

    // PHONE
    PHONE_NOT_FOUND(HttpStatus.NOT_FOUND,"휴대폰을 찾을 수 없습니다.", "/phones"),

    // ORDER
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다.", "/"),
    ORDER_CONCURRENCY_FAILURE(HttpStatus.CONFLICT, "동시성 충돌로 주문 처리에 실패했습니다.", "/orders"),
    // ORDER_PHONE
    ORDER_PHONE_NOT_FOUND(HttpStatus.NOT_FOUND, "주문한 휴대폰을 찾을 수 없습니다.", "/");


    private final HttpStatus httpStatus;
    private final String message;
    private final String redirectUrl;
}

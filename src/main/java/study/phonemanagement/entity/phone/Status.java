package study.phonemanagement.entity.phone;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    AVAILABLE("판매중"), OUT_OF_STOCK("품절"), STOPPING_SALE("판매 중단");

    private final String description;
}

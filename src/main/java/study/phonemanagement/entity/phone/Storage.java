package study.phonemanagement.entity.phone;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Storage {
    STORAGE_128("128GB"),
    STORAGE_256("256GB"),
    STORAGE_512("512GB"),
    STORAGE_1024("1TB");

    private final String description;
}

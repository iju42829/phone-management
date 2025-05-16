package study.phonemanagement.service.inquiry.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailInquiryResponse {
    private Long id;
    private String phoneName;
    private String content;
    private String reply;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}

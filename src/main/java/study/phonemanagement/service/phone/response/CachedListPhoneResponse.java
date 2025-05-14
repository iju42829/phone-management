package study.phonemanagement.service.phone.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CachedListPhoneResponse {
    private List<ListPhoneResponse> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
}

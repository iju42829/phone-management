package study.phonemanagement.entity.inquiry;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.phonemanagement.entity.BaseEntity;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Phone phone;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    private String content;

    private String reply;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InquiryStatus inquiryStatus;

    @Builder
    private Inquiry(Phone phone, User user, String content, InquiryStatus inquiryStatus) {
        this.phone = phone;
        this.user = user;
        this.content = content;
        this.inquiryStatus = inquiryStatus;
    }

    public void reply(String reply) {
        this.reply = reply;
        this.inquiryStatus = InquiryStatus.REPLIED;
    }
}

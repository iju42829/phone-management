package study.phonemanagement.repository.inquiry;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import study.phonemanagement.entity.inquiry.Inquiry;
import study.phonemanagement.entity.inquiry.InquiryStatus;
import study.phonemanagement.entity.user.User;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @EntityGraph(attributePaths = {"phone"})
    List<Inquiry> findWithPhoneByUser(User user);

    @EntityGraph(attributePaths = {"phone"})
    List<Inquiry> findWithPhoneByInquiryStatus(InquiryStatus inquiryStatus);
}

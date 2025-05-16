package study.phonemanagement.repository.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;
import study.phonemanagement.entity.inquiry.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}

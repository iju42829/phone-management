package study.phonemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.phonemanagement.entity.phone.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
}

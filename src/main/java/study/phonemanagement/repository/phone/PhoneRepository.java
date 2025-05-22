package study.phonemanagement.repository.phone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.phonemanagement.entity.phone.Phone;
import study.phonemanagement.entity.phone.Status;

import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long>, PhoneRepositoryCustom {

    Optional<Phone> findByIdAndDeletedAtIsNull(Long id);
    Optional<Phone> findByIdAndStatusAndDeletedAtIsNull(Long id, Status status);
}

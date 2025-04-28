package study.phonemanagement.repository.phone;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.phonemanagement.entity.phone.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long>, PhoneRepositoryCustom {
    Page<Phone> findAllByNameStartingWith(String searchWord, Pageable pageable);
}

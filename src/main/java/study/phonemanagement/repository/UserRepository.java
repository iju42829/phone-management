package study.phonemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.phonemanagement.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

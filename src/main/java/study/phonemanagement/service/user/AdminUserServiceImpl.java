package study.phonemanagement.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.phonemanagement.common.ErrorCode;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.user.Role;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.user.AlreadyExistsAdminException;
import study.phonemanagement.mapper.user.UserMapper;
import study.phonemanagement.repository.UserRepository;

import static study.phonemanagement.entity.user.Role.ADMIN;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    public Long createAdminUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByRole(Role.ADMIN)) {
            throw new AlreadyExistsAdminException(ErrorCode.ADMIN_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(createUserRequest.getPassword());

        User user = userMapper.toUser(createUserRequest);
        user.changePassword(encodedPassword);
        user.changeRole(ADMIN);
        userRepository.save(user);

        return user.getId();
    }
}

package study.phonemanagement.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.mapper.user.UserMapper;
import study.phonemanagement.repository.UserRepository;

import static study.phonemanagement.entity.user.Role.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Long createUser(CreateUserRequest createUserRequest) {
        String encodedPassword = passwordEncoder.encode(createUserRequest.getPassword());

        User user = userMapper.toUser(createUserRequest);
        user.changePassword(encodedPassword);
        user.changeRole(USER);
        userRepository.save(user);

        return user.getId();
    }
}

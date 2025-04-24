package study.phonemanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.mapper.user.UserMapper;
import study.phonemanagement.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Long createUser(CreateUserRequest createUserRequest) {
        User user = userMapper.toUser(createUserRequest);

        userRepository.save(user);

        return user.getId();
    }
}

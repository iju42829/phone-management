package study.phonemanagement.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.exception.user.AlreadyExistsEmailException;
import study.phonemanagement.exception.user.AlreadyExistsUsernameException;
import study.phonemanagement.exception.user.UserNotFoundException;
import study.phonemanagement.mapper.user.UserMapper;
import study.phonemanagement.repository.UserRepository;
import study.phonemanagement.service.user.response.AddressResponse;

import static study.phonemanagement.common.ErrorCode.*;
import static study.phonemanagement.entity.user.Role.USER;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Long createUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new AlreadyExistsUsernameException(USER_DUPLICATE_USERNAME);
        }

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new AlreadyExistsEmailException(USER_DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(createUserRequest.getPassword());

        User user = userMapper.toUser(createUserRequest);
        user.changePassword(encodedPassword);
        user.changeRole(USER);
        userRepository.save(user);

        return user.getId();
    }

    @Override
    public AddressResponse getUserAddress(CustomUserDetails customUserDetails) {
        User user = userRepository
                .findByUsername(customUserDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        return userMapper.toAddressResponse(user);
    }

}

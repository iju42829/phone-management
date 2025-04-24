package study.phonemanagement.mapper.user;

import org.springframework.stereotype.Component;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.user.User;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User toUser(CreateUserRequest createUserRequest) {
        if (createUserRequest == null) {
            return null;
        }

        return User.builder()
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword())
                .gender(createUserRequest.getGender())
                .build();
    }
}

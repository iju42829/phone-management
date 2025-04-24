package study.phonemanagement.mapper.user;

import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.user.User;

public interface UserMapper {
    User toUser(CreateUserRequest createUserRequest);
}

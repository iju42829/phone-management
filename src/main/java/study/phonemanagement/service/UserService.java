package study.phonemanagement.service;

import study.phonemanagement.controller.user.request.CreateUserRequest;

public interface UserService {

    Long createUser(CreateUserRequest createUserRequest);
}

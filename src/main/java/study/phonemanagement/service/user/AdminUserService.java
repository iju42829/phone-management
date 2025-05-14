package study.phonemanagement.service.user;

import study.phonemanagement.controller.user.request.CreateUserRequest;

public interface AdminUserService {
    Long createAdminUser(CreateUserRequest createUserRequest);
}
